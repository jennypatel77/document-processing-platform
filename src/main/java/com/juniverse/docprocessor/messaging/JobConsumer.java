package com.juniverse.docprocessor.messaging;

import com.juniverse.docprocessor.config.RabbitMQConfig;
import com.juniverse.docprocessor.config.TraceIdFilter;
import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.entity.JobStatus;
import com.juniverse.docprocessor.repository.JobRepository;
import com.juniverse.docprocessor.service.JobProcessingService;
import com.juniverse.docprocessor.service.JobWriteService;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JobConsumer {

    private static final int MAX_RETRIES = 3;
    private static final Logger log = LoggerFactory.getLogger(JobConsumer.class);

    private final JobRepository jobRepository;
    private final JobProducer jobProducer;
    private final JobProcessingService jobProcessingService;
    private final JobFailureClassifier jobFailureClassifier;
    private final JobWriteService jobWriteService;

    public JobConsumer(JobRepository jobRepository,
                       JobProducer jobProducer,
                       JobProcessingService jobProcessingService,
                       JobFailureClassifier jobFailureClassifier,
                       JobWriteService jobWriteService) {
        this.jobRepository = jobRepository;
        this.jobProducer = jobProducer;
        this.jobProcessingService = jobProcessingService;
        this.jobFailureClassifier = jobFailureClassifier;
        this.jobWriteService = jobWriteService;
    }

    @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE)
    public void consume(JobMessage jobMessage) {
        try {
            if (jobMessage.getTraceId() != null) {
                MDC.put(TraceIdFilter.TRACE_ID, jobMessage.getTraceId());
            }
            if (jobMessage.getJobId() != null) {
                MDC.put("jobId", String.valueOf(jobMessage.getJobId()));
            }

            log.info("Received job message retryCount={} fileName={}",
                    jobMessage.getRetryCount(), jobMessage.getFileName());

            Job job = jobRepository.findById(jobMessage.getJobId())
                    .orElseThrow(() -> new RuntimeException("Job not found: " + jobMessage.getJobId()));

            if (job.getStatus() == JobStatus.COMPLETED) {
                log.info("Job already completed, skipping duplicate delivery");
                return;
            }

        try {
            job.setStatus(JobStatus.PROCESSING);
            job.setErrorMessage(null);
            jobWriteService.updateJob(job, JobStatus.PROCESSING, null);

            jobProcessingService.process(job);

            jobWriteService.updateJob(job, JobStatus.COMPLETED, null);

            log.info("Job completed successfully: {}", job.getId());

        } catch (Exception e) {
            int currentRetry = jobMessage.getRetryCount() == null ? 0 : jobMessage.getRetryCount();

            job.setErrorMessage(e.getMessage());

            if (jobFailureClassifier.isNonRetryable(e)) {
                jobWriteService.updateJob(job, JobStatus.FAILED, null);

                jobProducer.sendToDlq(jobMessage);
                log.info("Non-retryable failure. Sent to DLQ: {}", job.getId());
                return;
            }

            if (jobFailureClassifier.isRetryable(e) && currentRetry < MAX_RETRIES) {
                jobWriteService.updateJob(job, JobStatus.PENDING, null);

                jobMessage.setRetryCount(currentRetry + 1);
                jobProducer.sendToRetry(jobMessage);

                log.info("Retryable failure. Sent to retry queue: {} retryCount={}", job.getId(), jobMessage.getRetryCount());
                return;
            }

            jobWriteService.updateJob(job, JobStatus.FAILED, null);

            jobProducer.sendToDlq(jobMessage);
            log.info("Retries exhausted or unknown failure. Sent to DLQ: {}", job.getId());
        }
        } finally {
            MDC.clear();
        }
    }
}
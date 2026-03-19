package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.config.TraceIdFilter;
import com.juniverse.docprocessor.dto.CreateJobRequest;
import com.juniverse.docprocessor.dto.JobResponse;
import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.entity.JobStatus;
import com.juniverse.docprocessor.exception.JobNotFoundException;
import com.juniverse.docprocessor.mapper.JobMapper;
import com.juniverse.docprocessor.messaging.JobConsumer;
import com.juniverse.docprocessor.messaging.JobMessage;
import com.juniverse.docprocessor.messaging.JobProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.juniverse.docprocessor.repository.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobConsumer.class);


    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JobProducer jobProducer;

    public JobServiceImpl(JobRepository jobRepository,
                          JobMapper jobMapper,
                          JobProducer jobProducer) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.jobProducer = jobProducer;
    }

    @Override
    public JobResponse createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setFileName(request.getFileName());
        job.setStatus(JobStatus.PENDING);

            Job savedJob = jobRepository.save(job);

            JobMessage jobMessage = new JobMessage();
            jobMessage.setJobId(savedJob.getId());
            jobMessage.setTraceId(MDC.get(TraceIdFilter.TRACE_ID));
            jobMessage.setFileName(savedJob.getFileName());
            jobMessage.setRetryCount(0);

            jobProducer.sendJob(jobMessage);

            return jobMapper.toResponse(savedJob);
    }

    @Override
    @Cacheable(value = "jobs", key = "#id")
    public JobResponse getJobById(Long id) {
        log.info("Fetching job from DB for id: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));

        return jobMapper.toResponse(job);
    }

    @Override
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
    }
}

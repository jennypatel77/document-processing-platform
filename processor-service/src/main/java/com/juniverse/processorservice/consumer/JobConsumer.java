package com.juniverse.processorservice.consumer;

import com.juniverse.processorservice.messaging.JobMessage;
import com.juniverse.processorservice.service.JobProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component

public class JobConsumer {

    private static final Logger log = LoggerFactory.getLogger(JobConsumer.class);
    private final JobProcessingService jobProcessingService;

    public JobConsumer(JobProcessingService jobProcessingService) {
        this.jobProcessingService = jobProcessingService;
    }

    @RabbitListener(queues = "job.queue")
    public void process(JobMessage message) {
        log.info("Received jobId={} payload={}", message.getJobId(), message.getFileName());

        jobProcessingService.process(message);
        log.info("Finished processing jobId={}", message.getJobId());
    }
}
package com.juniverse.processorservice.service;

import com.juniverse.processorservice.messaging.JobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class JobProcessingService {

    private static final Logger log = LoggerFactory.getLogger(JobProcessingService.class);

    public void process(JobMessage message) {
        log.info("Started processing jobId={} payload={}", message.getJobId(), message.getPayload());

        try {
            Thread.sleep(3000);
            log.info("Completed processing jobId={}", message.getJobId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Processing interrupted for jobId={}", message.getJobId(), e);
        }
    }
}
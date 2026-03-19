package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.exception.BusinessRuleViolationException;
import com.juniverse.docprocessor.exception.DependentServiceUnavailableException;
import com.juniverse.docprocessor.exception.InvalidPayloadException;
import com.juniverse.docprocessor.exception.JobTimeoutException;
import com.juniverse.docprocessor.exception.MissingRequiredFieldException;
import com.juniverse.docprocessor.exception.TemporaryNetworkException;
import com.juniverse.docprocessor.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Service;

@Service
public class JobProcessingServiceImpl implements JobProcessingService {

    @Override
    public void process(Job job) {
        String fileName = job.getFileName();

        if (fileName == null || fileName.isBlank()) {
            throw new MissingRequiredFieldException("fileName is missing");
        }

        if (!fileName.endsWith(".csv")) {
            throw new UnsupportedFileTypeException("Only .csv files are supported");
        }

        if (fileName.contains("invalid")) {
            throw new InvalidPayloadException("Payload is invalid");
        }

        if (fileName.contains("rule")) {
            throw new BusinessRuleViolationException("Business rule violated");
        }

        if (fileName.contains("timeout")) {
            throw new JobTimeoutException("Processing timed out");
        }

        if (fileName.contains("network")) {
            throw new TemporaryNetworkException("Temporary network issue");
        }

        if (fileName.contains("service-down")) {
            throw new DependentServiceUnavailableException("Dependent service unavailable");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JobTimeoutException("Processing interrupted");
        }
    }
}
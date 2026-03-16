package com.juniverse.docprocessor.mapper;

import com.juniverse.docprocessor.dto.JobResponse;
import com.juniverse.docprocessor.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobResponse toResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setFileName(job.getFileName());
        response.setStatus(job.getStatus().name());
        response.setErrorMessage(job.getErrorMessage());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());
        return response;
    }
}
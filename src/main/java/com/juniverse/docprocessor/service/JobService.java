package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.dto.CreateJobRequest;
import com.juniverse.docprocessor.dto.JobResponse;

import java.util.List;

public interface JobService {
    JobResponse createJob(CreateJobRequest request);
    JobResponse getJobById(Long id);
    List<JobResponse> getAllJobs();
}

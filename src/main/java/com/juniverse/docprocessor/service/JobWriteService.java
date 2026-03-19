package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.entity.JobStatus;

public interface JobWriteService {
    public Job updateJob(Job job, JobStatus status, String errorMessage);
}

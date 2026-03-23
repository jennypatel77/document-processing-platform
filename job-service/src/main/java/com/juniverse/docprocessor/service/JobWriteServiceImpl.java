package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.annotation.EvictJobCache;
import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.entity.JobStatus;
import com.juniverse.docprocessor.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
public class JobWriteServiceImpl implements JobWriteService {

    private final JobRepository jobRepository;

    public JobWriteServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    @EvictJobCache
    public Job updateJob(Job job, JobStatus status, String errorMessage) {
        job.setStatus(status);
        job.setErrorMessage(errorMessage);
        return jobRepository.save(job);
    }
}
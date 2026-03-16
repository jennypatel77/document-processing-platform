package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.dto.CreateJobRequest;
import com.juniverse.docprocessor.dto.JobResponse;
import com.juniverse.docprocessor.entity.Job;
import com.juniverse.docprocessor.entity.JobStatus;
import com.juniverse.docprocessor.exception.JobNotFoundException;
import com.juniverse.docprocessor.mapper.JobMapper;
import org.springframework.stereotype.Service;
import com.juniverse.docprocessor.repository.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public JobResponse createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setFileName(request.getFileName());
        job.setStatus(JobStatus.PENDING);

        Job savedJob = jobRepository.save(job);
        return jobMapper.toResponse(savedJob);
    }

    @Override
    public JobResponse getJobById(Long id) {
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

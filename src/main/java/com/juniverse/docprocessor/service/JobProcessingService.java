package com.juniverse.docprocessor.service;

import com.juniverse.docprocessor.entity.Job;

public interface JobProcessingService {
    void process(Job job);
}
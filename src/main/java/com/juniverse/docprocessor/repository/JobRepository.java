package com.juniverse.docprocessor.repository;

import com.juniverse.docprocessor.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}

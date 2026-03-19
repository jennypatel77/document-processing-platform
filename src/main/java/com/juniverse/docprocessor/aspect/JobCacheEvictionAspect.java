package com.juniverse.docprocessor.aspect;

import com.juniverse.docprocessor.entity.Job;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JobCacheEvictionAspect {

    private final CacheManager cacheManager;

    public JobCacheEvictionAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @AfterReturning(
            pointcut = "@annotation(com.juniverse.docprocessor.annotation.EvictJobCache)",
            returning = "result"
    )
    public void evictCacheAfterSave(Object result) {

        if (result instanceof Job job && job.getId() != null) {

            System.out.println("AOP: Evicting cache for job " + job.getId());

            Cache cache = cacheManager.getCache("jobs");
            if (cache != null) {
                cache.evict(job.getId());
                System.out.println("AOP: Evicted cache for job id " + job.getId());
            }}
    }
}
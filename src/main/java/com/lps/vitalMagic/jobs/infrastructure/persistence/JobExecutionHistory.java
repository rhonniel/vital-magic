package com.lps.vitalMagic.jobs.infrastructure.persistence;

import com.lps.vitalMagic.jobs.infrastructure.persistence.entity.JobExecutionEntity;
import com.lps.vitalMagic.jobs.infrastructure.persistence.repository.JobExecutionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobExecutionHistory {
    private final JobExecutionJpaRepository repository;

    public JobExecutionHistory(JobExecutionJpaRepository repository) {
        this.repository = repository;
    }

    public Long start(String jobName) {
        return repository.save(new JobExecutionEntity(jobName, LocalDateTime.now())).getId();
    }

    public void complete(Long executionId) {
        repository.findById(executionId).orElseThrow().complete(LocalDateTime.now());
    }

    public void fail(Long executionId, Throwable failure) {
        String message = failure.getClass().getName()
                + (failure.getMessage() == null ? "" : ": " + failure.getMessage());
        repository.findById(executionId).orElseThrow().fail(LocalDateTime.now(),
                message.substring(0, Math.min(message.length(), 2000)));
    }
}

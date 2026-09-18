package com.lps.vitalMagic.jobs.infrastructure.persistence.repository;

import com.lps.vitalMagic.jobs.infrastructure.persistence.entity.JobExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExecutionJpaRepository extends JpaRepository<JobExecutionEntity, Long> {
}

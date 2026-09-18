package com.lps.vitalMagic.jobs.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_execution")
@Getter
public class JobExecutionEntity {
    public enum Status { RUNNING, COMPLETED, FAILED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false, length = 150)
    private String jobName;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected JobExecutionEntity() {
    }

    public JobExecutionEntity(String jobName, LocalDateTime startedAt) {
        this.jobName = jobName;
        this.startedAt = startedAt;
        this.createdAt = startedAt;
        this.status = Status.RUNNING;
    }

    public void complete(LocalDateTime completedAt) {
        this.status = Status.COMPLETED;
        this.completedAt = completedAt;
        this.errorMessage = null;
    }

    public void fail(LocalDateTime completedAt, String errorMessage) {
        this.status = Status.FAILED;
        this.completedAt = completedAt;
        this.errorMessage = errorMessage;
    }
}

CREATE TABLE job_execution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_name VARCHAR(150) NOT NULL,
    started_at DATETIME(6) NOT NULL,
    completed_at DATETIME(6) NULL,
    status VARCHAR(20) NOT NULL,
    error_message VARCHAR(2000) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

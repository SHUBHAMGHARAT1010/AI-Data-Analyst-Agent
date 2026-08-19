CREATE TABLE dataset_processing_job
(
    id BIGSERIAL PRIMARY KEY,

    job_id VARCHAR(50) NOT NULL UNIQUE,

    dataset_id BIGINT NOT NULL UNIQUE,

    status VARCHAR(30) NOT NULL,

    started_at TIMESTAMP,

    completed_at TIMESTAMP,

    error_message TEXT,

    retry_count INTEGER DEFAULT 0,

    CONSTRAINT fk_processing_dataset
        FOREIGN KEY (dataset_id)
            REFERENCES datasets(id)
            ON DELETE CASCADE
);
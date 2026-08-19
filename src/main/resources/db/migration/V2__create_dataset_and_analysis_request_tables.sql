CREATE TABLE datasets (
                          id BIGSERIAL PRIMARY KEY,

                          dataset_id VARCHAR(50) NOT NULL UNIQUE,

                          original_file_name VARCHAR(500) NOT NULL,

                          file_type VARCHAR(20) NOT NULL,

                          file_path VARCHAR(1000) NOT NULL,

                          domain VARCHAR(100),

                          status VARCHAR(50) NOT NULL,

                          retry_count INTEGER NOT NULL DEFAULT 0,

                          max_retry_count INTEGER NOT NULL DEFAULT 3,

                          error_message TEXT,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE analysis_requests (
                                   id BIGSERIAL PRIMARY KEY,

                                   analysis_request_id VARCHAR(50) NOT NULL UNIQUE,

                                   dataset_id BIGINT NOT NULL,

                                   user_instruction TEXT NOT NULL,

                                   analysis_type VARCHAR(100),

                                   status VARCHAR(50) NOT NULL,

                                   retry_count INTEGER NOT NULL DEFAULT 0,

                                   max_retry_count INTEGER NOT NULL DEFAULT 3,

                                   error_message TEXT,

                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_analysis_request_dataset
                                       FOREIGN KEY (dataset_id)
                                           REFERENCES datasets(id)
                                           ON DELETE CASCADE
);


CREATE INDEX idx_datasets_dataset_id
    ON datasets(dataset_id);


CREATE INDEX idx_datasets_status
    ON datasets(status);


CREATE INDEX idx_analysis_requests_analysis_request_id
    ON analysis_requests(analysis_request_id);


CREATE INDEX idx_analysis_requests_dataset_id
    ON analysis_requests(dataset_id);


CREATE INDEX idx_analysis_requests_status
    ON analysis_requests(status);
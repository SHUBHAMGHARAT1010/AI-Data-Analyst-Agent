CREATE TABLE application_metadata(
    id BIGSERIAL PRIMARY KEY,
    application_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


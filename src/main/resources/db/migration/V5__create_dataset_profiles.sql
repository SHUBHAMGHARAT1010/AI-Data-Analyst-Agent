CREATE TABLE dataset_profiles(
    id BIGSERIAL PRIMARY KEY ,

    dataset_id BIGINT NOT NULL  UNIQUE ,

    total_rows BIGINT NOT NULL ,
    total_columns INTEGER NOT NULL ,
    duplicate_rows BIGINT NOT NULL ,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_dataset_profiles_dataset
                             FOREIGN KEY (dataset_id)
                             REFERENCES datasets(id)
);
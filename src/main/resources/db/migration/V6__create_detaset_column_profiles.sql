CREATE TABLE dataset_column_profiles(

    id BIGSERIAL PRIMARY KEY ,

    dataset_profile_id BIGINT NOT NULL ,
    column_name VARCHAR(255) NOT NULL ,
    data_type VARCHAR(50) NOT NULL ,
    total_values BIGINT NOT NULL ,
    null_values BIGINT NOT NULL ,
    empty_values BIGINT NOT NULL ,

    CONSTRAINT fk_dataset_column_profile_dataset_profile
                                    FOREIGN KEY (dataset_profile_id)
                                    REFERENCES dataset_profiles(id)
                                    ON DELETE CASCADE
);


CREATE INDEX idx_column_profile_dataset_profile_id
ON dataset_column_profiles(dataset_profile_id);
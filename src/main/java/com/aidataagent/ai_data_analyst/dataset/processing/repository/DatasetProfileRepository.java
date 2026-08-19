package com.aidataagent.ai_data_analyst.dataset.processing.repository;

import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatasetProfileRepository extends JpaRepository<DatasetProfileEntity,Long> {

   Optional<DatasetProfileEntity> findByDataSet_DatasetId(String datasetId);
}

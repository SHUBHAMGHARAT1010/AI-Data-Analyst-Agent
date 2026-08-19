package com.aidataagent.ai_data_analyst.dataset.processing.repository;

import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetColumnProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetColumnProfileRepository extends JpaRepository<DatasetColumnProfileEntity, Long> {

    List<DatasetColumnProfileEntity> findByDatasetProfileId(Long datasetProfileId);

}

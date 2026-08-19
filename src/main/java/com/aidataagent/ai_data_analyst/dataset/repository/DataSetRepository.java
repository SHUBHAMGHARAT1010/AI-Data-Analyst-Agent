package com.aidataagent.ai_data_analyst.dataset.repository;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.model.DatasetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DataSetRepository extends JpaRepository<DataSet, Long> {

    Optional<DataSet> findByDatasetId(String datasetId);

    List<DataSet> findByStatus(DatasetStatus status);
}

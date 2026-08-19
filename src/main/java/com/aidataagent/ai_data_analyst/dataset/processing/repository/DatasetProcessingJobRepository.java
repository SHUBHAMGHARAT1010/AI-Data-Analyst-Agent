package com.aidataagent.ai_data_analyst.dataset.processing.repository;

import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProcessingJob;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ProcessingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DatasetProcessingJobRepository extends JpaRepository<DatasetProcessingJob, Long> {

    Optional<DatasetProcessingJob> findByJobId(String jobId);

    @EntityGraph(attributePaths = "dataSet")
  List<DatasetProcessingJob> findByStatus(ProcessingStatus status);
}
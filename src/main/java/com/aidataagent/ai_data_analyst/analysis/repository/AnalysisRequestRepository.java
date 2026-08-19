package com.aidataagent.ai_data_analyst.analysis.repository;

import com.aidataagent.ai_data_analyst.analysis.entity.AnalysisRequest;
import com.aidataagent.ai_data_analyst.analysis.model.AnalysisStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnalysisRequestRepository extends JpaRepository<AnalysisRequest,Long> {

    Optional<AnalysisRequest> findByAnalysisRequestId(String analysisRequestId);

    List<AnalysisRequest> findByDataset(Long datasetId);

    List<AnalysisRequest> findByStatus(AnalysisStatus status);
}

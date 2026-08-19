package com.aidataagent.ai_data_analyst.dataset.dto;

import com.aidataagent.ai_data_analyst.analysis.model.AnalysisStatus;
import com.aidataagent.ai_data_analyst.dataset.model.DatasetStatus;
import lombok.Builder;

@Builder
public record DatasetUploadResponse(

        String datasetId,
        DatasetStatus datasetStatus,
        String analysisRequestId,
        AnalysisStatus analysisStatus,
        String message
) {
}

package com.aidataagent.ai_data_analyst.dataset.processing.model;

public record ColumnQualityResult(String columnName,
                                  long totalValues,
                                  long nullValues,
                                  long emptyValues,
                                  double missingPercentage,
                                  QualityStatus status) {
}

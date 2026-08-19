package com.aidataagent.ai_data_analyst.dataset.model;

public enum DatasetStatus {

    UPLOADED,

    VALIDATING,

    VALIDATED,

    PROCESSING,

    PROFILING,

    ANALYZING,

    COMPLETED,

    FAILED,

    retry_pending
}

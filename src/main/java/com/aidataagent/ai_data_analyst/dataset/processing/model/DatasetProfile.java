package com.aidataagent.ai_data_analyst.dataset.processing.model;

import java.util.List;

public record DatasetProfile(long totalRows, int totalColumns, List<ColumnProfile> columns,
                          long duplicateRows  ) {
}

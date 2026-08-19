package com.aidataagent.ai_data_analyst.dataset.processing.model;

public record ColumnProfile (String columnName,
                             long totalValues,
                             long nullValues,
                             long emptyValues,
                             DataType dataType){
}

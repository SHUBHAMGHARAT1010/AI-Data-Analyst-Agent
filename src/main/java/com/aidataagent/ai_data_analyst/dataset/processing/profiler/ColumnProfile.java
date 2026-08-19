package com.aidataagent.ai_data_analyst.dataset.processing.profiler;

import lombok.Getter;

@Getter
public class ColumnProfile {

    private final String columnName;
    private final int columnIndex;
    private final long totalValues;
    private final long nonNullCount;
    private final long nullCount;
    private final String detectedDataType;

    public ColumnProfile(String columnName,int columnIndex,long totalValues, long nonNullCount,long nullCount,String detectedDataType){
        this.columnIndex=columnIndex;
        this.columnName=columnName;
        this.totalValues=totalValues;
        this.nonNullCount=nonNullCount;
        this.nullCount=nullCount;
        this.detectedDataType=detectedDataType;
    }


}

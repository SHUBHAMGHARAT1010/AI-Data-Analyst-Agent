package com.aidataagent.ai_data_analyst.dataset.processing.profiler;

import lombok.Getter;

import java.util.List;

@Getter
public class DatasetProfile {

    private final int rowCount;
    private final int columnCount;
    private final long duplicateRowCount;
    private final List<ColumnProfile> columns;

    public DatasetProfile(
            int rowCount,int columnCount,
            long duplicateRowCount,
            List<ColumnProfile> columns
    ){
        this.rowCount=rowCount;
        this.columnCount=columnCount;
        this.duplicateRowCount=duplicateRowCount;
        this.columns=List.copyOf(columns);
    }


}

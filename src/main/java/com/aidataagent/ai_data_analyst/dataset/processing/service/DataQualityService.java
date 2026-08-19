package com.aidataagent.ai_data_analyst.dataset.processing.service;

import com.aidataagent.ai_data_analyst.dataset.processing.model.ColumnProfile;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ColumnQualityResult;
import com.aidataagent.ai_data_analyst.dataset.processing.model.QualityStatus;
import org.springframework.stereotype.Service;

@Service
public class DataQualityService {

    public ColumnQualityResult assess(ColumnProfile columnProfile){

        long totalValues=columnProfile.totalValues();
        long nullValues=columnProfile.nullValues();
        long emptyValues= columnProfile.emptyValues();

        long missingValues= nullValues+emptyValues;

        double missingPercentage=calculateMissingPercentage(totalValues,missingValues);

        QualityStatus status= determineStatus(missingPercentage);

        return new ColumnQualityResult(
                columnProfile.columnName(),
                totalValues,
                nullValues,
                emptyValues,
                missingPercentage,
                status
        );
    }

    private double calculateMissingPercentage(long totalValues,
                                              long missingValues){
        if(totalValues==0){
            return 0.0;
        }
        return  ((double) missingValues/totalValues)*100;
    }

    private QualityStatus determineStatus(double missingPercentage){
        if(missingPercentage<=1.0){
            return QualityStatus.GOOD;
        }

        if(missingPercentage<=10.0){
            return QualityStatus.WARNING;
        }

        return QualityStatus.CRITICAL;

    }
}


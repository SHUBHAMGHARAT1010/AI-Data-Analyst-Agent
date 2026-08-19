package com.aidataagent.ai_data_analyst.dataset.processing.mapper;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProfileEntity;
import com.aidataagent.ai_data_analyst.dataset.processing.model.DatasetProfile;
import org.springframework.stereotype.Component;

@Component
public class DatasetProfileMapper {


    public DatasetProfileEntity toEntity(
            DatasetProfile profile,
            DataSet dataSet
    ){
        return new DatasetProfileEntity(
                dataSet,profile.totalRows(),profile.totalColumns(),
                profile.duplicateRows()
        );
    }
}

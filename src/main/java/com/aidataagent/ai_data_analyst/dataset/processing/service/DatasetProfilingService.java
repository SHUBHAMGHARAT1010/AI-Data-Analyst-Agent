package com.aidataagent.ai_data_analyst.dataset.processing.service;

import com.aidataagent.ai_data_analyst.dataset.processing.model.ColumnProfile;
import com.aidataagent.ai_data_analyst.dataset.processing.model.DataType;
import com.aidataagent.ai_data_analyst.dataset.processing.model.DatasetProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DatasetProfilingService {

    public DatasetProfile profile(List<List<String>> rows){

        if(rows==null || rows.isEmpty()){
         return new DatasetProfile(
                 0,
                 0,
                 List.of(),
                 0

         )   ;
        }

        List<String> header= rows
                .get(0);

        int totalColumns= header.size();
        long totalRows= Math.max(0,rows.size()-1);
        List<ColumnProfile> columnProfiles= new ArrayList<>();


        for(int columnIndex=0; columnIndex<totalColumns;columnIndex++){
            String columnName=header.get(columnIndex);

            long totalValues=0;
            long nullValues=0;
            long emptyValues=0;
            DataType dataType= detectedDataType(rows,columnIndex);

            for(int rowIndex=1; rowIndex<rows.size();rowIndex++){

                List<String> row= rows.get(rowIndex);
                String value= columnIndex<row.size()? row.get(columnIndex):null;
                totalValues++;

                if(value==null){ nullValues++;}
                else if (value.isBlank()) {
                    emptyValues++;

                }
            }

            columnProfiles.add(new ColumnProfile(columnName,
                    totalValues,
                    nullValues,
                    emptyValues,
                    dataType
                    ));
        }

        long duplicateRows= countDuplicateRows(rows);

        return new DatasetProfile(
                totalRows,totalColumns,columnProfiles,duplicateRows
        );
    }

    private long countDuplicateRows(List<List<String>> rows){
        if(rows.size()<=2){
            return 0;
        }

        Set<List<String>> uniqueRows= new HashSet<>();

        long duplicates=0;

        for (int i=1;i<rows.size();i++){

            if(!uniqueRows.add(rows.get(i))){
                duplicates++;
            }
        }
        return duplicates;
    }

    private DataType detectedDataType(List<List<String>> rows, int columnIndex) {

        boolean hasInteger = false;
        boolean hasDecimal = false;
        boolean hasDate = false;
        boolean hasBoolean = false;
        boolean hasText = false;

        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {

            List<String> row = rows.get(rowIndex);

            if (columnIndex > row.size()) {
                continue;
            }

            String value = row.get(columnIndex);

            if (value == null || value.isBlank()) {
                continue;
            }

            String trimmedValue = value.trim();

            if (isBoolean(trimmedValue))
                hasBoolean = true;

            else if (isInteger(trimmedValue))
                hasInteger = true;
            else if (isDecimal(trimmedValue))
                hasDecimal = true;
            else if (isDate(trimmedValue)) hasDate = true;
            else hasText = true;

        }

        if (hasText) return DataType.TEXT;
        if (hasDate) return DataType.DATE;
        if (hasDecimal) return DataType.DECIMAL;
        if (hasInteger) return DataType.INTEGER;
        if (hasBoolean) return DataType.BOOLEAN;
        return DataType.UNKNOWN;
    }

    private boolean isInteger(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDecimal(String value) {
        try {
            Double.parseDouble(value);
            return value.contains(".");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")
                || value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("no");
    }


    private boolean isDate(String value) {
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

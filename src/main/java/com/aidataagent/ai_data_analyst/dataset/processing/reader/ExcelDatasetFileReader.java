package com.aidataagent.ai_data_analyst.dataset.processing.reader;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelDatasetFileReader implements DatasetFileReader {


    @Override
    public List<List<String>> read(String filePath) {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Dataset file not found: " + filePath);
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Dataset path is not a valid file: " + filePath);

        }

        try(InputStream stream = Files.newInputStream(path);

            Workbook workbook= WorkbookFactory.create(stream);){

            if(workbook.getNumberOfSheets()==0){
                throw new IllegalArgumentException("Excel file does not contain any sheet");
            }

            Sheet sheet= workbook.getSheetAt(0);
            List<List<String>> rows= new ArrayList<>();
            for(Row row:sheet){
                List<String> rowData= new ArrayList<>();

                for (Cell cell: row){
                    rowData.add(getCellValue(cell));
                }
                rows.add(rowData);
            }
            return rows;

        }catch (IOException ex){
            throw new IllegalStateException("Failed to read Excel File: "+filePath, ex);
        }

    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toString();
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();

            case BLANK -> "";
            case ERROR -> String.valueOf(cell.getErrorCellValue());
            default -> "";
        };
    }

    private static class DataFormatterHolder {
        private static final DataFormatter FORMATTER = new DataFormatter();
    }
}

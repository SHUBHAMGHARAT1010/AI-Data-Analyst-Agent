package com.aidataagent.ai_data_analyst.dataset.processing.profiler;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class DatasetProfiler {

    public DatasetProfile profile(List<List<String>> rows) {

        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("Can't profile an empty dataset");
        }

        List<String> headers = rows.get(0);

        if (headers == null || headers.isEmpty()) {
            throw new IllegalArgumentException(
                    "Dataset does not contain headers"
            );
        }

        int columnCount = headers.size();
        int rowCount = Math.max(rows.size() - 1, 0);

        List<ColumnProfile> columnProfiles = buildColumnProfile(rows
                , headers, columnCount);

        long duplicateRowCount = countDuplicateRows(rows);

        return new DatasetProfile(rowCount, columnCount
                , duplicateRowCount,
                columnProfiles);


    }

    private long countDuplicateRows(List<List<String>> rows) {
        if (rows.size() <= 2) {
            return 0;
        }

        Set<List<String>> uniqueRows = new HashSet<>();
        long duplicateCount = 0;
        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);

            if (!uniqueRows.add(row)) {
                duplicateCount++;
            }
        }

        return duplicateCount;
    }

    private List<ColumnProfile> buildColumnProfile(
            List<List<String>> rows,
            List<String> headers,
            int columnCount
    ) {
        return IntStream.range(0, columnCount)
                .mapToObj(columnIndex -> {
                    String columnName = normalizeColumnName(
                            headers.get(columnIndex),
                            columnIndex
                    );

                    long totalValues = 0;
                    long nonNullCount = 0;
                    long nullCount = 0;

                    for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
                        List<String> row = rows.get(rowIndex);
                        totalValues++;

                        String value = getCellValue(row, columnIndex);

                        if (value == null || value.isBlank()) {
                            nullCount++;
                        } else {
                            nonNullCount++;
                        }
                    }

                    String detectedDataType = detectedDataType(rows, columnIndex);

                    return new ColumnProfile(
                            columnName, columnIndex, totalValues, nonNullCount, nullCount, detectedDataType
                    );

                }).toList();
    }

    private String detectedDataType(List<List<String>> rows, int columnIndex) {

        boolean hasNumber = false;
        boolean hasBoolean = false;
        boolean hasDate = false;
        boolean hasText = false;

        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            String value = getCellValue(
                    rows.get(rowIndex),
                    columnIndex
            );

            if (value == null || value.isBlank()) {
                continue;
            }
            String trimmedValue = value.trim();

            if (isBoolean(trimmedValue)) {
                hasBoolean = true;
            } else if (isNumber(trimmedValue)) {

                hasNumber = true;

            } else if (isDate(trimmedValue)) {
                hasDate = true;
            } else {
                hasText = true;
            }
        }

        if (hasText) {
            return "TEXT";
        }

        if (hasDate) {
            return "DATE";
        }

        if (hasBoolean) return "BOOLEAN";

        return "UNKNOW";

    }

    private boolean isNumber(String value) {

        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isBoolean(String values) {
        return "true".equalsIgnoreCase(values) || "false".equalsIgnoreCase(values);
    }

    private boolean isDate(String value) {

        return value.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private String getCellValue(
            List<String> row,
            int columnIndex
    ) {
        if (row == null || columnIndex >= row.size()) {
            return null;
        }

        return row.get(columnIndex);
    }

    private String normalizeColumnName(String columnName, int columnIndex) {
        if (columnName == null || columnName.isBlank()) {
            return "column_" + (columnIndex + 1);
        }
        return columnName.trim();
    }

}

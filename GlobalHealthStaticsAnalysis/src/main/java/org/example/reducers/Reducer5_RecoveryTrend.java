package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Reducer5_RecoveryTrend {
    public static void run(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Reducer 5: Recovery Rate Trend by Disease");

        Map<String, Map<Integer, List<Double>>> trend = new TreeMap<>();

        try (Reader reader = new FileReader(inputFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                int year = Integer.parseInt(record.get("Year"));
                double recovery = Double.parseDouble(record.get("Recovery Rate (%)"));

                trend.computeIfAbsent(disease, k -> new TreeMap<>())
                        .computeIfAbsent(year, k -> new ArrayList<>()).add(recovery);
            }
        }

        // Print to terminal
        System.out.printf("%-30s %-10s %-25s%n", "Disease Name", "Year", "Avg Recovery Rate (%)");
        System.out.println("---------------------------------------------------------------");

        // Create Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Recovery Trend");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Disease Name");
        headerRow.createCell(1).setCellValue("Year");
        headerRow.createCell(2).setCellValue("Avg Recovery Rate (%)");

        int rowNum = 1;
        for (Map.Entry<String, Map<Integer, List<Double>>> diseaseEntry : trend.entrySet()) {
            String disease = diseaseEntry.getKey();
            for (Map.Entry<Integer, List<Double>> yearEntry : diseaseEntry.getValue().entrySet()) {
                int year = yearEntry.getKey();
                double avg = yearEntry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);

                // Print to terminal
                System.out.printf("%-30s %-10d %-25.2f%n", disease, year, avg);

                // Write to Excel row
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(disease);
                row.createCell(1).setCellValue(year);
                row.createCell(2).setCellValue(avg);
            }
        }

        // Autosize columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write Excel file
        try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("Reducer 5 output written to Excel file: " + outputFilePath);
    }
}
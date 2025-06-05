package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Reducer1_MortalityByCategory {
    public static void run(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Reducer 1: Calculating average mortality rate by disease category...");

        Map<String, List<Double>> map = new HashMap<>();

        // Read CSV input
        try (Reader reader = new FileReader(inputFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String category = record.get("Disease Category");
                double mortality = Double.parseDouble(record.get("Mortality Rate (%)"));
                map.computeIfAbsent(category, k -> new ArrayList<>()).add(mortality);
            }
        }

        // Create Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Average Mortality");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Disease Category");
        header.createCell(1).setCellValue("Average Mortality Rate (%)");

        // Write data rows and print on terminal
        int rowNum = 1;
        System.out.printf("%-30s %-25s%n", "Disease Category", "Average Mortality Rate (%)");
        System.out.println("---------------------------------------------------------------");

        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);

            // Print to terminal
            System.out.printf("%-30s %-25.2f%n", entry.getKey(), avg);

            // Write to Excel
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(avg);
        }

        // Autosize columns for better readability
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Write workbook to output file
        try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
            workbook.write(fileOut);
        }
        workbook.close();

        System.out.println("Reducer 1 output written to Excel file: " + outputFilePath);
    }
}
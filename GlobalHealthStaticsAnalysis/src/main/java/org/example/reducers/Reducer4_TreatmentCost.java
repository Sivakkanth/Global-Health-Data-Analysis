package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Reducer4_TreatmentCost {
    public static void run(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Reducer 4: Average Treatment Cost by Disease");

        Map<String, List<Double>> costs = new HashMap<>();

        try (Reader reader = new FileReader(inputFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                double cost = Double.parseDouble(record.get("Average Treatment Cost (USD)"));
                costs.computeIfAbsent(disease, k -> new ArrayList<>()).add(cost);
            }
        }

        // Print to terminal
        System.out.printf("%-30s %-30s%n", "Disease Name", "Avg Treatment Cost (USD)");
        System.out.println("--------------------------------------------------------------");

        // Create Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Avg Treatment Cost");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Disease Name");
        headerRow.createCell(1).setCellValue("Avg Treatment Cost (USD)");

        // Data rows
        int rowNum = 1;
        for (Map.Entry<String, List<Double>> entry : costs.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);

            // Print to terminal
            System.out.printf("%-30s %-30.2f%n", entry.getKey(), avg);

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(avg);
        }

        // Autosize columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Save Excel file
        try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("Reducer 4 output written to Excel file: " + outputFilePath);
    }
}
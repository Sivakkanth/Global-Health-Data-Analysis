package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Reducer2_TopDiseasesByPopulation {
    public static void run(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Reducer 2: Top 5 Diseases by Total Population Affected");

        Map<String, Long> totals = new HashMap<>();

        // Read input CSV file
        try (Reader reader = new FileReader(inputFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                long affected = Long.parseLong(record.get("Population Affected"));
                totals.put(disease, totals.getOrDefault(disease, 0L) + affected);
            }
        }

        // Sort entries by population affected descending, limit to top 5
        List<Map.Entry<String, Long>> toSort = new ArrayList<>();
        for (Map.Entry<String, Long> stringLongEntry : totals.entrySet()) {
            toSort.add(stringLongEntry);
        }
        toSort.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        List<Map.Entry<String, Long>> top5 = new ArrayList<>();
        long limit = 5;
        for (Map.Entry<String, Long> stringLongEntry : toSort) {
            if (limit-- == 0) break;
            top5.add(stringLongEntry);
        }

        // Print output to terminal
        System.out.printf("%-30s %-25s%n", "Disease Name", "Total Population Affected");
        System.out.println("-------------------------------------------------------------");
        for (Map.Entry<String, Long> entry : top5) {
            System.out.printf("%-30s %-25d%n", entry.getKey(), entry.getValue());
        }

        // Create Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Top 5 Diseases");

        // Create header row in Excel
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Disease Name");
        header.createCell(1).setCellValue("Total Population Affected");

        // Write data rows to Excel
        int rowNum = 1;
        for (Map.Entry<String, Long> entry : top5) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }

        // Auto-size columns for better appearance
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Write Excel file to output path
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("Reducer 2 output written to Excel file: " + outputFilePath);
    }
}

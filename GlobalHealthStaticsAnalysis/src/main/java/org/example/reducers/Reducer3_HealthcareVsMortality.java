package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Reducer3_HealthcareVsMortality {
    public static void run(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Reducer 3: Average Healthcare Access vs Mortality Rate by Country");

        Map<String, List<Double[]>> countryStats = new HashMap<>();

        try (Reader reader = new FileReader(inputFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String country = record.get("Country");
                double access = Double.parseDouble(record.get("Healthcare Access (%)"));
                double mortality = Double.parseDouble(record.get("Mortality Rate (%)"));
                countryStats.computeIfAbsent(country, k -> new ArrayList<>()).add(new Double[]{access, mortality});
            }
        }

        // Print header on terminal
        System.out.printf("%-25s %-30s %-30s%n", "Country", "Avg Healthcare Access (%)", "Avg Mortality Rate (%)");
        System.out.println("-------------------------------------------------------------------------------");

        // Prepare data for Excel and terminal output
        List<String[]> outputRows = new ArrayList<>();
        outputRows.add(new String[] {"Country", "Avg Healthcare Access (%)", "Avg Mortality Rate (%)"});

        for (Map.Entry<String, List<Double[]>> entry : countryStats.entrySet()) {
            String country = entry.getKey();
            List<Double[]> data = entry.getValue();
            double avgAccess = data.stream().mapToDouble(x -> x[0]).average().orElse(0);
            double avgMortality = data.stream().mapToDouble(x -> x[1]).average().orElse(0);

            // Print to terminal
            System.out.printf("%-25s %-30.2f %-30.2f%n", country, avgAccess, avgMortality);

            // Add row for Excel
            outputRows.add(new String[] {country, String.format("%.2f", avgAccess), String.format("%.2f", avgMortality)});
        }

        // Create Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Healthcare vs Mortality");

        // Write to Excel sheet
        for (int i = 0; i < outputRows.size(); i++) {
            Row row = sheet.createRow(i);
            String[] rowData = outputRows.get(i);
            for (int j = 0; j < rowData.length; j++) {
                row.createCell(j).setCellValue(rowData[j]);
            }
        }

        // Autosize columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write Excel file to output path
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("Reducer 3 output written to Excel file: " + outputFilePath);
    }
}
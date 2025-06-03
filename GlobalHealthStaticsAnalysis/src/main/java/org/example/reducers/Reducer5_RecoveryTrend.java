package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Reducer5_RecoveryTrend {
    public static void run() throws IOException {
        System.out.println("Reducer 5: Writing output to output/reducer5_output.csv");

        Map<String, Map<Integer, List<Double>>> trend = new TreeMap<>();
        try (InputStream input = Reducer5_RecoveryTrend.class.getClassLoader().getResourceAsStream("input/GlobalHealthStatics.csv");
             Reader reader = new InputStreamReader(input)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                int year = Integer.parseInt(record.get("Year"));
                double recovery = Double.parseDouble(record.get("Recovery Rate (%)"));
                trend.computeIfAbsent(disease, k -> new TreeMap<>())
                        .computeIfAbsent(year, k -> new ArrayList<>()).add(recovery);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output/reducer5_output.csv"));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Disease Name", "Year", "Avg Recovery Rate (%)"))) {
            for (Map.Entry<String, Map<Integer, List<Double>>> diseaseEntry : trend.entrySet()) {
                String disease = diseaseEntry.getKey();
                for (Map.Entry<Integer, List<Double>> yearEntry : diseaseEntry.getValue().entrySet()) {
                    double avg = yearEntry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    printer.printRecord(disease, yearEntry.getKey(), String.format("%.2f", avg));
                }
            }
        }
    }
}
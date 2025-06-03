package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reducer3_HealthcareVsMortality {
    public static void run() throws IOException {
        System.out.println("Reducer 3: Writing output to output/reducer3_output.csv");

        Map<String, List<Double[]>> countryStats = new HashMap<>();
        try (InputStream input = Reducer3_HealthcareVsMortality.class.getClassLoader().getResourceAsStream("input/GlobalHealthStatics.csv");
             Reader reader = new InputStreamReader(input)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String country = record.get("Country");
                double access = Double.parseDouble(record.get("Healthcare Access (%)"));
                double mortality = Double.parseDouble(record.get("Mortality Rate (%)"));
                countryStats.computeIfAbsent(country, k -> new ArrayList<>()).add(new Double[]{access, mortality});
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output/reducer3_output.csv"));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Country", "Avg Healthcare Access (%)", "Avg Mortality Rate (%)"))) {
            for (Map.Entry<String, List<Double[]>> entry : countryStats.entrySet()) {
                String country = entry.getKey();
                List<Double[]> data = entry.getValue();
                double avgAccess = data.stream().mapToDouble(x -> x[0]).average().orElse(0);
                double avgMortality = data.stream().mapToDouble(x -> x[1]).average().orElse(0);
                printer.printRecord(country, String.format("%.2f", avgAccess), String.format("%.2f", avgMortality));
            }
        }
    }
}
package org.example.reducers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.*;

public class Reducer1_MortalityByCategory {
    public static void run() throws IOException {
        System.out.println("Reducer 1: Average Mortality Rate by Disease Category");

        Map<String, List<Double>> map = new HashMap<>();
        try (InputStream input = Reducer1_MortalityByCategory.class.getClassLoader().getResourceAsStream("input/GlobalHealthStatics.csv");
             Reader reader = new InputStreamReader(input)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String category = record.get("Disease Category");
                double mortality = Double.parseDouble(record.get("Mortality Rate (%)"));
                map.computeIfAbsent(category, k -> new ArrayList<>()).add(mortality);
            }
        }

        // Write to CSV
        File outFile = new File("src/main/resources/output/reducer1_output.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Disease Category", "Average Mortality Rate (%)"))) {
            for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
                double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                printer.printRecord(entry.getKey(), String.format("%.2f", avg));
            }
        }
    }
}

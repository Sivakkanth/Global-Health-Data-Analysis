package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reducer4_TreatmentCost {
    public static void run() throws IOException {
        System.out.println("Reducer 4: Writing output to output/reducer4_output.csv");

        Map<String, List<Double>> costs = new HashMap<>();
        try (InputStream input = Reducer4_TreatmentCost.class.getClassLoader().getResourceAsStream("input/GlobalHealthStatics.csv");
             Reader reader = new InputStreamReader(input)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                double cost = Double.parseDouble(record.get("Average Treatment Cost (USD)"));
                costs.computeIfAbsent(disease, k -> new ArrayList<>()).add(cost);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output/reducer4_output.csv"));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Disease Name", "Avg Treatment Cost (USD)"))) {
            for (Map.Entry<String, List<Double>> entry : costs.entrySet()) {
                double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                printer.printRecord(entry.getKey(), String.format("%.2f", avg));
            }
        }
    }
}
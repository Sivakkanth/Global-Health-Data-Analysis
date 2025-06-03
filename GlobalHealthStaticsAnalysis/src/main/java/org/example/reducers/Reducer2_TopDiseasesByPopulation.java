package org.example.reducers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Reducer2_TopDiseasesByPopulation {
    public static void run() throws IOException {
        System.out.println("Reducer 2: Writing output to output/reducer2_output.csv");

        Map<String, Long> totals = new HashMap<>();
        try (InputStream input = Reducer2_TopDiseasesByPopulation.class.getClassLoader().getResourceAsStream("input/GlobalHealthStatics.csv");
             Reader reader = new InputStreamReader(input)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String disease = record.get("Disease Name");
                long affected = Long.parseLong(record.get("Population Affected"));
                totals.put(disease, totals.getOrDefault(disease, 0L) + affected);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/output/reducer2_output.csv"));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Disease Name", "Total Population Affected"))) {
            totals.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                    .limit(5)
                    .forEach(e -> {
                        try {
                            printer.printRecord(e.getKey(), e.getValue());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
        }
    }
}
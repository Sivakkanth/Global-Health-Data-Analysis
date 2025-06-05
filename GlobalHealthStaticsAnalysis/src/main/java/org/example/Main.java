package org.example;

import org.example.reducers.*;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception{
        if (args.length < 2) {
            System.err.println("Usage: java -jar <jar-file> <input-file-path> <output-directory>");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];

        if (!outputDir.endsWith(File.separator)) {
            outputDir += File.separator;
        }

        Reducer1_MortalityByCategory.run(inputPath, outputDir + "reducer1_output.xlsx");
        Reducer2_TopDiseasesByPopulation.run(inputPath, outputDir + "reducer2_output.xlsx");
        Reducer3_HealthcareVsMortality.run(inputPath, outputDir + "reducer3_output.xlsx");
        Reducer4_TreatmentCost.run(inputPath, outputDir + "reducer4_output.xlsx");
        Reducer5_RecoveryTrend.run(inputPath, outputDir + "reducer5_output.xlsx");
    }
}
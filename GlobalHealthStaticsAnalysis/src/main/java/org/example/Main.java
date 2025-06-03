package org.example;

import org.example.reducers.*;

public class Main {
    public static void main(String[] args) throws Exception{
        Reducer1_MortalityByCategory.run();
        Reducer2_TopDiseasesByPopulation.run();
        Reducer3_HealthcareVsMortality.run();
        Reducer4_TreatmentCost.run();
        Reducer5_RecoveryTrend.run();
    }
}
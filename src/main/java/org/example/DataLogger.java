package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class DataLogger {
    private String fileName;
    private String header = "krok;ilosc_miast;ilosc_reaktorow;kierunek_wiatru;laczna_populacja;laczne_zapotrzebowanie;aktywne_reaktory";

    public DataLogger() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        this.fileName = "docs/raport_" + timestamp + ".csv";
        initializeFile();
    }

    private void initializeFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(header + "\n");
            } catch (IOException e) {
                System.err.println("Błąd podczas tworzenia pliku raportu: " + e.getMessage());
            }
        }
    }

    public void saveData(int step, int citiesCount, int reactorsCount, String windDirection,
                         int totalPopulation, float totalEnergyDemand, int activeReactors) {
        String dataLine = String.format("%d;%d;%d;%s;%d;%.2f;%d",
                step, citiesCount, reactorsCount, windDirection,
                totalPopulation, totalEnergyDemand, activeReactors);

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(dataLine + "\n");
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania danych: " + e.getMessage());
        }
    }
}
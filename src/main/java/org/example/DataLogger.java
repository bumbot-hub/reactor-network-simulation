package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa odpowiedzialna za logowanie danych z przebiegu symulacji do pliku CSV.
 * Tworzy plik raportu z unikalną nazwą opartą na dacie i czasie, a następnie
 * zapisuje w nim dane w formacie CSV.
 */
class DataLogger {
    /** Nazwa pliku raportu, generowana na podstawie aktualnej daty i czasu. */
    private String fileName;
    /** Nagłówek pliku CSV, definiujący kolumny w raporcie. */
    private String header = "krok;ilosc_miast;ilosc_reaktorow;kierunek_wiatru;laczna_populacja;laczne_zapotrzebowanie;aktywne_reaktory";

    /**
     * Konstruktor klasy DataLogger.
     * Inicjalizuje nazwę pliku raportu, używając aktualnego znacznika czasu,
     * i wywołuje metodę tworzącą plik z nagłówkiem.
     */
    public DataLogger() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        this.fileName = "docs/raport_" + timestamp + ".csv";
        initializeFile();
    }

    /**
     * Inicjalizuje plik raportu.
     * Tworzy plik CSV i zapisuje w nim nagłówek, jeśli plik jeszcze nie istnieje.
     * W przypadku błędu, komunikat jest wyświetlany na standardowym wyjściu błędów.
     */
    private void initializeFile() {
        File file = new File(fileName);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(header + "\n");
            } catch (IOException e) {
                System.err.println("Błąd podczas tworzenia pliku raportu: " + e.getMessage());
            }
        }
    }

    /**
     * Zapisuje pojedynczy wiersz danych do pliku raportu.
     * Każdy wiersz reprezentuje stan symulacji w danym kroku.
     *
     * @param step numer kroku symulacji
     * @param citiesCount aktualna liczba miast
     * @param reactorsCount aktualna liczba reaktorów
     * @param windDirection aktualny kierunek wiatru
     * @param totalPopulation łączna populacja we wszystkich miastach
     * @param totalEnergyDemand łączne zapotrzebowanie na energię
     * @param activeReactors liczba aktywnych reaktorów
     */
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

package org.example;

import java.util.Scanner;

/**
 * Główna klasa programu, odpowiedzialna za uruchomienie symulacji.
 * Zbiera dane konfiguracyjne od użytkownika za pośrednictwem konsoli,
 * a następnie inicjuje i uruchamia obiekt symulacji.
 */
public class Main {
    /** Singleton konfiguracji symulacji, dostarczający globalne parametry. */
    private final static ConfigLoader config = ConfigLoader.getInstance();

    /**
     * Metoda główna aplikacji (punkt wejścia).
     * Uruchamia proces zbierania konfiguracji i rozpoczyna symulację.
     *
     * @param args argumenty wiersza poleceń (nieużywane w tej aplikacji).
     */
    public static void main(String[] args) {
        int[] simConfig = getSimulationConfig();
        Simulation simulation = new Simulation(
                simConfig[0], simConfig[1], simConfig[2], simConfig[3], simConfig[4], simConfig[5]
        );
        simulation.run();
    }

    /**
     * Zbiera od użytkownika dane konfiguracyjne niezbędne do uruchomienia symulacji.
     *
     * @return tablica int[] z wartościami konfiguracyjnymi w następującej kolejności:
     *         [szerokość mapy, wysokość mapy, maks. miast, maks. reaktorów, pocz. miast, pocz. reaktorów]
     */
    private static int[] getSimulationConfig() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== KONFIGURACJA SYMULACJI ===");
        int mapWidth = getValidInput(scanner, "Szerokość mapy (400-1000): ", 400, 1000);
        int mapHeight = getValidInput(scanner, "Wysokość mapy (400-1000): ", 400, 1000);

        int maxCitiesLimit = config.getMaxCities();
        int maxReactorsLimit = config.getMaxReactors();

        int initialCities = getValidInput(scanner, String.format("Początkowa liczba miast (1-%d): ", maxCitiesLimit), 1, maxCitiesLimit);
        int initialReactors = getValidInput(scanner, String.format("Początkowa liczba reaktorów (1-%d): ", maxReactorsLimit), 1, maxReactorsLimit);

        scanner.close();
        return new int[] { mapWidth, mapHeight, maxCitiesLimit, maxReactorsLimit, initialCities, initialReactors };
    }

    /**
     * Pobiera od użytkownika poprawną liczbę całkowitą z zadanego przedziału.
     * Pętla kontynuuje działanie, dopóki użytkownik nie poda prawidłowej wartości.
     *
     * @param scanner obiekt Scannera do odczytu wejścia.
     * @param prompt komunikat wyświetlany użytkownikowi.
     * @param min minimalna akceptowalna wartość (włącznie).
     * @param max maksymalna akceptowalna wartość (włącznie).
     * @return poprawna liczba całkowita podana przez użytkownika.
     */
    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Proszę wprowadzić poprawną liczbę całkowitą!");
                scanner.next();
                System.out.print(prompt);
            }
            value = scanner.nextInt();
            if (value < min || value > max) {
                System.out.printf("Wartość musi znajdować się w przedziale od %d do %d!\n", min, max);
            }
        } while (value < min || value > max);
        return value;
    }
}

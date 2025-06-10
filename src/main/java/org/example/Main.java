package org.example;

import java.util.Scanner;

public class Main {
    private final static ConfigLoader config = ConfigLoader.getInstance();
    public static void main(String[] args) {
        int[] config = getSimulationConfig();
        Simulation simulation = new Simulation(
                config[0], config[1], config[2], config[3], config[4], config[5]
        );
        simulation.run();
    }

    private static int[] getSimulationConfig() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== KONFIGURACJA SYMULACJI ===");
        int mapWidth = getValidInput(scanner, "Szerokość mapy (400-1000): ", 400, 1000);
        int mapHeight = getValidInput(scanner, "Wysokość mapy (400-1000): ", 400, 1000);

        int maxCitiesLimit = config.getMaxCities();
        int maxReactorsLimit = config.getMaxReactors();

        int initialCities = getValidInput(scanner, String.format("Początkowa liczba miast (1-%d): ", maxCitiesLimit), 1, maxCitiesLimit);
        int initialReactors = getValidInput(scanner, String.format("Początkowa liczba reaktorów (1-%d): ", maxReactorsLimit), 1, maxCitiesLimit);

        scanner.close();
        return new int[] { mapWidth, mapHeight, maxCitiesLimit, maxCitiesLimit, initialCities, initialReactors };
    }

    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Proszę wprowadzić liczbę całkowitą!");
                scanner.next();
                System.out.print(prompt);
            }
            value = scanner.nextInt();
            if (value < min || value > max) {
                System.out.printf("Wartość musi być między %d a %d!\n", min, max);
            }
        } while (value < min || value > max);
        return value;
    }
}
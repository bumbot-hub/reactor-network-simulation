package org.example;

import java.util.Scanner;

public class Main {
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
        int maxPossibleObjects = calculateMaxObjects(mapWidth, mapHeight);
        int maxCitiesLimit = Math.min(maxPossibleObjects / 2, 1000);
        int maxReactorsLimit = Math.min(maxPossibleObjects / 4, 200);

        System.out.printf("Dla mapy %dx%d sugerowane limity:\n", mapWidth, mapHeight);
        System.out.printf("Maksymalne miasta: %d, Maksymalne reaktory: %d\n", maxCitiesLimit, maxReactorsLimit);

        int maxCities = getValidInput(scanner,
                String.format("Maksymalna liczba miast (1-%d): ", maxCitiesLimit), 1, maxCitiesLimit);
        int maxReactors = getValidInput(scanner,
                String.format("Maksymalna liczba reaktorów (1-%d): ", maxReactorsLimit), 1, maxReactorsLimit);
        int initialCities = getValidInput(scanner,
                String.format("Początkowa liczba miast (1-%d): ", maxCities), 1, maxCities);
        int initialReactors = getValidInput(scanner,
                String.format("Początkowa liczba reaktorów (1-%d): ", maxReactors), 1, maxReactors);

        scanner.close();

        return new int[] { mapWidth, mapHeight, maxCities, maxReactors, initialCities, initialReactors };
    }

    // 30% mapy może być zajęte przez obiekty dla dobrej rozgrywki
    private static int calculateMaxObjects(int width, int height) {
        return (int)((width * height) * 0.3);
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
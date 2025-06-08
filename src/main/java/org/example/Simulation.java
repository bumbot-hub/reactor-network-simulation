package org.example;

import java.util.List;

public class Simulation {
    private final TerrainMap terrain;
    private final DataLogger logger;
    private int stepCounter;
    private final int simulationDuration;

    public Simulation(int mapWidth, int mapHeight, int maxCities, int maxReactors, int initialCities, int initialReactors) {
        this.terrain = new TerrainMap(
                new int[]{mapWidth, mapHeight},
                maxCities,
                maxReactors
        );
        this.logger = new DataLogger();
        this.stepCounter = 0;
        this.simulationDuration = 100;

        initializeSimulation(initialCities, initialReactors);
    }

    private void initializeSimulation(int initialCities, int initialReactors) {
        for (int i = 0; i < initialCities; i++) {
            terrain.generateCity();
        }

        for (int i = 0; i < initialReactors; i++) {
            terrain.generateReactor(null);
        }
        terrain.visualize();
    }

    public void run() {
        while (stepCounter < simulationDuration) {
            terrain.updateStepInGUI(stepCounter);
            runStep();
        }
        printFinalStats();
    }

    private void runStep() {
        stepCounter++;

        generateNewObjects();
        terrain.update();
        logCurrentState();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void generateNewObjects() {
        if (stepCounter % 3 == 0 && terrain.getCities().size() < terrain.getMaxCities()) {
            terrain.generateCity();
        }
    }

    private void logCurrentState() {
        List<City> cities = terrain.getCities();
        List<Reactor> reactors = terrain.getReactors();

        int activeReactors = (int) reactors.stream()
                .filter(Reactor::checkActivity)
                .count();

        logger.saveData(
                stepCounter,
                cities.size(),
                reactors.size(),
                terrain.getWindDirection(),
                calculateTotalPopulation(cities),
                calculateTotalEnergyDemand(cities),
                activeReactors
        );
    }

    private int calculateTotalPopulation(List<City> cities) {
        return cities.stream()
                .mapToInt(City::getPopulation)
                .sum();
    }

    private float calculateTotalEnergyDemand(List<City> cities) {
        return (float) cities.stream()
                .mapToDouble(City::getEnergyUsage)
                .sum();
    }

    private void printFinalStats() {
        System.out.println("\n=== PODSUMOWANIE ===");
        System.out.println("Liczba kroków: " + stepCounter);
        System.out.println("Ostateczna liczba miast: " + terrain.getCities().size());
        System.out.println("Ostateczna liczba reaktorów: " + terrain.getReactors().size());
        System.out.println("Ostatni kierunek wiatru: " + terrain.getWindDirection());
    }
}
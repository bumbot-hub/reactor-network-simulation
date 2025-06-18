package org.example;

import java.util.List;

/**
 * Główna klasa sterująca przebiegiem symulacji reaktorów jądrowych.
 * Odpowiada za inicjalizację, uruchomienie głównej pętli, logowanie danych
 * oraz zarządzanie cyklem życia symulacji.
 */
public class Simulation {
    /** Singleton konfiguracji symulacji. */
    private static final ConfigLoader config = ConfigLoader.getInstance();

    /** Obiekt mapy terenu, na której odbywa się symulacja. */
    private final TerrainMap terrain;
    /** Obiekt odpowiedzialny za zapisywanie danych do pliku CSV. */
    private final DataLogger logger;
    /** Licznik wykonanych kroków symulacji. */
    private int stepCounter;
    /** Czas trwania symulacji (w krokach), wczytywany z konfiguracji. */
    private final int simulationDuration;

    /**
     * Konstruktor klasy Simulation.
     * Inicjalizuje wszystkie komponenty symulacji na podstawie podanych parametrów.
     *
     * @param mapWidth szerokość mapy
     * @param mapHeight wysokość mapy
     * @param maxCities maksymalna dozwolona liczba miast
     * @param maxReactors maksymalna dozwolona liczba reaktorów
     * @param initialCities początkowa liczba miast
     * @param initialReactors początkowa liczba reaktorów
     */
    public Simulation(int mapWidth, int mapHeight, int maxCities, int maxReactors, int initialCities, int initialReactors) {
        this.terrain = new TerrainMap(
                new int[]{mapWidth, mapHeight},
                maxCities,
                maxReactors
        );
        this.logger = new DataLogger();
        this.stepCounter = 0;
        this.simulationDuration = config.getSimulationDuration();

        initializeSimulation(initialCities, initialReactors);
    }

    /**
     * Inicjalizuje stan początkowy symulacji.
     * Generuje zadaną liczbę miast i reaktorów oraz uruchamia wizualizację.
     *
     * @param initialCities początkowa liczba miast
     * @param initialReactors początkowa liczba reaktorów
     */
    private void initializeSimulation(int initialCities, int initialReactors) {
        for (int i = 0; i < initialCities; i++) {
            terrain.generateCity();
        }

        for (int i = 0; i < initialReactors; i++) {
            terrain.generateReactor(null);
        }
        terrain.visualize();
    }

    /**
     * Uruchamia główną pętlę symulacji, która trwa do osiągnięcia zdefiniowanego czasu.
     * Po zakończeniu pętli, drukuje podsumowanie.
     */
    public void run() {
        while (stepCounter < simulationDuration) {
            terrain.updateStepInGUI(stepCounter);
            runStep();
        }
        printFinalStats();
    }

    /**
     * Wykonuje pojedynczy krok symulacji.
     * Zwiększa licznik, generuje nowe obiekty, aktualizuje stan mapy, loguje dane
     * i wstrzymuje wykonanie na 1 sekundę.
     */
    private void runStep() {
        stepCounter++;

        generateNewObjects();
        terrain.update();
        logCurrentState();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Generuje nowe obiekty na mapie (np. miasta) zgodnie z częstotliwością
     * i limitami zdefiniowanymi w konfiguracji.
     */
    private void generateNewObjects() {
        if (stepCounter % config.getCityGenerationFrequency() == 0 && terrain.getCities().size() < terrain.getMaxCities()) {
            terrain.generateCity();
        }
    }

    /**
     * Zbiera aktualne dane o stanie symulacji i zapisuje je do pliku CSV
     * za pomocą obiektu DataLogger.
     */
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

    /**
     * Oblicza łączną populację wszystkich miast na mapie.
     *
     * @param cities lista miast
     * @return całkowita populacja
     */
    private int calculateTotalPopulation(List<City> cities) {
        return cities.stream()
                .mapToInt(City::getPopulation)
                .sum();
    }

    /**
     * Oblicza łączne zapotrzebowanie na energię wszystkich miast na mapie.
     *
     * @param cities lista miast
     * @return całkowite zapotrzebowanie na energię (w MW)
     */
    private float calculateTotalEnergyDemand(List<City> cities) {
        return (float) cities.stream()
                .mapToDouble(City::getEnergyUsage)
                .sum();
    }

    /**
     * Drukuje na konsoli podsumowanie statystyk po zakończeniu symulacji.
     */
    private void printFinalStats() {
        System.out.println("\n=== PODSUMOWANIE ===");
        System.out.println("Liczba kroków: " + stepCounter);
        System.out.println("Ostateczna liczba miast: " + terrain.getCities().size());
        System.out.println("Ostateczna liczba reaktorów: " + terrain.getReactors().size());
        System.out.println("Ostatni kierunek wiatru: " + terrain.getWindDirection());
    }
}

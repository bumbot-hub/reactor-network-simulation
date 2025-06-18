package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa reprezentująca mapę terenu w symulacji[1].
 * Zarządza wszystkimi obiektami (miastami, reaktorami, zanieczyszczeniami),
 * ich interakcjami, generowaniem oraz warunkami środowiskowymi, takimi jak wiatr[1].
 * Stanowi centralny komponent logiki symulacji[1].
 */
class TerrainMap {
    /** Singleton konfiguracji symulacji[1]. */
    private static final ConfigLoader config = ConfigLoader.getInstance();

    /** Lista miast na mapie[1]. */
    private List<City> cities;
    /** Lista reaktorów na mapie[1]. */
    private List<Reactor> reactors;
    /** Aktualny kierunek wiatru[1]. */
    private String windDirection;
    /** Lista zanieczyszczeń na mapie[1]. */
    private List<Pollution> pollutions;
    /** Wymiary mapy: [szerokość, wysokość][1]. */
    private final int[] dimensions;
    /** Maksymalna liczba miast dozwolona na mapie[1]. */
    private final int maxCities;
    /** Maksymalna liczba reaktorów dozwolona na mapie[1]. */
    private final int maxReactors;
    /** Trójwymiarowa siatka do śledzenia zajętych pozycji na mapie[1]. */
    private List<List<List<MapObject>>> occupiedPositions;
    /** Liczba poziomów wysokości na mapie (np. ziemia, powietrze)[1]. */
    private final int HEIGHT_LEVELS = 2;
    /** Komponent wizualizujący mapę w GUI[1]. */
    private MapVisualizer visualizer;

    /**
     * Konstruktor klasy TerrainMap[1].
     * Inicjalizuje mapę o podanych wymiarach i limitach obiektów[1].
     *
     * @param mapSize tablica z wymiarami mapy [szerokość, wysokość][1].
     * @param maxCities maksymalna liczba miast[1].
     * @param maxReactors maksymalna liczba reaktorów[1].
     * @throws IllegalArgumentException jeśli podane parametry są nieprawidłowe[1].
     */
    public TerrainMap(int[] mapSize, int maxCities, int maxReactors) {
        validateParameters(mapSize, maxCities, maxReactors);

        this.dimensions = mapSize.clone();
        this.maxCities = maxCities;
        this.maxReactors = maxReactors;
        this.cities = new ArrayList<>();
        this.reactors = new ArrayList<>();
        this.pollutions = new ArrayList<>();
        this.windDirection = updateWind();

        initializeOccupiedPositions();
    }

    /**
     * Waliduje parametry wejściowe konstruktora[1].
     *
     * @param mapSize wymiary mapy[1].
     * @param maxCities maksymalna liczba miast[1].
     * @param maxReactors maksymalna liczba reaktorów[1].
     * @throws IllegalArgumentException jeśli wymiary lub limity są nieprawidłowe[1].
     */
    private void validateParameters(int[] mapSize, int maxCities, int maxReactors) {
        if (mapSize == null || mapSize.length != 2 || mapSize[0] < 400 || mapSize[1] < 400) {
            throw new IllegalArgumentException("Nieprawidłowe wymiary mapy");
        }
        if (maxCities < 1 || maxReactors < 1) {
            throw new IllegalArgumentException("Maksymalna liczba miast i reaktorów musi być większa od 0");
        }
    }

    /**
     * Inicjalizuje strukturę danych do śledzenia zajętych pozycji na mapie[1].
     */
    private void initializeOccupiedPositions() {
        this.occupiedPositions = new ArrayList<>(dimensions[0]);
        for (int x = 0; x < dimensions[0]; x++) {
            List<List<MapObject>> yList = new ArrayList<>(dimensions[1]);
            for (int y = 0; y < dimensions[1]; y++) {
                List<MapObject> zList = new ArrayList<>(HEIGHT_LEVELS);
                for (int z = 0; z < HEIGHT_LEVELS; z++) {
                    zList.add(null);
                }
                yList.add(zList);
            }
            occupiedPositions.add(yList);
        }
    }

    /**
     * Główna metoda aktualizująca stan całej mapy i wszystkich obiektów na niej[1].
     * Wywoływana w każdym kroku symulacji[1].
     */
    public void update() {
        updateWindDirection();
        updateConnections();
        updateCities();
        updateReactors();
        updatePollutions();
    }

    /**
     * Aktualizuje kierunek wiatru na mapie[1].
     */
    private void updateWindDirection() {
        this.windDirection = updateWind();
    }

    /**
     * Aktualizuje stan wszystkich miast na mapie, wywołując ich metody update()[1].
     */
    private void updateCities() {
        for (City city : cities) {
            city.update();
            city.info();
        }
    }

    /**
     * Aktualizuje stan wszystkich reaktorów na mapie, wywołując ich metody update()[1].
     */
    private void updateReactors() {
        for (Reactor reactor : reactors) {
            reactor.update();
            reactor.info();
        }
    }

    /**
     * Aktualizuje stan wszystkich zanieczyszczeń na mapie, wywołując ich metody update()[1].
     */
    private void updatePollutions() {
        for (Pollution pollution : pollutions) {
            pollution.update();
        }
    }

    /**
     * Losuje nowy kierunek wiatru[1].
     *
     * @return nowy kierunek wiatru jako String[1].
     */
    public String updateWind() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "C"};
        return directions[new Random().nextInt(directions.length)];
    }

    /**
     * Dodaje miasto do mapy, jeśli pozycja jest poprawna i wolna[1].
     *
     * @param city obiekt miasta do dodania[1].
     */
    public void addCity(City city) {
        int[] position = city.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            cities.add(city);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], city);
        }
    }

    /**
     * Dodaje reaktor do mapy, jeśli pozycja jest poprawna i wolna[1].
     *
     * @param reactor obiekt reaktora do dodania[1].
     */
    public void addReactor(Reactor reactor) {
        int[] position = reactor.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            reactors.add(reactor);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], reactor);
        }
    }

    /**
     * Dodaje zanieczyszczenie do mapy, jeśli pozycja jest poprawna i wolna[1].
     *
     * @param pollution obiekt zanieczyszczenia do dodania[1].
     */
    public void addPollution(Pollution pollution) {
        int[] position = pollution.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            pollutions.add(pollution);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], pollution);
        }
    }

    /**
     * Sprawdza, czy podana pozycja znajduje się w granicach mapy[1].
     *
     * @param position tablica współrzędnych [x, y, z][1].
     * @return true, jeśli pozycja jest poprawna, w przeciwnym razie false[1].
     */
    private boolean isPositionValid(int[] position) {
        return position != null &&
                position.length == 3 &&
                position[0] >= 0 &&
                position[0] < dimensions[0] &&
                position[1] >= 0 &&
                position[1] < dimensions[1] &&
                position[2] >= 0 &&
                position[2] < HEIGHT_LEVELS;
    }

    /**
     * Sprawdza, czy podana pozycja jest wolna[1].
     *
     * @param position tablica współrzędnych [x, y, z][1].
     * @return true, jeśli pozycja jest wolna, w przeciwnym razie false[1].
     */
    private boolean isPositionEmpty(int[] position) {
        return occupiedPositions.get(position[0]).get(position[1]).get(position[2]) == null;
    }

    /**
     * Generuje nowe miasto na losowej, wolnej pozycji, jeśli nie osiągnięto limitu miast[1].
     */
    public void generateCity() {
        if (cities.size() >= maxCities) {
            return;
        }

        Random random = new Random();
        int population = 15000 + random.nextInt(60000);
        int[] coords = findEmptyPosition("city");

        if (coords != null) {
            City city = new City(cities.size() + 1, coords, population);
            addCity(city);
        }
    }

    /**
     * Generuje nowy reaktor w pobliżu podanego miasta lub na losowej pozycji[1].
     *
     * @param cityCoords współrzędne miasta, w pobliżu którego ma powstać reaktor (może być null)[1].
     */
    public void generateReactor(int[] cityCoords) {
        if (reactors.size() >= maxReactors) {
            return;
        }

        Random random = new Random();
        int reactorLevel = 1 + random.nextInt(4);
        int[] coords = null;

        if(cityCoords != null) {
            double radius = Math.min(dimensions[0], dimensions[1]) * 0.3;
            int attempts = 0;
            final int MAX_ATTEMPTS = 30;
            int z = 0;

            while(attempts<MAX_ATTEMPTS) {
                int dx = random.nextInt((int) radius * 2 + 1) - (int) radius;
                int dy = random.nextInt((int) radius * 2 + 1) - (int) radius;

                int x = cityCoords[0] + dx;
                int y = cityCoords[1] + dy;

                int[] position = new int[]{x, y, z};
                if(isPositionValid(position) && isPositionEmpty(position)) {
                    coords = position;
                    break;
                }
                attempts++;
            }
        }else {
            coords = findEmptyPosition("reactor");
        }

        if (coords != null) {
            Reactor reactor = new Reactor(reactors.size() + 1, coords, reactorLevel, this);
            addReactor(reactor);
        }
    }

    /**
     * Znajduje losową wolną pozycję na mapie dla danego typu obiektu[1].
     *
     * @param objectType typ obiektu ("city", "reactor", "pollution")[1].
     * @return tablica współrzędnych lub null, jeśli nie znaleziono wolnej pozycji[1].
     */
    private int[] findEmptyPosition(String objectType) {
        Random random = new Random();
        int attempts = 0;
        final int MAX_ATTEMPTS = 50;

        int z = 0;
        if (objectType.equals("pollution")) {
            z = 1;
        }

        while (attempts < MAX_ATTEMPTS) {
            int x = 50 + random.nextInt(dimensions[0] - 100);
            int y = 50 + random.nextInt(dimensions[1] - 100);
            int[] position = new int[]{x, y, z};

            if (isPositionValid(position) && isPositionEmpty(position) && isFarEnoughFromOthers(position, 20)) {
                return position;
            }
            attempts++;
        }
        return null;
    }

    /**
     * Sprawdza, czy podana pozycja jest wystarczająco oddalona od innych obiektów[1].
     *
     * @param position pozycja do sprawdzenia[1].
     * @param minDistance minimalna wymagana odległość[1].
     * @return true, jeśli pozycja jest wystarczająco oddalona[1].
     */
    private boolean isFarEnoughFromOthers(int[] position, int minDistance) {
        for (City city : cities) {
            if (calculateDistance(city.getPosition(), position) < minDistance) {
                return false;
            }
        }
        for (Reactor reactor : reactors) {
            if (calculateDistance(reactor.getPosition(), position) < minDistance) {
                return false;
            }
        }
        for (Pollution pollution : pollutions) {
            if (calculateDistance(pollution.getPosition(), position) < minDistance) {
                return false;
            }
        }
        return true;
    }

    /**
     * Aktualizuje połączenia energetyczne między miastami a reaktorami[1].
     */
    public void updateConnections() {
        for (City city : cities) {
            Reactor currentReactor = city.getReactor();

            if (currentReactor == null || !currentReactor.checkActivity()) {
                if (currentReactor != null) {
                    currentReactor.removeCity(city);
                }
                connectCityToBestReactor(city);
            }
        }
    }

    /**
     * Łączy miasto z najlepszym dostępnym reaktorem w zasięgu[1].
     *
     * @param city miasto do podłączenia[1].
     */
    private void connectCityToBestReactor(City city) {
        Reactor bestReactor = null;
        double minDistance = Double.MAX_VALUE;

        for (Reactor reactor : reactors) {
            if (reactor.checkActivity()) {
                double distance = calculateDistance(city.getPosition(), reactor.getPosition());
                float availablePower = reactor.getMaxPower() - reactor.getCurrentPower();
                double maxDistance = Math.min(dimensions[0], dimensions[1]) * 0.3;

                if(distance <= maxDistance && availablePower >= city.getEnergyUsage() * config.getEnergyConnectionPowerBuffer()) {
                    if(distance < minDistance) {
                        minDistance = distance;
                        bestReactor = reactor;
                    }
                }
            }
        }

        if (bestReactor != null) {
            city.connectWithReactor(bestReactor);
            bestReactor.addCity(city);
        } else {
            generateReactor(city.getPosition());
        }
    }

    /**
     * Oblicza odległość euklidesową między dwoma punktami w 2D (ignoruje oś Z)[1].
     *
     * @param a pierwsza pozycja [x, y, z][1].
     * @param b druga pozycja [x, y, z][1].
     * @return odległość jako double[1].
     */
    public double calculateDistance(int[] a, int[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    /**
     * Uruchamia wizualizację mapy w oknie Swing[1].
     */
    public void visualize() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Symulacja Reaktorów Jądrowych");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.visualizer = new MapVisualizer(this);
            frame.add(visualizer);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Aktualizuje numer kroku symulacji wyświetlany w GUI[1].
     *
     * @param step aktualny numer kroku[1].
     */
    public void updateStepInGUI(int step) {
        if (visualizer != null) {
            SwingUtilities.invokeLater(() -> visualizer.updateStep(step));
        }
    }

    /**
     * Zwraca szerokość mapy[1].
     * @return szerokość mapy w pikselach[1].
     */
    public int getWidth() {
        return dimensions[0];
    }

    /**
     * Zwraca wysokość mapy[1].
     * @return wysokość mapy w pikselach[1].
     */
    public int getHeight() {
        return dimensions[1];
    }

    /**
     * Zwraca kopię listy miast[1].
     * @return lista miast[1].
     */
    public List<City> getCities() {
        return new ArrayList<>(cities);
    }

    /**
     * Zwraca kopię listy reaktorów[1].
     * @return lista reaktorów[1].
     */
    public List<Reactor> getReactors() {
        return new ArrayList<>(reactors);
    }

    /**
     * Zwraca kopię listy zanieczyszczeń[1].
     * @return lista zanieczyszczeń[1].
     */
    public List<Pollution> getPollutions() {
        return new ArrayList<>(pollutions);
    }

    /**
     * Zwraca aktualny kierunek wiatru[1].
     * @return kierunek wiatru[1].
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * Zwraca maksymalną dozwoloną liczbę miast[1].
     * @return maksymalna liczba miast[1].
     */
    public int getMaxCities() {
        return maxCities;
    }
}

package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TerrainMap {
    private List<City> cities;
    private List<Reactor> reactors;
    private String windDirection;
    private List<Pollution> pollutions;
    private int[] dimensions;
    private int maxCities;
    private int maxReactors;
    private List<List<List<MapObject>>> occupiedPositions;
    private final int HEIGHT_LEVELS = 2;
    private MapVisualizer visualizer;

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

    private void validateParameters(int[] mapSize, int maxCities, int maxReactors) {
        if (mapSize == null || mapSize.length != 2 || mapSize[0] < 400 || mapSize[1] < 400) {
            throw new IllegalArgumentException("Nieprawidłowe wymiary mapy");
        }
        if (maxCities < 1 || maxReactors < 1) {
            throw new IllegalArgumentException("Maksymalna liczba miast i reaktorów musi być większa od 0");
        }
    }

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

    public void update() {
        updateWindDirection();
        updateConnections();
        updateCities();
        updateReactors();
        updatePollutions();
    }

    private void updateWindDirection() {
        this.windDirection = updateWind();
    }

    private void updateCities() {
        for (City city : cities) {
            city.update();
            city.info();
        }
    }

    private void updateReactors() {
        for (Reactor reactor : reactors) {
            reactor.update();
            reactor.info();
        }
    }

    private void updatePollutions() {
        for (Pollution pollution : pollutions) {
            pollution.update();
        }
    }

    public String updateWind() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "C"};
        return directions[new Random().nextInt(directions.length)];
    }

    public void addCity(City city) {
        int[] position = city.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            cities.add(city);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], city);
        }
    }

    public void addReactor(Reactor reactor) {
        int[] position = reactor.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            reactors.add(reactor);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], reactor);
        }
    }

    public void addPollution(Pollution pollution) {
        int[] position = pollution.getPosition();
        if (isPositionValid(position) && isPositionEmpty(position)) {
            pollutions.add(pollution);
            occupiedPositions.get(position[0]).get(position[1]).set(position[2], pollution);
        }
    }

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

    private boolean isPositionEmpty(int[] position) {
        return occupiedPositions.get(position[0]).get(position[1]).get(position[2]) == null;
    }

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

    private int[] findEmptyPosition(String objectType) {
        Random random = new Random();
        int attempts = 0;
        final int MAX_ATTEMPTS = 50;

        int z = 0;
        if (objectType.equals("pollution")) {
            z = 1;
        }

        while (attempts < MAX_ATTEMPTS) {
            int x = random.nextInt(dimensions[0]);
            int y = random.nextInt(dimensions[1]);
            int[] position = new int[]{x, y, z};

            if (isPositionValid(position) && isPositionEmpty(position)) {
                return position;
            }
            attempts++;
        }
        return null;
    }


    // Logika połączeń
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

    private void connectCityToBestReactor(City city) {
        Reactor bestReactor = null;
        double minDistance = Double.MAX_VALUE;

        for (Reactor reactor : reactors) {
            if (reactor.checkActivity()) {
                double distance = calculateDistance(city.getPosition(), reactor.getPosition());
                float availablePower = reactor.getMaxPower() - reactor.getCurrentPower();

                double maxDistance = Math.min(dimensions[0], dimensions[1]) * 0.3; // 30% rozmiaru mapy

                System.out.printf("Miasto %d -> Reaktor %d: odległość=%.1f, max=%.1f, dostępna_moc=%.1f, potrzebna=%.1f\n",
                        city.getId(), reactor.getId(), distance, maxDistance, availablePower, city.getEnergyUsage());

                if (distance <= maxDistance && availablePower >= city.getEnergyUsage() * 1.05) { // Zmniejszony bufor z 1.10 na 1.05
                    if (distance < minDistance) {
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

    // Poprawiona metoda calculateDistance() - ignoruje współrzędną Z
    private double calculateDistance(int[] a, int[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

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

    // Nowa metoda do aktualizacji kroku
    public void updateStepInGUI(int step) {
        if (visualizer != null) {
            SwingUtilities.invokeLater(() -> visualizer.updateStep(step));
        }
    }

    public int getWidth() {
        return dimensions[0];
    }

    public int getHeight() {
        return dimensions[1];
    }

    public List<City> getCities() {
        return new ArrayList<>(cities);
    }
    public List<Reactor> getReactors() {
        return new ArrayList<>(reactors);
    }

    public List<Pollution> getPollutions() {
        return new ArrayList<>(pollutions);
    }
    public String getWindDirection() {
        return windDirection;
    }
    public int getMaxCities() {
        return maxCities;
    }
    public int getMaxReactors() {
        return maxReactors;
    }
    public int[] getDimensions() {
        return dimensions.clone();
    }
}
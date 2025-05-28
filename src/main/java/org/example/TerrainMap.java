package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TerrainMap {
    private List<City> cities;
    private List<Reactor> reactors;
    private String windDirection;
    private List<Pollution> pollutions;
    private int[] dimensions;
    private List<int[]> drawablePositions;
    private int maxCities;
    private int maxReactors;
    private final int REACTOR_RADIUS = 10;
    private List<MapObject> occupiedPositions;

    public TerrainMap(int[] mapSize,int maxCities, int maxReactors) {
        this.dimensions = mapSize;
        this.maxCities = maxCities;
        this.maxReactors = maxReactors;
        this.cities = new ArrayList<>();
        this.reactors = new ArrayList<>();
        this.pollutions = new ArrayList<>();
        this.windDirection = updateWind();
        this.occupiedPositions = new ArrayList<>();
    }

    public void update(){
        windDirection = updateWind();
        updateConnections();
        for(City city: cities){
            city.update();
            city.info();
        }

        for(Reactor reactor: reactors){
            reactor.update();
            reactor.info();
        }

    }

    public String updateWind() {
        String[] values = {"N","NE","E","SE","S","SW","W","NW","C"};
        Random rand = new Random();

        System.out.println(values[rand.nextInt(values.length)]+"\n");

        return values[rand.nextInt(values.length)];
    }

    public void addCity(City city){
        if(cities.size()<maxCities){
           cities.add(city);
           //occupiedPositions.add();
        }
    }

    public void addReactor(Reactor reactor){
        if(reactors.size()<maxReactors){
            reactors.add(reactor);
            //occupiedPositions.add();
        }
    }

    public void updateConnections() {
        for(City city: cities){
            // Connect with reactor if connection is non-existing
            if(city.getReactor() == null){
                Reactor bestReactor = null;
                double minDistance = Double.MAX_VALUE;

                for(Reactor reactor: reactors){
                    float leftPower = reactor.getMaxPower() - reactor.getCurrentPower();
                    double distance = calculateDistance(city.getPosition(), reactor.getPosition());

                    if(leftPower >= city.getEnergyUsage() * 1.10 & distance <= 70 & reactor.checkActivity()){
                        if(distance < minDistance){
                            minDistance = distance;
                            bestReactor = reactor;
                        }
                    }
                }

                if(bestReactor != null){
                    city.connectWithReactor(bestReactor);
                }
            }

            // Disconnect with reactor if it's turned off
        }
    }

    private double calculateDistance(float[] a, float[] b){
        return Math.sqrt(Math.pow(a[0] - b[0],2) + Math.pow(a[1] - b[1],2));
    }

    public void visualize() {
        // Visualization implementation
    }

    public int citiesInRange() {
        // Implementation to count cities in range
        return 0;
    }
}
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
    private int maxCities;
    private int maxReactors;
    private final int REACTOR_RADIUS = 10;
    private List<List<MapObject>> occupiedPositions;

    public TerrainMap(int[] mapSize,int maxCities, int maxReactors) {
        this.dimensions = mapSize;
        this.maxCities = maxCities;
        this.maxReactors = maxReactors;
        this.cities = new ArrayList<>();
        this.reactors = new ArrayList<>();
        this.pollutions = new ArrayList<>();
        this.windDirection = updateWind();

        this.occupiedPositions = new ArrayList<>(dimensions[0]);
        for(int i=0;i<dimensions[0];i++){
            ArrayList<MapObject> row = new ArrayList<>(dimensions[1]);
            for(int j = 0; j < dimensions[1]; j++){
                row.add(null);
            }
            occupiedPositions.add(row);
        }
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

        for(Pollution pollution: pollutions){
            pollution.update();
            //pollution.info();
        }

    }

    public String updateWind() {
        String[] values = {"N","NE","E","SE","S","SW","W","NW","C"};
        Random rand = new Random();

        return values[rand.nextInt(values.length)];
    }

    public void addCity(City city){
        if(cities.size()<maxCities){
           cities.add(city);
           int[] position = city.getPosition();
           occupiedPositions.get(position[0]).set(position[1], city);
        }
    }

    public void addReactor(Reactor reactor){
        if(reactors.size()<maxReactors){
            reactors.add(reactor);
            int[] position = reactor.getPosition();
            occupiedPositions.get(position[0]).set(position[1], reactor);
        }
    }

    public void updateConnections() {
        for(City city: cities){
            Reactor cityReactor = city.getReactor();

            if(cityReactor == null || !cityReactor.checkActivity()){
                if(cityReactor != null){
                    cityReactor.removeCity(city);
                }

                Reactor bestReactor = null;
                double minDistance = Double.MAX_VALUE;

                for(Reactor reactor: reactors){
                    float leftPower = reactor.getMaxPower() - reactor.getCurrentPower();
                    double distance = calculateDistance(city.getPosition(), reactor.getPosition());

                    if(leftPower >= city.getEnergyUsage() * 1.10 && distance <= 70 && reactor.checkActivity()){
                        if(distance < minDistance){
                            minDistance = distance;
                            bestReactor = reactor;
                        }
                    }
                }

                if(bestReactor != null){
                    city.connectWithReactor(bestReactor);
                    bestReactor.addCity(city);
                }else{
                    generateReactor();
                }
            }
        }
    }

    public void generateCity(){
        Random random = new Random();
        int population = 10500 + random.nextInt(90000);
        int x = random.nextInt(this.dimensions[0]);
        int y = random.nextInt(this.dimensions[1]);
        int[] coords = new int[]{x,y};
        City city = new City((cities.size()+1), coords, population);
        cities.add(city);
    }

    public void generateReactor(){
        Random random = new Random();
        float maxPower = 10500 + random.nextInt(90000);
        int reactorLevel = 1 + random.nextInt(4);
        int x = random.nextInt(this.dimensions[0]);
        int y = random.nextInt(this.dimensions[1]);
        int[] coords = new int[]{x,y};
        Reactor reactor = new Reactor(reactors.size()+1, coords, maxPower, reactorLevel);
        reactors.add(reactor);
    }

    private double calculateDistance(int[] a, int[] b){
        return Math.sqrt(Math.pow(a[0] - b[0],2) + Math.pow(a[1] - b[1],2));
    }

    public void visualize() {
        // Visualization implementation
    }
}
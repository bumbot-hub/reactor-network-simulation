package org.example;

class Simulation {
    private TerrainMap terrain;
    private DataLogger logger;
    private int stepCounter = 0;

    public Simulation(int maxCities, int maxReactors, int[] mapSize) {
        this.terrain = new TerrainMap(mapSize, maxCities, maxReactors);
        this.logger = new DataLogger();

        City c1 = new City(1, new int[]{34, 140}, 100430);
        City c2 = new City(2, new int[]{50, 70}, 500050);
        terrain.addCity(c1);
        terrain.addCity(c2);

        Reactor r1 = new Reactor(3, new int[]{45, 101}, 900, 5);
        terrain.addReactor(r1);
    }

    public void run(){
        for(int i=0;i<3;i++){
            runStep();
        }
    }

    public void runStep() {
        stepCounter++;
        if(stepCounter%5==0){
            terrain.generateCity();
        }
        if(stepCounter%12==0){
            terrain.generateReactor();
        }
        updatePollution();
        updateEnergyDistribution();
        terrain.update();
        logger.saveData();
    }

    public void updatePollution() {
        // Pollution update logic
    }

    public void updateEnergyDistribution() {
        // Energy distribution logic
    }
}
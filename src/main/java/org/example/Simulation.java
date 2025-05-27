package org.example;

class Simulation {
    private TerrainMap terrain;
    private DataLogger logger;

    public Simulation(int cityNum, int reactorNum, float[] mapSize) {
        this.terrain = new TerrainMap();
        this.logger = new DataLogger();
    }

    public void run(){
        while (true) {
            runStep();
            logData();
            // Add delay or exit condition
        }
    }

    public void runStep() {
        updatePollution();
        updateEnergyDistribution();
    }

    public void updatePollution() {
        // Pollution update logic
    }

    public void updateEnergyDistribution() {
        // Energy distribution logic
    }

    public void logData() {
        logger.saveData();
    }
}
package org.example;
import java.util.Arrays;

public class City extends MapObject {
    private int population;
    private float energyUsage;
    private float pollutionLevel;
    private Reactor reactor;
    private int cityStatus;
    private float cityRadius;

    public City(int id, float[] coordinates, int population, float energyUsage,
                int cityStatus, float cityRadius) {
        super(id, coordinates);
        this.population = population;
        this.energyUsage = energyUsage;
        this.cityStatus = cityStatus;
        this.cityRadius = cityRadius;
        this.pollutionLevel = 0; // Default value

        connectWithReactor();
    }

    @Override
    public void update() {
        // Implementation of the abstract method
        // Update city state based on pollution, energy availability, etc.
        if (pollutionLevel > 0) {
            affectPopulation();
        }
        updateEnergyUsage();
    }

    private void affectPopulation() {
        // Reduce population based on pollution level
        population = Math.max(0, population - (int) (pollutionLevel * 10));
    }

    private void updateEnergyUsage() {
        // Adjust energy usage based on population and city status
        energyUsage = population * 0.1f * (cityStatus + 1);
    }

    public void connectWithReactor() {
        // this.reactor = new Reactor
    }

    // Getters and setters
    public int getPopulation() {
        return population;
    }

    public float getEnergyUsage() {
        return energyUsage;
    }

    public float getPollutionLevel() {
        return pollutionLevel;
    }

    public Reactor getReactor() {
        return reactor;
    }

    public float getCityRadius() {
        return cityRadius;
    }

    public void setPollutionLevel(float level) {
        pollutionLevel = level;
    }

    @Override
    public String toString() {
        return String.format("City[id=, population=%,d, energy=%.1f, pollution=%.1f] @ %s", population, energyUsage, pollutionLevel,
                Arrays.toString(getPosition()));
    }
}
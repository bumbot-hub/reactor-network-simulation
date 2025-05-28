package org.example;
import java.util.Arrays;
import java.util.Random;

public class City extends MapObject {
    private int population;
    private float energyUsage;
    private float pollutionLevel;
    private Reactor reactor;
    private double cityRadius;

    public City(int id, float[] coordinates, int population) {
        super(id, coordinates);
        this.population = population;
        this.cityRadius = Math.log(population)*2.3;
        this.pollutionLevel = 0;

        updateEnergyUsage();
        connectWithReactor();
    }

    @Override
    public void update() {
        updatePopulation();
        updateEnergyUsage();
    }

    private void updatePopulation() {
        Random random = new Random();
        float prob = random.nextFloat();

        //Add logic with pollution level
        if(prob>=0.6){
            this.population += (int) (1.01+ random.nextFloat() * 0.031f); // 0,1% - 3,1% range
        }else if(prob<=0.2){
            this.population -= (int) (1.01+ random.nextFloat() * 0.031f); // 0,1% - 3,1% range
        }
    }

    private void updateEnergyUsage() {
        energyUsage = population * 0.82f;
    }

    public void connectWithReactor() {

    }

    public void info(){
        System.out.println("City id " + getId());
        System.out.print("Population: " + this.population + "\nRadius: " + this.cityRadius + "\nEnergy usage: " + this.energyUsage + "\n\n");
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

    public double getCityRadius() {
        return cityRadius;
    }

    public void setPollutionLevel(float level) {
        pollutionLevel = level;
    }
}
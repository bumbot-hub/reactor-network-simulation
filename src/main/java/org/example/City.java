package org.example;
import java.util.Random;

public class City extends MapObject {
    private int population;
    private float energyUsage;
    private float pollutionLevel;
    private Reactor reactor;
    private double cityRadius;

    public City(int id, int[] coordinates, int population) {
        super(id, coordinates);
        this.population = population;
        this.cityRadius = Math.log(population)*2.3;
        this.pollutionLevel = 0;

        updateEnergyUsage();
    }

    @Override
    public void update() {
        updatePopulation();
        updateEnergyUsage();
    }

    private void updatePopulation() {
        Random random = new Random();
        float prob = random.nextFloat();

        if(prob>=0.35){
            this.population += (int) (this.population*(1.01+ random.nextFloat() * 0.031f)); // 0,1% - 3,1% range
        }else if(prob<=0.1){
            this.population -= (int) (this.population*(1.01+ random.nextFloat() * 0.006f)); // 0,1% - 0,6% range
        }
    }

    public void connectWithReactor(Reactor reactor){
        this.reactor = reactor;
    }

    private void updateEnergyUsage() {
        energyUsage = population * 0.00082f;
    }

    public void info(){
        System.out.printf("\n\nCity id %d:\nObszar: %.2f\nPopulacja: %d\nZapotrzebowanie mocy: %.6f\nSkażenie: %.2f%%\n\n",
                this.getId(),
                cityRadius,
                population,
                energyUsage,
                pollutionLevel
        );
    }


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
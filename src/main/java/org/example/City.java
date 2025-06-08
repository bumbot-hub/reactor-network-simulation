package org.example;

import java.util.Random;

public class City extends MapObject {
    private int population;
    private int originalPopulation;
    private float energyUsage;
    private float pollutionLevel;
    private Reactor reactor;
    private static final float ENERGY_PER_PERSON = 0.0005f;

    public City(int id, int[] coordinates, int population) {
        super(id, coordinates);
        this.population = population;
        this.originalPopulation = population;
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

        int newPopulation = population;

        if(prob >= 0.35) {
            // Wzrost populacji: 0.1% - 3.1%
            float growthRate = 0.001f + random.nextFloat() * 0.03f;
            newPopulation += (int)(population * growthRate);
        } else if(prob <= 0.1) {
            // Spadek populacji: 0.1% - 0.6%
            float declineRate = 0.001f + random.nextFloat() * 0.005f;
            newPopulation -= (int)(population * declineRate);
        }

        int minimumPopulation = (int)(originalPopulation * 0.9);
        if (newPopulation < minimumPopulation) {
            newPopulation = minimumPopulation;
        }

        this.population = newPopulation;
    }

    public void connectWithReactor(Reactor reactor){
        this.reactor = reactor;
    }

    private void updateEnergyUsage() {
        energyUsage = population * ENERGY_PER_PERSON;
    }

    public void info(){
        System.out.printf("\n Miasto %d:\n" +
                        "   Populacja: %,d (oryginalnie: %,d)\n" +
                        "   Zapotrzebowanie: %.1f MW\n" +
                        "   Skażenie: %.2f%%\n" +
                        "   Reaktor: %s\n\n",
                this.getId(),
                population,
                originalPopulation,
                energyUsage,
                pollutionLevel,
                (reactor != null && reactor.checkActivity()) ?
                        String.format("#%d (%.1f MW)", reactor.getId(), reactor.getMaxPower()) : "BRAK"
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

    public void setPollutionLevel(float level) {
        pollutionLevel = level;
    }
}

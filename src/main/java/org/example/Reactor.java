package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Reactor extends MapObject {
    private List<City> connectedCities;
    private float maxPower;
    private float currentPower;
    private boolean isMalfunction;
    private int reactorLevel;
    private float durability;
    private TerrainMap mapRefference;

    public Reactor(int id, int[] coordinates, int level, TerrainMap mapRefference) {
        super(id, coordinates);
        this.connectedCities = new ArrayList<>();
        this.reactorLevel = level;
        this.mapRefference = mapRefference;
        this.maxPower = calculateMaxPower(level);
        this.currentPower = 0.0f;
        this.isMalfunction = false;
        this.durability = 1.0f;
    }

    private float calculateMaxPower(int level) {
        Random random = new Random();
        switch(level) {
            case 1: return 50 + random.nextFloat() * 150;
            case 2: return 200 + random.nextFloat() * 400;
            case 3: return 600 + random.nextFloat() * 600;
            case 4: return 1200 + random.nextFloat() * 400;
            default: return 100;
        }
    }

    @Override
    public void update() {
        updatePowerUsage();
        triggerMalfunction();
        updateDurability();
        checkExplosion();
        checkDeactivation();
    }

    private void triggerMalfunction() {
        float baseChance = 0.12f;
        float levelModifier = 1.0f / reactorLevel;
        float durabilityModifier = 1.0f - durability;
        float totalChance = baseChance * levelModifier * (1.0f + durabilityModifier);

        Random random = new Random();
        isMalfunction = random.nextFloat() < totalChance;
    }

    private void updateDurability(){
        if(isMalfunction){
            durability *= 0.98f;
        } else {
            durability *= 0.997f;
        }
        durability = Math.max(0.0f, durability);
    }

    private void updatePowerUsage(){
        float newUsage = 0;
        for(City city: connectedCities){
            newUsage += city.getEnergyUsage();
        }
        currentPower = newUsage;
    }

    private void checkDeactivation(){
        if(durability < 0.01f){
            durability = 0;
            this.deactivateObject();
        }
    }

    private void checkExplosion() {
        if (isMalfunction && durability < 0.3f && currentPower > maxPower * 0.85) {
            durability = 0;
            this.deactivateObject();

            //Create pollution object
        }
    }

    public void addCity(City city) {
        connectedCities.add(city);
    }

    public void removeCity(City city) {
        connectedCities.remove(city);
    }

    public void info() {
        System.out.printf("\nReaktor %d (Poziom %d):\n" +
                        "   Moc: %.1f/%.1f MW\n" +
                        "   Wytrzymałość: %.1f%%\n" +
                        "   Połączonych miast: %d\n\n",
                this.getId(),
                reactorLevel,
                currentPower,
                maxPower,
                durability * 100,
                connectedCities.size()
        );
    }

    public float getCurrentPower() {
            return currentPower;
    }

    public float getMaxPower() {
            return maxPower;
    }
}
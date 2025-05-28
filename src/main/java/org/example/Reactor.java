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

    public Reactor(int id, float[] coordinates, float maxPower, int level) {
        super(id, coordinates);
        this.connectedCities = new ArrayList<>();
        this.maxPower = maxPower;
        this.reactorLevel = level;
        this.currentPower = 0.0f;
        this.isMalfunction = false;
        this.durability = 1.0f;
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
        //float powerModifier = 1.0f - (currentPower/10000);
        float totalChance = baseChance * levelModifier * (1.0f + durabilityModifier);

        Random random = new Random();
        float prob = random.nextFloat();

        if(prob < totalChance) {
            isMalfunction = true;
        }else{
            isMalfunction = false;
        }
    }

    private void updateDurability(){
        if(isMalfunction){
            durability *= 0.98f;
        }else{
            durability *= 0.997f;
        }

        durability = Math.max(0.0f, durability);
    }

    private void updatePowerUsage(){
        if(connectedCities != null){
            float newUsage = 0;
            for(City city: connectedCities){
                newUsage += city.getEnergyUsage();
            }
            currentPower = newUsage;
        }else{
            currentPower = 0;
        }
    }

    private void checkDeactivation(){
        if(durability<0.01f){
            durability = 0;
            this.deactivateObject();
        }
    }

    private void checkExplosion(){
        if(isMalfunction && durability < 0.3f && currentPower > maxPower * 0.85){
            durability = 0;
            this.deactivateObject();

            //Create pollution object
        }
    }

    public void info() {
        System.out.printf(
                "\n\nReactor %d:\nMoc %.1f/%.1f \nWytrzymałość %.1f%%\n\n",
                this.getId(),
                currentPower,
                maxPower,
                durability * 100
        );
    }

    public float getCurrentPower(){
        return currentPower;
    }

    public float getMaxPower(){
        return maxPower;
    }
}
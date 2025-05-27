package org.example;

import java.util.List;

class Reactor extends MapObject {
    private List<City> connectedCities;
    private float maxPower;
    private float currentPower;
    private boolean isMalfunction;
    private int reactorLevel;
    private float durability;

    public Reactor(int id, float[] coordinates, float maxPower) {
        super(id, coordinates);
        this.maxPower = maxPower;
    }

    @Override
    public void update() {

    }

    public void triggerMalfunction(int reactorLevel, float durability) {
        this.isMalfunction = true;
        this.reactorLevel = reactorLevel;
        this.durability = durability;
    }

    public void update(int reactorLevel, List<City> connectedCities, float maxPower) {
        this.reactorLevel = reactorLevel;
        this.connectedCities = connectedCities;
        this.maxPower = maxPower;
    }


}
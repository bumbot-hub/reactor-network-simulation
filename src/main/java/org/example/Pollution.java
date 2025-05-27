package org.example;

class Pollution {
    private float currentRadius;
    private float targetRadius;
    private float concentration;
    private int duration;
    private Reactor reactor;

    public Pollution(Reactor reactor) {
        this.reactor = reactor;
    }

    public void update(float currentRadius, float targetRadius, String windDirection) {
        this.currentRadius = currentRadius;
        this.targetRadius = targetRadius;
    }

    public float outRadius() {
        return currentRadius;
    }

    public void setRadius(float newRadius) {
        this.currentRadius = newRadius;
    }
}
package org.example;

class Pollution extends MapObject{
    private float currentRadius;
    private float targetRadius;
    private float concentration;
    private int duration;
    private Reactor reactor;

    public Pollution(int id, int[] coordinates, Reactor reactor) {
        super(id, coordinates);
        this.reactor = reactor;
    }

    public void update() {
    }

    public float getRadius() {
        return currentRadius;
    }

    public void setRadius(float newRadius) {
        this.currentRadius = newRadius;
    }
}
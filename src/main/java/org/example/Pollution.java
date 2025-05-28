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

    public void update(String windDirection) {
        if(currentRadius<targetRadius){
            currentRadius += 1;
        }
    }

    public float getRadius() {
        return currentRadius;
    }

    public void setRadius(float newRadius) {
        this.currentRadius = newRadius;
    }
}
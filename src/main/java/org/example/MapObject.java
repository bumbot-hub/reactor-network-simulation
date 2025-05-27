package org.example;

public abstract class MapObject {
    private int id;
    private float[] coordinates;

    public MapObject(int id, float[] coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    public float[] getPosition() {
        return coordinates;
    }

    public abstract void update();
}
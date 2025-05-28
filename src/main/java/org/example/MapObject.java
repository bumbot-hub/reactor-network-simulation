package org.example;

public abstract class MapObject {
    private int id;
    private float[] coordinates;
    private boolean is_active;

    public MapObject(int id, float[] coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        this.is_active = true;
    }

    public abstract void update();

    public float[] getPosition() {
        return coordinates;
    }

    public int getId(){
        return id;
    }

    public void deactivateObject(){
        this.is_active = false;
    }

}
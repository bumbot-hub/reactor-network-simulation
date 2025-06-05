package org.example;

public abstract class MapObject {
    private int id;
    private int[] coordinates;
    private boolean is_active;

    public MapObject(int id, int[] coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        this.is_active = true;
    }

    public abstract void update();

    public int[] getPosition() {
        return coordinates;
    }

    public int getId(){
        return id;
    }

    public boolean checkActivity(){
        return is_active;
    }

    public void deactivateObject(){
        this.is_active = false;
    }

}
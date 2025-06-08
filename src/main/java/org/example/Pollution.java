package org.example;

class Pollution extends MapObject {
    private float radius;
    private float intensity;
    private int age;
    private final int maxAge;

    public Pollution(int id, int[] reactorCoordinates, Reactor sourceReactor) {
        super(id, new int[]{reactorCoordinates[0], reactorCoordinates[1], 1});
        this.radius = 1.0f;
        this.intensity = 100.0f;
        this.age = 0;
        this.maxAge = 10 + (int)(sourceReactor.getMaxPower() / 100);
    }

    @Override
    public void update() {
        if (!checkActivity()){
            return;
        }

        age++;

        if (radius < 15.0f) {
            radius += 0.5f;
        }

        intensity *= 0.95f; // 5% spadek co krok
        affectNearby();

        if (age >= maxAge || intensity < 10.0f) {
            this.deactivateObject();
        }
    }

    private void affectNearby() {
    }

    public float getRadius() {
        return radius;
    }

    public float getIntensity() {
        return intensity;
    }

    public int getAge() {
        return age;
    }
}
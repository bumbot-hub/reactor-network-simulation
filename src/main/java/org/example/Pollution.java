package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Pollution extends MapObject {
    private String windDirection;
    private float radius;
    private final float maxRadius;
    private int age;
    private final int maxAge;
    private int[] windDrift = {0, 0};
    private final TerrainMap mapReference;

    public Pollution(int id, int[] reactorCoordinates, Reactor sourceReactor,
                     String windDirection, TerrainMap mapRef) {
        super(id, new int[]{reactorCoordinates[0], reactorCoordinates[1], 1});
        this.age = 0;
        this.maxAge = 40 + (int)(sourceReactor.getMaxPower() / 100);
        this.windDirection = windDirection;
        this.mapReference = mapRef;

        float reactorPower = sourceReactor.getMaxPower();
        float baseRadius = (float)(0.8 * Math.pow(reactorPower / 100.0, 0.6));
        float initialRadius = Math.max(1.0f, baseRadius * 0.3f);
        this.maxRadius = baseRadius * 9.8f;
        this.radius = initialRadius;
    }

    @Override
    public void update() {
        if (!checkActivity()) {
            return;
        }
        age++;

        if(radius < 15.0f) {
            if(age<=9){
                radius += 4.1f;
            }else{
                radius += 0.82f;
            }
        }

        // PRZEMIESZCZENIE Z WIATREM
        int[] drift = calculateWindDrift(windDirection);
        int[] currentPos = this.getPosition();
        int[] newPos = new int[]{
                currentPos[0] + drift[0],
                currentPos[1] + drift[1],
                currentPos[2]  // Z pozostaje bez zmian
        };

        currentPos = newPos;
        affectNearby();

        if (age >= maxAge) {
            this.deactivateObject();
        }
    }

    public void updateWind(String direction){
        this.windDirection = direction;
    }

    private void affectNearby() {
        if (mapReference == null) return;

        List<City> cities = mapReference.getCities();
        List<Reactor> reactors = mapReference.getReactors();
        int[] pollutionPos = this.getPosition();

        for (City city : cities) {
            if (!city.checkActivity()){
                continue;
            }

            int[] cityPos = city.getPosition();
            double distance = mapReference.calculateDistance(pollutionPos, cityPos);


            if (distance <= radius) {
                float currentPollution = city.getPollutionLevel();
                float newPollution = Math.min(100.0f, currentPollution + maxRadius * 2.0f);
                city.setPollutionLevel(newPollution);
            }
        }

        for(Reactor reactor: reactors){
            if(!reactor.checkActivity()){
                continue;
            }

            int[] reactorPos = reactor.getPosition();
            double distance = mapReference.calculateDistance(pollutionPos, reactorPos);

            if (distance <= radius) {
                reactor.deactivateObject();
            }
        }

    }

    private int[] calculateWindDrift(String windDirection) {
        // Mapowanie kierunków wiatru na wektory jednostkowe
        Map<String, int[]> windVectors = new HashMap<>();
        windVectors.put("N", new int[]{0, 30});
        windVectors.put("NE", new int[]{30, 30});
        windVectors.put("E", new int[]{30, 0});
        windVectors.put("SE", new int[]{30, -30});
        windVectors.put("S", new int[]{0, -30});
        windVectors.put("SW", new int[]{-30, -30});
        windVectors.put("W", new int[]{-30, 0});
        windVectors.put("NW", new int[]{-30, 30});
        windVectors.put("C", new int[]{0, 0});

        int[] baseVector = windVectors.getOrDefault(windDirection, new int[]{0, 0});

        // Siła wiatru rośnie z wiekiem zanieczyszczenia
        double driftStrength = 2.0f;

        int deltaX = (int)(baseVector[0] * driftStrength);
        int deltaY = (int)(baseVector[1] * driftStrength);

        return new int[]{deltaX, deltaY};
    }

    public float getRadius() {
        return radius;
    }
}
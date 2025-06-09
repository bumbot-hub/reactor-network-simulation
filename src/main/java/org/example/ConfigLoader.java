package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance;
    private Properties config;

    private int simulationDuration;
    private int cityGenerationFrequency;
    private int maxCities;
    private int maxReactors;
    private float reactorMalfunctionBaseChance;
    private float reactorDurabilityDecayNormal;
    private float reactorDurabilityDecayMalfunction;
    private float reactorExplosionDurabilityThreshold;
    private float reactorExplosionPowerThreshold;
    private float energyConnectionPowerBuffer;

    private ConfigLoader() {
        loadConfig();
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    private void loadConfig() {
        config = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Nie znaleziono pliku simulation.properties, używam wartości domyślnych");
                setDefaultValues();
                return;
            }

            config.load(input);
            parseConfiguration();

        } catch (IOException e) {
            System.err.println("Błąd wczytywania konfiguracji: " + e.getMessage());
            setDefaultValues();
        }
    }

    private void parseConfiguration() {
        simulationDuration = getIntProperty("simulation.duration", 100);
        cityGenerationFrequency = getIntProperty("simulation.city.generation.frequency", 3);
        maxCities = getIntProperty("limits.cities.max", 0);
        maxReactors = getIntProperty("limits.reactors.max", 0);
        reactorMalfunctionBaseChance = getFloatProperty("reactor.malfunction.base.chance", 0.12f);
        reactorDurabilityDecayNormal = getFloatProperty("reactor.durability.decay.normal", 0.985f);
        reactorDurabilityDecayMalfunction = getFloatProperty("reactor.durability.decay.malfunction", 0.94f);
        reactorExplosionDurabilityThreshold = getFloatProperty("reactor.explosion.durability.threshold", 0.3f);
        reactorExplosionPowerThreshold = getFloatProperty("reactor.explosion.power.threshold", 0.85f);
        energyConnectionPowerBuffer = getFloatProperty("energy.connection.power.buffer", 1.05f);
    }

    private int getIntProperty(String key, int defaultValue) {
        String value = config.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.err.printf("Błędna wartość dla %s: %s, używam domyślnej: %d%n", key, value, defaultValue);
            }
        }
        return defaultValue;
    }

    private float getFloatProperty(String key, float defaultValue) {
        String value = config.getProperty(key);
        if (value != null) {
            try {
                return Float.parseFloat(value.trim());
            } catch (NumberFormatException e) {
                System.err.printf("Błędna wartość dla %s: %s, używam domyślnej: %.3f%n", key, value, defaultValue);
            }
        }
        return defaultValue;
    }

    private void setDefaultValues() {
        // Ustawia wszystkie wartości domyślne
        simulationDuration = 100;
        cityGenerationFrequency = 3;
        maxCities = 0;
        maxReactors = 0;
        reactorMalfunctionBaseChance = 0.12f;
        reactorDurabilityDecayNormal = 0.985f;
        reactorDurabilityDecayMalfunction = 0.94f;
        reactorExplosionDurabilityThreshold = 0.3f;
        reactorExplosionPowerThreshold = 0.85f;
        energyConnectionPowerBuffer = 1.05f;
    }


    public int getSimulationDuration() {
        return simulationDuration;
    }

    public int getCityGenerationFrequency() {
        return cityGenerationFrequency;
    }

    public int getMaxCities() {
        return maxCities;
    }

    public int getMaxReactors() {
        return maxReactors;
    }

    public float getReactorMalfunctionBaseChance() {
        return reactorMalfunctionBaseChance;
    }

    public float getReactorDurabilityDecayNormal() {
        return reactorDurabilityDecayNormal;
    }

    public float getReactorDurabilityDecayMalfunction() {
        return reactorDurabilityDecayMalfunction;
    }

    public float getReactorExplosionDurabilityThreshold() {
        return reactorExplosionDurabilityThreshold;
    }

    public float getReactorExplosionPowerThreshold() {
        return reactorExplosionPowerThreshold;
    }

    public float getEnergyConnectionPowerBuffer() {
        return energyConnectionPowerBuffer;
    }
}
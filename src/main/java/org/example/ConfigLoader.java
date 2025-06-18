package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa implementująca wzorzec Singleton do ładowania i zarządzania konfiguracją symulacji.
 * Wczytuje parametry z pliku `config.properties`. W przypadku braku pliku lub błędów
 * w jego zawartości, używa wartości domyślnych.
 */
public class ConfigLoader {
    /** Statyczna instancja singletona. */
    private static ConfigLoader instance;
    /** Obiekt przechowujący wczytane właściwości konfiguracyjne. */
    private Properties config;

    // --- Parametry konfiguracyjne ---
    /** Czas trwania symulacji (w krokach). */
    private int simulationDuration;
    /** Częstotliwość generowania nowych miast (co ile kroków). */
    private int cityGenerationFrequency;
    /** Maksymalna dozwolona liczba miast. */
    private int maxCities;
    /** Maksymalna dozwolona liczba reaktorów. */
    private int maxReactors;
    /** Bazowa szansa na wystąpienie awarii reaktora. */
    private float reactorMalfunctionBaseChance;
    /** Współczynnik spadku wytrzymałości reaktora w trybie normalnym. */
    private float reactorDurabilityDecayNormal;
    /** Współczynnik spadku wytrzymałości reaktora w trybie awarii. */
    private float reactorDurabilityDecayMalfunction;
    /** Próg wytrzymałości, poniżej którego może nastąpić eksplozja. */
    private float reactorExplosionDurabilityThreshold;
    /** Próg mocy, powyżej którego może nastąpić eksplozja. */
    private float reactorExplosionPowerThreshold;
    /** Bufor mocy wymagany do podłączenia miasta do reaktora. */
    private float energyConnectionPowerBuffer;

    /**
     * Prywatny konstruktor, aby zapobiec tworzeniu instancji z zewnątrz.
     * Inicjuje proces ładowania konfiguracji.
     */
    private ConfigLoader() {
        loadConfig();
    }

    /**
     * Zwraca jedyną instancję klasy ConfigLoader (Singleton).
     *
     * @return instancja ConfigLoader
     */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    /**
     * Wczytuje konfigurację z pliku `config.properties` znajdującego się w zasobach.
     * W przypadku niepowodzenia, wywołuje metodę ustawiającą wartości domyślne.
     */
    private void loadConfig() {
        config = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Nie znaleziono pliku config.properties, używam wartości domyślnych");
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

    /**
     * Parsuje wczytane właściwości i przypisuje je do pól klasy.
     */
    private void parseConfiguration() {
        simulationDuration = getIntProperty("simulation.duration", 100);
        cityGenerationFrequency = getIntProperty("simulation.city.generation.frequency", 3);
        maxCities = getIntProperty("limits.cities.max", 10);
        maxReactors = getIntProperty("limits.reactors.max", 5);
        reactorMalfunctionBaseChance = getFloatProperty("reactor.malfunction.base.chance", 0.12f);
        reactorDurabilityDecayNormal = getFloatProperty("reactor.durability.decay.normal", 0.985f);
        reactorDurabilityDecayMalfunction = getFloatProperty("reactor.durability.decay.malfunction", 0.94f);
        reactorExplosionDurabilityThreshold = getFloatProperty("reactor.explosion.durability.threshold", 0.3f);
        reactorExplosionPowerThreshold = getFloatProperty("reactor.explosion.power.threshold", 0.85f);
        energyConnectionPowerBuffer = getFloatProperty("energy.connection.power.buffer", 1.05f);
    }

    /**
     * Pobiera wartość typu int z konfiguracji. Jeśli klucz nie istnieje lub wartość
     * jest nieprawidłowa, zwraca wartość domyślną.
     *
     * @param key klucz właściwości
     * @param defaultValue wartość domyślna
     * @return wartość typu int
     */
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

    /**
     * Pobiera wartość typu float z konfiguracji. Jeśli klucz nie istnieje lub wartość
     * jest nieprawidłowa, zwraca wartość domyślną.
     *
     * @param key klucz właściwości
     * @param defaultValue wartość domyślna
     * @return wartość typu float
     */
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

    /**
     * Ustawia domyślne wartości wszystkich parametrów konfiguracyjnych.
     * Wywoływana w przypadku problemów z wczytaniem pliku konfiguracyjnego.
     */
    private void setDefaultValues() {
        simulationDuration = 100;
        cityGenerationFrequency = 3;
        maxCities = 10;
        maxReactors = 5;
        reactorMalfunctionBaseChance = 0.12f;
        reactorDurabilityDecayNormal = 0.985f;
        reactorDurabilityDecayMalfunction = 0.94f;
        reactorExplosionDurabilityThreshold = 0.3f;
        reactorExplosionPowerThreshold = 0.85f;
        energyConnectionPowerBuffer = 1.05f;
    }

    public int getSimulationDuration() { return simulationDuration; }
    public int getCityGenerationFrequency() { return cityGenerationFrequency; }
    public int getMaxCities() { return maxCities; }
    public int getMaxReactors() { return maxReactors; }
    public float getReactorMalfunctionBaseChance() { return reactorMalfunctionBaseChance; }
    public float getReactorDurabilityDecayNormal() { return reactorDurabilityDecayNormal; }
    public float getReactorDurabilityDecayMalfunction() { return reactorDurabilityDecayMalfunction; }
    public float getReactorExplosionDurabilityThreshold() { return reactorExplosionDurabilityThreshold; }
    public float getReactorExplosionPowerThreshold() { return reactorExplosionPowerThreshold; }
    public float getEnergyConnectionPowerBuffer() { return energyConnectionPowerBuffer; }
}

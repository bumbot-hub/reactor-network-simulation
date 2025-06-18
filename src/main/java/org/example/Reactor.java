package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa reprezentująca reaktor jądrowy na mapie symulacji.
 * Odpowiada za produkcję energii, zarządzanie podłączonymi miastami,
 * symulowanie usterek, zużycia oraz potencjalnych eksplozji.
 */
class Reactor extends MapObject {
    /** Singleton konfiguracji symulacji. */
    private static final ConfigLoader config = ConfigLoader.getInstance();
    /** Lista miast podłączonych do reaktora. */
    private List<City> connectedCities;
    /** Maksymalna moc produkcyjna reaktora (w MW). */
    private final float maxPower;
    /** Aktualne obciążenie reaktora (w MW). */
    private float currentPower;
    /** Flaga informująca, czy reaktor jest w stanie awarii. */
    private boolean isMalfunction;
    /** Poziom technologiczny reaktora (wpływa na moc i ryzyko awarii). */
    private final int reactorLevel;
    /** Wytrzymałość reaktora (wartość od 0.0 do 1.0). */
    private float durability;
    /** Referencja do mapy terenu, na której znajduje się reaktor. */
    private final TerrainMap mapRefference;

    /**
     * Konstruktor obiektu Reactor.
     * Inicjalizuje reaktor z podanym ID, współrzędnymi, poziomem i odniesieniem do mapy.
     *
     * @param id identyfikator reaktora
     * @param coordinates współrzędne reaktora [x, y, z]
     * @param level poziom reaktora (1-4)
     * @param mapRefference odniesienie do obiektu mapy terenu
     */
    public Reactor(int id, int[] coordinates, int level, TerrainMap mapRefference) {
        super(id, coordinates);
        this.connectedCities = new ArrayList<>();
        this.reactorLevel = level;
        this.mapRefference = mapRefference;
        this.maxPower = calculateMaxPower(level);
        this.currentPower = 0.0f;
        this.isMalfunction = false;
        this.durability = 1.0f;
    }

    /**
     * Oblicza maksymalną moc reaktora na podstawie jego poziomu.
     * Moc jest losowana w predefiniowanych zakresach dla każdego poziomu.
     *
     * @param level poziom reaktora (1-4)
     * @return maksymalna moc produkcyjna reaktora (w MW)
     */
    private float calculateMaxPower(int level) {
        Random random = new Random();
        switch(level) {
            case 1: return 50 + random.nextFloat() * 150;
            case 2: return 200 + random.nextFloat() * 400;
            case 3: return 600 + random.nextFloat() * 600;
            case 4: return 1200 + random.nextFloat() * 400;
            default: return 100;
        }
    }

    /**
     * Aktualizuje stan reaktora w każdym kroku symulacji.
     * Wywołuje metody odpowiedzialne za pobór mocy, usterki, zużycie i eksplozje.
     */
    @Override
    public void update() {
        updatePowerUsage();
        triggerMalfunction();
        updateDurability();
        checkExplosion();
        checkDeactivation();
    }

    /**
     * Oblicza prawdopodobieństwo i wywołuje usterkę reaktora.
     * Szansa na awarię zależy od poziomu reaktora i jego wytrzymałości.
     */
    private void triggerMalfunction() {
        float baseChance = config.getReactorMalfunctionBaseChance();
        float levelModifier = 1.0f / reactorLevel;
        float durabilityModifier = 1.0f - durability;
        float totalChance = baseChance * levelModifier * (1.0f + durabilityModifier);

        Random random = new Random();
        isMalfunction = random.nextFloat() < totalChance;
    }

    /**
     * Aktualizuje wytrzymałość reaktora.
     * Zużycie jest większe, jeśli reaktor jest w stanie awarii.
     */
    private void updateDurability(){
        if(isMalfunction){
            durability *= config.getReactorDurabilityDecayMalfunction();
        } else {
            durability *= config.getReactorDurabilityDecayNormal();
        }
        durability = Math.max(0.0f, durability);
    }

    /**
     * Aktualizuje całkowite obciążenie reaktora na podstawie zapotrzebowania podłączonych miast.
     */
    private void updatePowerUsage(){
        float newUsage = 0;
        for(City city: connectedCities){
            newUsage += city.getEnergyUsage();
        }
        currentPower = newUsage;
    }

    /**
     * Sprawdza, czy wytrzymałość reaktora spadła do krytycznego poziomu i dezaktywuje go.
     */
    private void checkDeactivation(){
        if(durability < 0.01f){
            durability = 0;
            this.deactivateObject();
        }
    }

    /**
     * Sprawdza warunki do eksplozji reaktora (awaria, niska wytrzymałość, przeciążenie).
     * Jeśli warunki są spełnione, tworzy obiekt zanieczyszczenia.
     */
    private void checkExplosion() {
        if (isMalfunction && durability < config.getReactorExplosionDurabilityThreshold() &&
                currentPower > maxPower * config.getReactorExplosionPowerThreshold()) {
            durability = 0;
            this.deactivateObject();

            Pollution pollution = new Pollution(
                    mapRefference.getPollutions().size() + 1,
                    this.getPosition(),
                    this,
                    mapRefference.getWindDirection(),
                    mapRefference
            );
            mapRefference.addPollution(pollution);
        }
    }

    /**
     * Dodaje miasto do listy podłączonych do reaktora.
     *
     * @param city obiekt miasta do dodania
     */
    public void addCity(City city) {
        connectedCities.add(city);
    }

    /**
     * Usuwa miasto z listy podłączonych do reaktora.
     *
     * @param city obiekt miasta do usunięcia
     */
    public void removeCity(City city) {
        connectedCities.remove(city);
    }

    /**
     * Wyświetla informacje o stanie reaktora na konsoli.
     */
    public void info() {
        System.out.printf("\nReaktor %d (Poziom %d):\n" +
                        "   Moc: %.1f/%.1f MW\n" +
                        "   Wytrzymałość: %.1f%%\n" +
                        "   Połączonych miast: %d\n\n",
                this.getId(),
                reactorLevel,
                currentPower,
                maxPower,
                durability * 100,
                connectedCities.size()
        );
    }

    /**
     * Zwraca aktualne obciążenie reaktora.
     *
     * @return aktualne zużycie mocy (w MW)
     */
    public float getCurrentPower() {
        return currentPower;
    }

    /**
     * Zwraca maksymalną moc produkcyjną reaktora.
     *
     * @return maksymalna moc reaktora (w MW)
     */
    public float getMaxPower() {
        return maxPower;
    }
}

package org.example;

import java.util.Random;

/**
 * Klasa reprezentująca miasto w symulacji.
 * Zarządza swoją populacją, zapotrzebowaniem na energię, poziomem skażenia
 * oraz połączeniem z reaktorem.
 */
public class City extends MapObject {
    /** Aktualna populacja miasta. */
    private int population;
    /** Początkowa populacja miasta, używana do obliczeń. */
    private final int originalPopulation;
    /** Zapotrzebowanie miasta na energię (w MW). */
    private float energyUsage;
    /** Poziom skażenia w mieście (w procentach). */
    private float pollutionLevel;
    /** Reaktor, z którym miasto jest połączone. */
    private Reactor reactor;
    /** Stała określająca zużycie energii na jednego mieszkańca. */
    private static final float ENERGY_PER_PERSON = 0.0005f;

    /**
     * Konstruktor obiektu City.
     * Inicjalizuje miasto z podanym ID, współrzędnymi i populacją.
     *
     * @param id identyfikator miasta
     * @param coordinates współrzędne miasta [x, y, z]
     * @param population początkowa populacja
     */
    public City(int id, int[] coordinates, int population) {
        super(id, coordinates);
        this.population = population;
        this.originalPopulation = population;
        this.pollutionLevel = 0;
        updateEnergyUsage();
    }

    /**
     * Aktualizuje stan miasta w każdym kroku symulacji.
     * Wywołuje metody do aktualizacji populacji i zapotrzebowania na energię.
     */
    @Override
    public void update() {
        updatePopulation();
        updateEnergyUsage();
    }

    /**
     * Aktualizuje populację miasta na podstawie poziomu skażenia i losowych czynników.
     * W przypadku wysokiego skażenia populacja spada. W przeciwnym razie może losowo rosnąć lub maleć.
     */
    private void updatePopulation() {
        Random random = new Random();
        float prob = random.nextFloat();

        int newPopulation = population;

        if(pollutionLevel >= 20){
            double reduction = 1.0 - (pollutionLevel / 100.0) * 0.75;
            population *= (int) reduction;
        }else{
            if(prob >= 0.35) {
                float growthRate = 0.001f + random.nextFloat() * 0.03f;
                newPopulation += (int)(population * growthRate);
            } else if(prob <= 0.1) {
                float declineRate = 0.001f + random.nextFloat() * 0.005f;
                newPopulation -= (int)(population * declineRate);
            }
        }

        int minimumPopulation = (int)(originalPopulation * 0.9);
        if (newPopulation < minimumPopulation) {
            newPopulation = minimumPopulation;
        }

        this.population = newPopulation;
    }

    /**
     * Łączy miasto z podanym reaktorem.
     *
     * @param reactor reaktor, z którym miasto ma nawiązać połączenie
     */
    public void connectWithReactor(Reactor reactor){
        this.reactor = reactor;
    }

    /**
     * Aktualizuje zapotrzebowanie na energię na podstawie bieżącej populacji.
     */
    private void updateEnergyUsage() {
        energyUsage = population * ENERGY_PER_PERSON;
    }

    /**
     * Wyświetla szczegółowe informacje o stanie miasta na konsoli.
     */
    public void info(){
        System.out.printf("\n Miasto %d:\n" +
                        "   Populacja: %,d (oryginalnie: %,d)\n" +
                        "   Zapotrzebowanie: %.1f MW\n" +
                        "   Skażenie: %.2f%%\n" +
                        "   Reaktor: %s\n\n",
                this.getId(),
                population,
                originalPopulation,
                energyUsage,
                pollutionLevel,
                (reactor != null && reactor.checkActivity()) ?
                        String.format("#%d (%.1f MW)", reactor.getId(), reactor.getMaxPower()) : "BRAK"
        );
    }

    /**
     * Zwraca aktualną populację miasta.
     *
     * @return populacja miasta
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Zwraca początkową populację miasta.
     *
     * @return oryginalna populacja
     */
    public int getOriginalPopulation(){
        return originalPopulation;
    }

    /**
     * Zwraca aktualne zapotrzebowanie miasta na energię.
     *
     * @return zapotrzebowanie na energię (w MW)
     */
    public float getEnergyUsage() {
        return energyUsage;
    }

    /**
     * Zwraca aktualny poziom skażenia w mieście.
     *
     * @return poziom skażenia (w procentach)
     */
    public float getPollutionLevel() {
        return pollutionLevel;
    }

    /**
     * Zwraca reaktor, z którym miasto jest połączone.
     *
     * @return obiekt reaktora lub null, jeśli brak połączenia
     */
    public Reactor getReactor() {
        return reactor;
    }

    /**
     * Ustawia nowy poziom skażenia dla miasta.
     *
     * @param level nowy poziom skażenia
     */
    public void setPollutionLevel(float level) {
        pollutionLevel = level;
    }
}

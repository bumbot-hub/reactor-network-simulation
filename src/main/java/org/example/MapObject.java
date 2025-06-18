package org.example;

/**
 * Abstrakcyjna klasa bazowa dla wszystkich obiektów na mapie.
 * Definiuje wspólne właściwości, takie jak ID, współrzędne i stan aktywności.
 */
public abstract class MapObject {
    /** Unikalny identyfikator obiektu. */
    private int id;
    /** Współrzędne obiektu w formacie [x, y, z]. */
    private int[] coordinates;
    /** Flaga określająca, czy obiekt jest aktywny. */
    private boolean is_active;

    /**
     * Konstruktor klasy MapObject.
     * Tworzy nowy obiekt z podanym ID i współrzędnymi.
     *
     * @param id unikalny identyfikator obiektu
     * @param coordinates tablica współrzędnych [x, y, z]
     * @throws IllegalArgumentException jeśli tablica współrzędnych nie ma długości 3
     */
    public MapObject(int id, int[] coordinates) {
        this.id = id;
        if (coordinates.length != 3) {
            throw new IllegalArgumentException("Coordinates must be [x, y, z]");
        }
        this.coordinates = coordinates.clone();
        this.is_active = true;
    }

    /**
     * Abstrakcyjna metoda aktualizująca stan obiektu w każdym kroku symulacji.
     * Musi być zaimplementowana przez klasy dziedziczące.
     */
    public abstract void update();

    /**
     * Zwraca współrzędne obiektu.
     *
     * @return tablica współrzędnych [x, y, z]
     */
    public int[] getPosition() {
        return coordinates;
    }

    /**
     * Zwraca identyfikator obiektu.
     *
     * @return unikalny identyfikator (ID)
     */
    public int getId(){
        return id;
    }

    /**
     * Sprawdza, czy obiekt jest aktywny.
     *
     * @return true, jeśli obiekt jest aktywny, w przeciwnym razie false
     */
    public boolean checkActivity(){
        return is_active;
    }

    /**
     * Dezaktywuje obiekt, ustawiając jego stan na nieaktywny.
     */
    public void deactivateObject(){
        this.is_active = false;
    }

}

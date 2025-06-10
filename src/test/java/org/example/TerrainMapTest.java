package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TerrainMapTest {

    @Test
    void constructor_throwsForInvalidMapSize() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new TerrainMap(new int[]{100, 100}, 5, 5));
        assertEquals("Nieprawidłowe wymiary mapy", ex.getMessage());
    }

    @Test
    void addCity_addsCityToList() {
        TerrainMap map = new TerrainMap(new int[]{500, 500}, 5, 5);
        City city = new City(1, new int[]{100, 100, 0}, 10000);
        map.addCity(city);
        assertEquals(1, map.getCities().size());
        assertEquals(city, map.getCities().get(0));
    }

    @Test
    void calculateDistance_returnsCorrectValue() {
        TerrainMap map = new TerrainMap(new int[]{500, 500}, 5, 5);
        double dist = map.calculateDistance(new int[]{0, 0, 0}, new int[]{3, 4, 0});
        assertEquals(5.0, dist, 0.001);
    }
}

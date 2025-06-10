package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapObjectTest {

    @Test
    void deactivateObject_setsInactive() {
        City city = new City(1, new int[]{100, 200, 0}, 10000);
        assertTrue(city.checkActivity());
        city.deactivateObject();
        assertFalse(city.checkActivity());
    }

    @Test
    void constructor_throwsForInvalidCoordinates() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new City(1, new int[]{1, 2}, 1000));
        assertEquals("Coordinates must be [x, y, z]", ex.getMessage());
    }
}

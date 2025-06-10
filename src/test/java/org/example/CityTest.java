package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void cityInitialization_setsCorrectValues() {
        City city = new City(1, new int[]{100, 200, 0}, 20000);
        assertEquals(1, city.getId());
        assertArrayEquals(new int[]{100, 200, 0}, city.getPosition());
        assertEquals(20000, city.getPopulation());
        assertEquals(20000, city.getOriginalPopulation());
        assertEquals(0, city.getPollutionLevel());
        assertTrue(city.getEnergyUsage() > 0);
        assertNull(city.getReactor());
    }

    @Test
    void setPollutionLevel_setsValue() {
        City city = new City(1, new int[]{100, 200, 0}, 20000);
        city.setPollutionLevel(50.5f);
        assertEquals(50.5f, city.getPollutionLevel());
    }

    @Test
    void connectWithReactor_setsReactor() {
        City city = new City(1, new int[]{100, 200, 0}, 20000);
        Reactor reactor = new Reactor(1, new int[]{150, 150, 0}, 1, null);
        city.connectWithReactor(reactor);
        assertEquals(reactor, city.getReactor());
    }
}

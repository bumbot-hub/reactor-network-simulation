package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReactorTest {

    @Test
    void reactorInitialization_setsCorrectValues() {
        Reactor reactor = new Reactor(1, new int[]{150, 150, 0}, 2, null);
        assertEquals(1, reactor.getId());
        assertArrayEquals(new int[]{150, 150, 0}, reactor.getPosition());
        assertTrue(reactor.getMaxPower() >= 200);
        assertEquals(0, reactor.getCurrentPower());
        assertTrue(reactor.checkActivity());
    }

    @Test
    void addAndRemoveCity_modifiesConnectedCities() {
        Reactor reactor = new Reactor(1, new int[]{150, 150, 0}, 1, null);
        City city = new City(2, new int[]{120, 120, 0}, 10000);
        reactor.addCity(city);
        assertTrue(reactor.getCurrentPower() >= 0); // moc się aktualizuje
        reactor.removeCity(city);
        // Po usunięciu moc powinna być 0
        assertEquals(0, reactor.getCurrentPower());
    }
}
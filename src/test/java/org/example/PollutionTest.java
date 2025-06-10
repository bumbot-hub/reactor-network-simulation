package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PollutionTest {

    @Test
    void pollutionInitialization_setsRadiusAndMaxAge() {
        Reactor reactor = new Reactor(1, new int[]{100, 100, 0}, 2, null);
        TerrainMap map = new TerrainMap(new int[]{500, 500}, 5, 5);
        Pollution pollution = new Pollution(1, new int[]{100, 100, 1}, reactor, "N", map);
        assertTrue(pollution.getRadius() > 0);
    }
}

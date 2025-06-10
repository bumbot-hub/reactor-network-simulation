package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    @Test
    void getInstance_returnsSingleton() {
        ConfigLoader c1 = ConfigLoader.getInstance();
        ConfigLoader c2 = ConfigLoader.getInstance();
        assertSame(c1, c2);
    }

    @Test
    void defaultValues_areSetIfNoConfig() {
        ConfigLoader config = ConfigLoader.getInstance();
        assertTrue(config.getSimulationDuration() > 0);
        assertTrue(config.getCityGenerationFrequency() > 0);
    }
}
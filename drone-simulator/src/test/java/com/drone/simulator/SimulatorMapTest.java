package com.drone.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SimulatorMapTest {

    @Test
    public void testWrapCoordinate() {
        SimulatorMap map = new SimulatorMap(5, 5);

        assertEquals(4, map.wrapCoordinate(-1, 5));
        assertEquals(0, map.wrapCoordinate(5, 5));
        assertEquals(2, map.wrapCoordinate(2, 5));
    }

    @Test
    public void testObstacleAndDronePlacement() {
        SimulatorMap map = new SimulatorMap(5, 5);
        Drone drone = new Drone(0, 1, 1, 'E', "M");

        map.placeDrone(drone);
        assertEquals('>', map.getGrid()[1][1]);

        map.removeDrone(drone);
        assertEquals('.', map.getGrid()[1][1]);

        map.getGrid()[2][2] = '#';
        assertTrue(map.isBlocked(2, 2));
        assertFalse(map.isBlocked(1, 1));
    }
}

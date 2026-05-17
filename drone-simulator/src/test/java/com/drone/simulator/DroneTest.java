package com.drone.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DroneTest {

    @Test
    public void testDroneTurnAndMove() {
        SimulatorMap map = new SimulatorMap(5, 5);
        Drone drone = new Drone(0, 1, 2, 'N', "LMLMLMLMM");

        drone.turnLeft();
        assertEquals('W', drone.getOrientation());

        drone.turnRight();
        assertEquals('N', drone.getOrientation());

        drone.moveForward(map);
        assertEquals(1, drone.getX());
        assertEquals(3, drone.getY());

        drone.moveBackward(map);
        assertEquals(1, drone.getX());
        assertEquals(2, drone.getY());
    }

    @Test
    public void testDroneExecuteCommandSequence() {
        SimulatorMap map = new SimulatorMap(5, 5);
        Drone drone = new Drone(0, 0, 0, 'N', "MRM");

        for (char cmd : drone.getCommands().toCharArray()) {
            drone.executeCommand(cmd, map);
        }

        assertEquals(1, drone.getX());
        assertEquals(1, drone.getY());
        assertEquals('E', drone.getOrientation());
    }
}

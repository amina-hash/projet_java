package com.drone.simulator.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

import com.drone.simulator.Drone;
import com.drone.simulator.SimulatorMap;

public class DroneWorkerTest {

    @Test
    public void testDroneWorkerExecutesCommand() throws InterruptedException {
        SimulatorMap map = new SimulatorMap(5, 5);
        Drone drone = new Drone(0, 0, 0, 'E', "M");
        map.placeDrone(drone);
        map.addDroneToList(drone);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        DroneWorker worker = new DroneWorker(drone, map, lock, condition);
        worker.start();
        worker.join();

        assertEquals(1, drone.getX());
        assertEquals(0, drone.getY());
    }
}

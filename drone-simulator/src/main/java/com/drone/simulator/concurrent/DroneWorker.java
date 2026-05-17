package com.drone.simulator.concurrent;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.drone.simulator.Drone;
import com.drone.simulator.SimulatorMap;

public class DroneWorker extends Thread {

    private Drone drone;
    private SimulatorMap map;
    private ReentrantLock lock;
    private Condition condition;

    // Constructeur
    public DroneWorker(Drone drone, SimulatorMap map, 
                       ReentrantLock lock, Condition condition) {
        this.drone = drone;
        this.map = map;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        // Exécuter chaque commande du drone
        for (char cmd : drone.getCommands().toCharArray()) {

            // Protéger l'accès à la carte
            synchronized (map) {
                // Effacer l'ancienne position
                map.removeDrone(drone);

                // Exécuter la commande
                drone.executeCommand(cmd, map);

                // Placer le drone à sa nouvelle position
                map.placeDrone(drone);
            }

            // Notifier le thread d'affichage
            lock.lock();
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }

            // Attendre un peu entre chaque commande
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Getter
    public Drone getDrone() { return drone; }
}
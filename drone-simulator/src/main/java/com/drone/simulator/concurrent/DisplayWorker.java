package com.drone.simulator.concurrent;

// Importer les classes du package principal
import com.drone.simulator.Drone;
import com.drone.simulator.SimulatorMap;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.drone.simulator.Drone;
import com.drone.simulator.SimulatorMap;

public class DisplayWorker extends Thread {

    private SimulatorMap map;
    private volatile boolean running;
    private ReentrantLock lock;
    private Condition condition;

    // Constructeur
    public DisplayWorker(SimulatorMap map, ReentrantLock lock, Condition condition) {
        this.map = map;
        this.lock = lock;
        this.condition = condition;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            lock.lock();
            try {
                // Attendre qu'un drone notifie
                condition.await();

                // Effacer l'écran
                clearScreen();

                // Afficher les infos en haut
                System.out.println("================================");
                System.out.println("  Simulateur de Drones - V2   ");
                System.out.println("================================");

                // Afficher la carte
                map.displayMap();

                // Afficher les positions actuelles des drones
                System.out.println("Positions actuelles :");
                for (Drone drone : map.getDrones()) {
                    System.out.println("  Drone " + drone.getId() +
                                     " : " + drone.getPosition());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    // Arrêter le thread d'affichage
    public void stopDisplay() {
        running = false;
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Effacer l'écran
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
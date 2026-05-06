package com.drone.simulator.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.drone.simulator.Drone;
import com.drone.simulator.FileReader;
import com.drone.simulator.SimulatorMap;

public class SimulatorConcurrent {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("  Simulateur de Drones - V2   ");
        System.out.println("================================");

        // Étape 1 : Lire le fichier de configuration
        FileReader fileReader = new FileReader();
        fileReader.readConfiguration("drones.txt");

        int width = fileReader.getMapWidth();
        int height = fileReader.getMapHeight();
        List<Drone> drones = fileReader.getDrones();

        // Étape 2 : Créer la carte
        SimulatorMap map = new SimulatorMap(width, height);

        // Étape 3 : Récupérer les positions initiales des drones
        int[] dronesX = new int[drones.size()];
        int[] dronesY = new int[drones.size()];
        for (int i = 0; i < drones.size(); i++) {
            dronesX[i] = drones.get(i).getX();
            dronesY[i] = drones.get(i).getY();
        }

        // Étape 4 : Ajouter les obstacles
        map.addObstacles(20, dronesX, dronesY);

        // Étape 5 : Placer les drones sur la carte
for (Drone drone : drones) {
    map.placeDrone(drone);
    map.addDroneToList(drone);
}
        // Étape 6 : Créer le lock et la condition
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        // Étape 7 : Créer et démarrer le thread d'affichage
        DisplayWorker displayWorker = new DisplayWorker(map, lock, condition);
        displayWorker.start();

        // Étape 8 : Créer et démarrer un thread par drone
        List<DroneWorker> droneWorkers = new ArrayList<>();
        for (Drone drone : drones) {
            DroneWorker worker = new DroneWorker(drone, map, lock, condition);
            droneWorkers.add(worker);
            worker.start();
        }

        // Étape 9 : Attendre que tous les drones terminent
        for (DroneWorker worker : droneWorkers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Étape 10 : Arrêter le thread d'affichage
        displayWorker.stopDisplay();

        // Étape 11 : Afficher les positions finales
        System.out.println("\nPositions finales des drones:");
        System.out.println("================================");
        for (Drone drone : drones) {
            System.out.println("Drone " + drone.getId() +
                             " : " + drone.getPosition());
        }
    }
}
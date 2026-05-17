package com.drone.simulator.server;

import com.drone.simulator.Drone;
import com.drone.simulator.SimulatorMap;
import com.drone.simulator.concurrent.DroneWorker;
import com.drone.simulator.concurrent.DisplayWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerConnectionHandler implements Runnable {

    private Socket socket;

    // Constructeur
    public ServerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true)) {

            System.out.println("Réception de la configuration...");

            // Étape 1 : Recevoir la taille de la carte
            String mapDimensions = input.readLine();
            String[] dims = mapDimensions.split(" ");
            int width  = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);
            System.out.println("Carte : " + width + " x " + height);

            // Étape 2 : Recevoir le nombre de drones
            int numDrones = Integer.parseInt(input.readLine());
            System.out.println("Nombre de drones : " + numDrones);

            // Étape 3 : Recevoir les données de chaque drone
            List<Drone> drones = new ArrayList<>();
            for (int i = 0; i < numDrones; i++) {
                String droneLine = input.readLine();
                Drone drone = parseDroneData(i, droneLine);
                drones.add(drone);
                System.out.println("Drone " + i + " reçu : " + 
                                 drone.getPosition() + 
                                 " | Commandes : " + drone.getCommands());
            }

            // Étape 4 : Créer la carte
            SimulatorMap map = new SimulatorMap(width, height);

            // Étape 5 : Récupérer positions initiales
            int[] dronesX = new int[drones.size()];
            int[] dronesY = new int[drones.size()];
            for (int i = 0; i < drones.size(); i++) {
                dronesX[i] = drones.get(i).getX();
                dronesY[i] = drones.get(i).getY();
            }

            // Étape 6 : Ajouter obstacles et placer drones
            map.addObstacles(20, dronesX, dronesY);
            for (Drone drone : drones) {
                map.placeDrone(drone);
                map.addDroneToList(drone);
            }

            // Étape 7 : Exécuter la simulation concurrente
            executeSimulation(drones, map);

            // Étape 8 : Envoyer les résultats au client
            output.println("SUCCESS");
            for (Drone drone : drones) {
                output.println(drone.getX() + " " + 
                             drone.getY() + " " + 
                             drone.getOrientation());
            }
            System.out.println("Résultats envoyés au client !");

        } catch (IOException e) {
            System.out.println("Erreur connexion : " + e.getMessage());
        }
    }

    // Exécuter la simulation concurrente
    private void executeSimulation(List<Drone> drones, SimulatorMap map) {

        // Créer lock et condition
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        // Démarrer le thread d'affichage
        DisplayWorker displayWorker = new DisplayWorker(map, lock, condition);
        displayWorker.start();

        // Démarrer un thread par drone
        List<DroneWorker> droneWorkers = new ArrayList<>();
        for (Drone drone : drones) {
            DroneWorker worker = new DroneWorker(drone, map, lock, condition);
            droneWorkers.add(worker);
            worker.start();
        }

        // Attendre que tous les drones terminent
        for (DroneWorker worker : droneWorkers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Arrêter le thread d'affichage
        displayWorker.stopDisplay();

        System.out.println("\nSimulation terminée !");
        System.out.println("Positions finales :");
        for (Drone drone : drones) {
            System.out.println("Drone " + drone.getId() + 
                             " : " + drone.getPosition());
        }
    }

    // Parser une ligne de drone
    private Drone parseDroneData(int id, String line) {
        String[] parts = line.split(" ");
        int x            = Integer.parseInt(parts[0]);
        int y            = Integer.parseInt(parts[1]);
        char orientation = parts[2].charAt(0);
        String commands  = parts[3];
        return new Drone(id, x, y, orientation, commands);
    }
}
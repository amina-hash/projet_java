package com.drone.simulator;

import java.util.List;

public class SimulatorBasic {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("  Simulateur de Drones - V1   ");
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
        map.addObstacles(10, dronesX, dronesY);

        // Étape 5 : Placer les drones sur la carte
        for (Drone drone : drones) {
            map.placeDrone(drone);
        }

        // Étape 6 : Afficher la carte initiale
        System.out.println("\nCarte initiale :");
        map.displayMap();

        // Étape 7 : Exécuter les commandes de chaque drone
        for (Drone drone : drones) {

            System.out.println("\nDrone " + drone.getId() + 
                             " : Position initiale : " + drone.getPosition() + 
                             " | Commandes : " + drone.getCommands());
            System.out.println("===========================================");

            for (char cmd : drone.getCommands().toCharArray()) {

                // Effacer l'ancienne position du drone
                map.removeDrone(drone);

                // Exécuter la commande
                drone.executeCommand(cmd, map);

                // Placer le drone à sa nouvelle position
                map.placeDrone(drone);

                // Afficher la position actuelle
                System.out.println("Position du drone " + drone.getId() + 
                                 " : " + drone.getPosition());

                // Afficher la carte
               
                map.displayMap();

                // Attendre 500ms avant la prochaine commande
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Étape 8 : Afficher les positions finales
        System.out.println("\nPositions finales des drones:");
        System.out.println("================================");
        for (Drone drone : drones) {
            System.out.println("Drone " + drone.getId() + 
                             " : " + drone.getPosition());
        }
    }
}
package com.drone.simulator.client;

import com.drone.simulator.Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class DroneClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("  Simulateur de Drones - V3   ");
        System.out.println("      Mode Client              ");
        System.out.println("================================");

        // Lire la configuration depuis le fichier
       
        FileConfigurationProvider config = 
            new FileConfigurationProvider("drones_client.txt");

        List<Drone> drones = config.getDrones();

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter output = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connecté au serveur " + HOST + ":" + PORT);

            // Étape 1 : Envoyer la taille de la carte
            output.println(config.getMapWidth() + " " + config.getMapHeight());

            // Étape 2 : Envoyer le nombre de drones
            output.println(drones.size());

            // Étape 3 : Envoyer les données de chaque drone
            for (Drone drone : drones) {
                String droneData = drone.getX() + " " +
                                 drone.getY() + " " +
                                 drone.getOrientation() + " " +
                                 drone.getCommands();
                output.println(droneData);
                System.out.println("Drone " + drone.getId() + 
                                 " envoyé : " + droneData);
            }

            System.out.println("\nEn attente des résultats...");

            // Étape 4 : Recevoir le statut
            String status = input.readLine();

            if ("SUCCESS".equals(status)) {
                System.out.println("\nSimulation réussie !");
                System.out.println("================================");
                System.out.println("Positions finales des drones :");
                System.out.println("================================");

                // Étape 5 : Recevoir les positions finales
                for (int i = 0; i < drones.size(); i++) {
                    String resultLine = input.readLine();
                    String[] parts = resultLine.split(" ");
                    int x            = Integer.parseInt(parts[0]);
                    int y            = Integer.parseInt(parts[1]);
                    char orientation = parts[2].charAt(0);
                    System.out.println("Drone " + i + " : " + 
                                     x + " " + y + " " + orientation);
                }
            } else {
                System.out.println("Erreur : simulation échouée !");
            }

        } catch (IOException e) {
            System.out.println("Erreur connexion : " + e.getMessage());
            System.out.println("Vérifiez que le serveur est bien lancé !");
        }
    }
}
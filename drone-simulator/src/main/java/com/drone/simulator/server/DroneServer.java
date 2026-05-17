package com.drone.simulator.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DroneServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("  Simulateur de Drones - V3   ");
        System.out.println("      Mode Serveur             ");
        System.out.println("================================");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("En attente de connexion client...");

            // Accepter plusieurs clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté : " + 
                                 clientSocket.getInetAddress());

                // Créer un thread pour gérer ce client
                ServerConnectionHandler handler = 
                    new ServerConnectionHandler(clientSocket);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
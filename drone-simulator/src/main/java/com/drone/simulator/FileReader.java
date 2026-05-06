package com.drone.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private int mapWidth;
    private int mapHeight;
    private List<Drone> drones;

    // Constructeur
    public FileReader() {
        this.drones = new ArrayList<>();
    }

    // Lire le fichier de configuration
    public void readConfiguration(String filename) {
        try {
            BufferedReader reader = new BufferedReader(
                new java.io.FileReader(filename)
            );

            // Lire la première ligne : taille de la carte
            String firstLine = reader.readLine();
            String[] dimensions = firstLine.split(" ");
            mapWidth  = Integer.parseInt(dimensions[0]);
            mapHeight = Integer.parseInt(dimensions[1]);

            // Lire les lignes suivantes : les drones
            String line;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Drone drone = parseDroneData(id, line);
                    drones.add(drone);
                    id++;
                }
            }

            reader.close();
            System.out.println("Fichier lu avec succès !");
            System.out.println("Carte : " + mapWidth + " x " + mapHeight);
            System.out.println("Nombre de drones : " + drones.size());

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    // Parser une ligne de drone
    // Exemple de ligne : "0 5 N LMLMLMLMM"
    private Drone parseDroneData(int id, String line) {
        String[] parts = line.split(" ");
        int x               = Integer.parseInt(parts[0]);
        int y               = Integer.parseInt(parts[1]);
        char orientation    = parts[2].charAt(0);
        String commands     = parts[3];

        return new Drone(id, x, y, orientation, commands);
    }

    // Getters
    public int getMapWidth()        { return mapWidth; }
    public int getMapHeight()       { return mapHeight; }
    public List<Drone> getDrones()  { return drones; }
}
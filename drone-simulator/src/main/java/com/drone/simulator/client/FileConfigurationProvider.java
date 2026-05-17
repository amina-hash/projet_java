package com.drone.simulator.client;

import com.drone.simulator.Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileConfigurationProvider implements ConfigurationProvider {

    private int mapWidth;
    private int mapHeight;
    private List<Drone> drones;

    // Constructeur
    public FileConfigurationProvider(String filename) {
        this.drones = new ArrayList<>();
        readFromFile(filename);
    }

    // Lire le fichier de configuration
    private void readFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(
                new java.io.FileReader(filename)
            );

            // Lire la taille de la carte
            String firstLine = reader.readLine();
            String[] dimensions = firstLine.split(" ");
            mapWidth  = Integer.parseInt(dimensions[0]);
            mapHeight = Integer.parseInt(dimensions[1]);

            // Lire le nombre de drones
            int numDrones = Integer.parseInt(reader.readLine().trim());

            // Lire les données de chaque drone
            int id = 0;
            for (int i = 0; i < numDrones; i++) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    Drone drone = parseDroneData(id, line);
                    drones.add(drone);
                    id++;
                }
            }

            reader.close();
            System.out.println("Configuration lue depuis : " + filename);
            System.out.println("Carte : " + mapWidth + " x " + mapHeight);
            System.out.println("Nombre de drones : " + drones.size());

        } catch (IOException e) {
            System.out.println("Erreur lecture fichier : " + e.getMessage());
        }
    }

    // Parser une ligne de drone
    // Exemple : "0 5 N LMLMLMLMM"
    private Drone parseDroneData(int id, String line) {
        String[] parts = line.split(" ");
        int x            = Integer.parseInt(parts[0]);
        int y            = Integer.parseInt(parts[1]);
        char orientation = parts[2].charAt(0);
        String commands  = parts[3];
        return new Drone(id, x, y, orientation, commands);
    }

    // Implémenter les méthodes de l'interface
    @Override
    public int getMapWidth() { return mapWidth; }

    @Override
    public int getMapHeight() { return mapHeight; }

    @Override
    public List<Drone> getDrones() { return drones; }

// Retourner les commandes d'un drone selon son id
@Override
public String getDroneCommands(int droneId) {
    if (droneId >= 0 && droneId < drones.size()) {
        return drones.get(droneId).getCommands();
    }
    return "";
}
}
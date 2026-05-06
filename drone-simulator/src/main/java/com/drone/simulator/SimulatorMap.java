package com.drone.simulator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorMap {

    private char[][] grid;
    private int width;
    private int height;

    private static final char OBSTACLE = '#';
    private static final char EMPTY = '.';
    private List<Drone> dronesList = new ArrayList<>();

    // Constructeur
    public SimulatorMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];

        // Remplir la grille avec des cases vides
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = EMPTY;
            }
        }
    }

    // Générer les obstacles aléatoirement
    public void addObstacles(int nombreObstacles, int[] dronesX, int[] dronesY) {
        Random random = new Random();
        int count = 0;

        while (count < nombreObstacles) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            // Vérifier que ce n'est pas la position d'un drone
            boolean positionDrone = false;
            for (int i = 0; i < dronesX.length; i++) {
                if (dronesX[i] == x && dronesY[i] == y) {
                    positionDrone = true;
                    break;
                }
            }

            // Placer l'obstacle seulement si la case est vide
            if (!positionDrone && grid[y][x] == EMPTY) {
                grid[y][x] = OBSTACLE;
                count++;
            }
        }
    }

    // Placer un drone sur la carte
    public void placeDrone(Drone drone) {
        grid[drone.getY()][drone.getX()] = drone.getSymbol();
    }

    // Effacer un drone de la carte
    public void removeDrone(Drone drone) {
        grid[drone.getY()][drone.getX()] = EMPTY;
    }

    // Vérifier si une case est bloquée par un obstacle
    public boolean isBlocked(int x, int y) {
        return grid[y][x] == OBSTACLE;
    }

    // Wrapping des coordonnées
    public int wrapCoordinate(int coord, int max) {
        return ((coord % max) + max) % max;
    }

    // Afficher la carte dans le terminal
    public void displayMap() {
        System.out.println("====================");
        for (int row = height - 1; row >= 0; row--) {
            for (int col = 0; col < width; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("====================");
    }

    public void addDroneToList(Drone drone) {
    dronesList.add(drone);
}

public List<Drone> getDrones() {
    return dronesList;
}
    // Getters
    public int getWidth()          { return width; }
    public int getHeight()         { return height; }
    public char[][] getGrid()      { return grid; }
}
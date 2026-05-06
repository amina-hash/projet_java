package com.drone.simulator;

public class Drone {
    
    // Les attributs du drone
    private int x;
    private int y;
    private char orientation; // N, S, E, W
    private String commands;
    private int id;

    // Constructeur
    public Drone(int id, int x, int y, char orientation, String commands) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.commands = commands;
    }

    // Tourner à gauche
    public void turnLeft() {
        switch (orientation) {
            case 'N': orientation = 'W'; break;
            case 'W': orientation = 'S'; break;
            case 'S': orientation = 'E'; break;
            case 'E': orientation = 'N'; break;
        }
    }

    // Tourner à droite
    public void turnRight() {
        switch (orientation) {
            case 'N': orientation = 'E'; break;
            case 'E': orientation = 'S'; break;
            case 'S': orientation = 'W'; break;
            case 'W': orientation = 'N'; break;
        }
    }

    // Avancer d'une case
    public void moveForward(SimulatorMap map) {
        int newX = x;
        int newY = y;

        switch (orientation) {
            case 'N': newY++; break;
            case 'S': newY--; break;
            case 'E': newX++; break;
            case 'W': newX--; break;
        }

        // Appliquer le wrapping
        newX = map.wrapCoordinate(newX, map.getWidth());
        newY = map.wrapCoordinate(newY, map.getHeight());

        // Vérifier obstacle avant de bouger
        if (!map.isBlocked(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    // Reculer d'une case
    public void moveBackward(SimulatorMap map) {
        int newX = x;
        int newY = y;

        switch (orientation) {
            case 'N': newY--; break;
            case 'S': newY++; break;
            case 'E': newX--; break;
            case 'W': newX++; break;
        }

        // Appliquer le wrapping
        newX = map.wrapCoordinate(newX, map.getWidth());
        newY = map.wrapCoordinate(newY, map.getHeight());

        // Vérifier obstacle avant de bouger
        if (!map.isBlocked(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    // Exécuter une commande
    public void executeCommand(char cmd, SimulatorMap map) {
        switch (cmd) {
            case 'L': turnLeft();          break;
            case 'R': turnRight();         break;
            case 'M': moveForward(map);    break;
            case 'B': moveBackward(map);   break;
        }
    }

    // Retourner le symbole du drone selon son orientation
    public char getSymbol() {
        switch (orientation) {
            case 'N': return '^';
            case 'S': return 'v';
            case 'E': return '>';
            case 'W': return '<';
            default:  return '?';
        }
    }

    // Retourner la position en texte
    public String getPosition() {
        return x + " " + y + " " + orientation;
    }

    // Getters
    public int getId()           { return id; }
    public int getX()            { return x; }
    public int getY()            { return y; }
    public char getOrientation() { return orientation; }
    public String getCommands()  { return commands; }
}
package com.drone.simulator;

import com.drone.simulator.concurrent.SimulatorConcurrent;
import com.drone.simulator.server.DroneServer;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("  Simulateur de Flotte Drones  ");
        System.out.println("================================");
        System.out.println("Choisissez un mode :");
        System.out.println("  1 - Mode Basique (séquentiel)");
        System.out.println("  2 - Mode Concurrent (threads)");
        System.out.println("  3 - Mode Serveur (réseau)");
        System.out.println("================================");
        System.out.print("Votre choix : ");

        int choix = scanner.nextInt();

        switch (choix) {
            case 1:
                SimulatorBasic.main(args);
                break;
            case 2:
                SimulatorConcurrent.main(args);
                break;
            case 3:
                DroneServer.main(args);
                break;
            default:
                System.out.println("Choix invalide !");
        }

        scanner.close();
    }
}
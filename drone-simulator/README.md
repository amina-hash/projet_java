# Drone Simulator

Simulateur Java simple pour une flotte de drones avec trois modes d'exécution :

- `Mode Basique` : exécution séquentielle des commandes de drones.
- `Mode Concurrent` : simulation multithread avec affichage dynamique.
- `Mode Serveur` : démarrage d'un serveur réseau écoutant les connexions clients.

## Structure du projet

- `pom.xml` : configuration Maven du projet.
- `src/main/java/com/drone/simulator/` : classes principales et modes de simulation.
- `src/main/java/com/drone/simulator/concurrent/` : classes pour l'exécution concurrente.
- `src/main/java/com/drone/simulator/server/` : classes pour le mode serveur.
- `drones.txt` : fichier de configuration des drones utilisé par les simulateurs.
- `drones_client.txt` : fichier de configuration client utilisé par la communication client/serveur.

## Prérequis

- Java 17
- Maven

## Build

Depuis le dossier `drone-simulator`, exécutez :

```powershell
mvn clean package
```

## Exécution

### Démarrer le simulateur principal

```powershell
mvn exec:java
```

Le programme affichera un menu interactif :

1. Mode Basique (séquentiel)
2. Mode Concurrent (threads)
3. Mode Serveur (réseau)

### Mode Basique

Lit la configuration depuis `drones.txt`, affiche la carte et exécute les commandes de chaque drone les unes après les autres.

### Mode Concurrent

Lit la configuration depuis `drones.txt`, lance un thread par drone et un thread dédié à l'affichage pour simuler le comportement concurrent.

### Mode Serveur

Démarre un serveur sur le port `5000` et attend des connexions clients.

### Mode Client

Le client lit la configuration depuis `drones_client.txt`, se connecte au serveur `localhost:5000`, envoie la configuration puis affiche les positions finales retournées par le serveur.

Pour lancer le client après avoir démarré le serveur :

```powershell
java -cp target/classes com.drone.simulator.client.DroneClient
```

## Format des fichiers de configuration

### `drones.txt` (Modes Basique et Concurrent)

- Première ligne : `largeur hauteur`
- Lignes suivantes : une définition de drone par ligne

Exemple :

```text
20 20
0 5 N LMLMLMLMM
5 0 S MMRMMRMRRM
```

### `drones_client.txt` (Mode Client/Serveur)

- Première ligne : largeur hauteur
- Deuxième ligne : nombre de drones
- Lignes suivantes : une définition de drone par ligne

Exemple :

```text
20 20
2
0 5 N LMLMLMLMM
5 0 S MMRMMRMRRM
```

Chaque ligne de drone contient :

- `x` : position X initiale
- `y` : position Y initiale
- `orientation` : direction initiale (`N`, `S`, `E`, `W`)
- `commands` : suite de commandes (`L`, `R`, `M`, `B`)

## Tests

Le projet utilise JUnit 5. Pour lancer les tests :

```powershell
mvn test
```

Les fichiers de test ajoutés incluent :

- `src/test/java/com/drone/simulator/FileReaderTest.java` : validation de la lecture des fichiers de configuration.
- `src/test/java/com/drone/simulator/DroneTest.java` : validation des commandes et des rotations de drone.
- `src/test/java/com/drone/simulator/SimulatorMapTest.java` : validation du wrapping, des obstacles et du placement de drones.
- `src/test/java/com/drone/simulator/concurrent/DroneWorkerTest.java` : validation d'une étape de simulation concurrente.

Des exemples de fichiers de configuration supplémentaires ont également été ajoutés pour tester différents scénarios :

- `test_petit.txt`
- `test_wrapping.txt`
- `test_obstacle.txt`
- `test_wrapping_vertical.txt`

Ces tests permettent de vérifier les comportements des versions Basique, Concurrente et Client/Serveur via leurs composants.

## Personnalisation

- Mettez à jour `drones.txt` pour tester d'autres cartes et trajectoires.
- Adaptez les classes `SimulatorBasic`, `SimulatorConcurrent` ou `DroneServer` pour étendre les comportements.

## Notes

- Le mode `Mode Basique` utilise la lecture de `drones.txt` à la racine du projet.
- Le mode `Mode Concurrent` ajoute des obstacles et synchronise l'affichage entre threads.
- Le mode `Mode Serveur` attend des clients sur le port `5000`.
- Le mode `Mode Client` utilise `drones_client.txt` et doit être lancé après le démarrage du serveur.

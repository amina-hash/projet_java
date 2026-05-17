package com.drone.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FileReaderTest {

    @Test
    public void testReadConfigurationFromFile() throws Exception {
        Path tempFile = Files.createTempFile("drones-test", ".txt");
        String content = "5 5\n" +
                         "1 2 N LMLMLMLMM\n" +
                         "3 3 E MMRMMRMRRM\n";
        Files.writeString(tempFile, content);

        FileReader reader = new FileReader();
        reader.readConfiguration(tempFile.toString());

        assertEquals(5, reader.getMapWidth());
        assertEquals(5, reader.getMapHeight());

        List<Drone> drones = reader.getDrones();
        assertEquals(2, drones.size());
        assertEquals('N', drones.get(0).getOrientation());
        assertEquals("MMRMMRMRRM", drones.get(1).getCommands());
    }
}

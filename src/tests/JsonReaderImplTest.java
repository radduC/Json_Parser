package tests;

import jsonparser.JsonReaderImpl;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderImplTest {
    private final JsonReaderImpl jsonReader = new JsonReaderImpl();

    @Test
    void readFileTest() throws FileNotFoundException {
        String inputFile = "resources/input/example.json";
        Queue<String> lines = jsonReader.readFile(inputFile);
        System.out.println(lines);

        assertEquals(26, lines.size());
        assertEquals("[", lines.poll());
        assertEquals("{", lines.poll());
        assertEquals("\"firstName\":\"John\",", lines.poll());
        assertEquals("\"lastName\":\"Smith\",", lines.poll());
        assertEquals("\"isAlive\":true,", lines.poll());
    }
}
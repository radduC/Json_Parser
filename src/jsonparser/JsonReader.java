package jsonparser;

import java.io.FileNotFoundException;
import java.util.Queue;

public interface JsonReader {
    Queue<String> readFile(String inputFile) throws FileNotFoundException;
}

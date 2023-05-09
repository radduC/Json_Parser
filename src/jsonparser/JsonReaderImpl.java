package jsonparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class JsonReaderImpl implements JsonReader {
    @Override
    public Queue<String> readFile(String inputFile) throws FileNotFoundException {
        Queue<String> lines = new LinkedList<>();
        String pattern = "\\s+(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputFile)))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine().replaceAll(pattern, "");
                lines.add(line);
            }
        }

        return lines;
    }
}

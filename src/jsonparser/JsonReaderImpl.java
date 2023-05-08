package jsonparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class JsonReaderImpl implements JsonReader {
    String inputFile;

    public JsonReaderImpl(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public Scanner getScanner(String inputFile) {
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return new Scanner(bufferedReader);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

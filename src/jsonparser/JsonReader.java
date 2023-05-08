package jsonparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class JsonReader {
    Scanner scanner;
    BufferedReader bufferedReader;
    FileReader fileReader;
    String inputFile;

    public JsonReader(String inputFile) {
        this.inputFile = inputFile;

        try {
            fileReader = new FileReader(inputFile);
            bufferedReader = new BufferedReader(fileReader);
            scanner = new Scanner(bufferedReader);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Scanner getScanner() {
        return scanner;
    }
}

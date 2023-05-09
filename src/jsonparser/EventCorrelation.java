package jsonparser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EventCorrelation {
    public void correlateEvents(String jsonFile, String inputFile, String outputFile) throws IOException {
        writeOutput(outputFile, jsonFile, readInput(inputFile));
    }

    private List<List<String>> readInput(String inputFile) throws FileNotFoundException {
        List<List<String>> operations = new LinkedList<>();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputFile)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<String> operation = new LinkedList<>();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '"') {
                        i++;
                        while (i < line.length() && line.charAt(i) != '"') {
                            sb.append(line.charAt(i++));
                        }

                    } else if (Character.isWhitespace(line.charAt(i))) {
                        continue;

                    } else {
                        while (i < line.length() && Character.isLetterOrDigit(line.charAt(i))) {
                            sb.append(line.charAt(i++));
                        }
                    }

                    operation.add(sb.toString());
                    sb.setLength(0);
                }

                operations.add(operation);
            }

        }

        return operations;
    }

    private void writeOutput(String outputFile, String jsonFile, List<List<String>> operations) throws IOException {
        JsonDataStructure jsonDS = new JsonDataStructure(new JsonReaderImpl(), new JsonTokenizerImpl());
        jsonDS.process(jsonFile);

        try (BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(outputFile))) {
            List<String> outputs = new LinkedList<>();

            for (List<String> operation : operations) {
                String keyword = operation.remove(0);
                String[] inputs = operation.toArray(new String[operation.size()]);
                String pattern = "[\\S ]+";
                String result = correlateOperations(jsonDS, keyword, inputs);

                if (result.matches(pattern)) {
                    outputs.add(result);
                }
            }

            for (String output : outputs) {
                outputBuffer.write(output + "\n");
            }
        }
    }

    private String correlateOperations(JsonDataStructure jsonDS, String keyword, String[] inputs) {
        switch (keyword) {
            case "GET" -> {
                GetCorrelation correlation = new GetCorrelation(jsonDS);
                return correlation.get(inputs);
            }

            case "PUT" -> {
                PutCorrelation correlation = new PutCorrelation(jsonDS);
                return correlation.put(inputs);
            }

            case "DEL" -> {
                DelCorrelation correlation = new DelCorrelation(jsonDS);
                return correlation.del(inputs);
            }

            default -> {
                return "Uknown operation";
            }
        }
    }
}

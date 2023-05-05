import jsonparser.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        corelateEvents("resources/input/example.json", "resources/queries/query.txt", "resources/output/output.txt");
        corelateEvents("resources/input/traffic_0.json", "resources/queries/query_0.txt", "resources/output/output_0.txt");
        corelateEvents("resources/input/traffic_1.json", "resources/queries/query_1.txt", "resources/output/output_1.txt");
        corelateEvents("resources/input/traffic_2.json", "resources/queries/query_2.txt", "resources/output/output_2.txt");
        corelateEvents("resources/input/traffic_3.json", "resources/queries/query_3.txt", "resources/output/output_3.txt");
        corelateEvents("resources/input/traffic_4.json", "resources/queries/query_4.txt", "resources/output/output_4.txt");
        corelateEvents("resources/input/traffic_5.json", "resources/queries/query_5.txt", "resources/output/output_5.txt");
    }

    public static void corelateEvents(String jsonFile, String inputFile, String outputFile) throws IOException {
        writeOutput(outputFile, jsonFile, readInput(inputFile));
    }

    private static List<List<String>> readInput(String inputFile) throws FileNotFoundException {
        List<List<String>> operations = new LinkedList<>();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputFile)))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                List<String> operation = new LinkedList<>();
                int i = 0;

                while (i < line.length()) {
                    char c = line.charAt(i);

                    if (c == '"') {
                        StringBuilder sb = new StringBuilder();
                        i++;

                        while (i < line.length() && line.charAt(i) != '"') {
                            sb.append(line.charAt(i++));
                        }

                        operation.add(sb.toString());

                    } else if (Character.isWhitespace(c)) {
                        i++;
                        continue;

                    } else {
                        StringBuilder sb = new StringBuilder();

                        while (i < line.length() && Character.isLetterOrDigit(line.charAt(i))) {
                            sb.append(line.charAt(i++));
                        }

                        operation.add(sb.toString());
                    }

                    i++;
                }

                operations.add(operation);
            }
        }

        return operations;
    }

    private static void writeOutput(String outputFile, String jsonFile, List<List<String>> operations)
            throws IOException {

//        EventCorrelation eventCorrelation2 = new EventCorrelation(new JSON(jsonFile));
        EventCorrelationV2 eventCorrelation = new EventCorrelationV2(new JSONv2(jsonFile));

        try (BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(outputFile))) {
            List<String> outputs = new LinkedList<>();

            for (List<String> operation : operations) {
                String keyword = operation.remove(0);
                String[] inputs = operation.toArray(new String[operation.size()]);
                String input;
                String pattern = "[\\S ]+";

                input = switch (keyword) {
                    case "GET" -> eventCorrelation.get(inputs);
                    case "PUT" -> eventCorrelation.put(inputs);
                    case "DEL" -> eventCorrelation.del(inputs);
                    default -> "Uknown operation";
                };

                if (input.matches(pattern)) {
                    outputs.add(input);
                }
            }

            for (String output : outputs) {
                outputBuffer.write(output + "\n");
            }
        }
    }

    public static String tokenizeString(String string) throws Exception {
        StringBuilder sb = new StringBuilder();

        if (string.charAt(0) == '"') {
            string = string.substring(1);
        } else {
            return null;
        }

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (c == '"') {
                return sb.toString();
            }

            sb.append(c);
        }

        throw new Exception("Error: end of string quote expected!");
    }

    public static String tokenizeNumber(String string) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if ((c >= '0' && c <= '9') || c == '-') {
                sb.append(c);
            } else {
                break;
            }
        }

        return sb.toString();
    }

    public static String tokenizeBoolean(String string) {
        string = string.toLowerCase();

        if (string.equals("true") || string.equals("false")) {
            return string;
        }

        return null;
    }

    public static String tokenizeNull(String string) {
        if (string.toLowerCase().equals("null")) {
            return "null";
        }

        return null;
    }
}

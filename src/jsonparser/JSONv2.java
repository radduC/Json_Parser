package jsonparser;

import models.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class JSONv2 {
    private Queue<String> tokens;
    private JsonArray json;

    public JSONv2(String inputFile) {
        try {
            tokens = readFile(inputFile);
            json = parseJson(tokens);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonArray getJson() {
        return json;
    }

    private Queue<String> readFile(String inputFile) throws FileNotFoundException {
        Queue<String> tokens = new LinkedList<>();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputFile)))) {
            String pattern = "\\s+(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

            while (scanner.hasNext()) {
                String line = scanner.nextLine().replaceAll(pattern, "");
                Queue<String> token = getTokens(line);
                tokens.addAll(token);
            }
        }

        return tokens;
    }

    private Queue<String> getTokens(String line) {
        Queue<String> tokens = new LinkedList<>();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if ("{}[]:,#_-".contains(c + "")) {
                tokens.offer(c + "");

            } else if (c == '"') {
                StringBuilder sb = new StringBuilder();
                int index = i + 1;

                while (line.charAt(index) != '"') {
                    sb.append(line.charAt(index++));
                }

                tokens.offer(sb.toString());
                i = index;

            } else if (Character.toLowerCase(c) == 't' || c == 'f') {
                StringBuilder sb = new StringBuilder();
                int index = i;

                while (line.charAt(index) != 'e') {
                    sb.append(line.charAt(index++));
                }

                i = index;
                sb.append(line.charAt(i--));
                tokens.offer(sb.toString());

            } else if ((c >= '0' && c <= '9') || c == '-') {
                StringBuilder sb = new StringBuilder();
                int index = i;

                while ((c >= '0' && c <= '9') || c == '-') {
                    sb.append(c);

                    try {
                        c = line.charAt(++index);
                    } catch (Exception e) {
                        break;
                    }
                }

                i = --index;
                tokens.offer(sb.toString());

            } else if (Character.toLowerCase(c) == 'n') {
                StringBuilder sb = new StringBuilder();
                int index = i;

                while (line.charAt(index) != 'l') {
                    sb.append(line.charAt(index++));
                }

                sb.append(line.charAt(index++));
                sb.append(line.charAt(index));
                i = --index;
                tokens.offer(sb.toString());
            }
        }

        return tokens;
    }

    private JsonArray parseJson(Queue<String> tokens) throws Exception {
        return (JsonArray) parse(tokens);
    }

    private GenericNode parse(Queue<String> tokens) throws Exception {
        String firstToken = tokens.poll();

        if (firstToken.equals("[")) {
            return parseArray(tokens);
        }

        if (firstToken.equals("{")) {
            return parseObject(tokens);
        }

        if (firstToken.matches("\btrue\b|\bfalse\b") || firstToken.equals("true") || firstToken.equals("false")) {
            return new JsonBoolean(firstToken);
        }

        if (firstToken.matches("[0-9]+")) {
            return new JsonNumber(firstToken);
        }

        if (firstToken.matches("\bnull\b") || firstToken.equals("null")) {
            return new JsonNull();
        }

        if (firstToken.matches("[\\w\\s\\W]+")) {
            return new JsonString(firstToken);
        }

        return new JsonNull();
    }

    private JsonArray parseArray(Queue<String> tokens) throws Exception {
        JsonArray jsonArray = new JsonArray();
        String firstToken = tokens.peek();

        if (firstToken.equals("]")) {
            tokens.poll();
            return jsonArray;
        }

        while (!tokens.isEmpty()) {
            JsonObject jsonNode = (JsonObject) parse(tokens);
            jsonArray.add(jsonNode);
            firstToken = tokens.peek();

            if (firstToken.equals("]")) {
                tokens.poll();
                return jsonArray;

            } else if (!firstToken.equals(",")) {
                throw new Exception("Expected comma after object in array, instead found this: " + firstToken + "\n");

            } else {
                tokens.poll();
            }
        }

        throw new Exception("Expected end-of-array bracket");
    }

    private JsonObject parseObject(Queue<String> tokens) throws Exception {
        JsonObject jsonObject = new JsonObject();
        String firstToken = tokens.peek();

        if (firstToken.equals("}")) {
            tokens.poll();
            return jsonObject;
        }

        while (!tokens.isEmpty()) {
            String key = tokens.peek();
            // String pattern = "[\\w\\s\\W]+";
            String pattern = "[\\w\\s\\#\\-\\_]+";

            if (key.matches(pattern)) {
                tokens.poll();

            } else {
                return jsonObject;
            }

            tokens.poll();
            GenericNode value = parse(tokens);
            jsonObject.put(key, value);
            firstToken = tokens.peek();

            if (firstToken.equals("]")) {
                tokens.poll();
                return jsonObject;

            } else if (!firstToken.equals(",")) {
                // throw new Exception("Expected comma after pair in object, got:" +
                // firstToken);
            }

            tokens.poll();
        }

        return jsonObject;
    }
}

package jsonparser;

import models.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class JsonTokenizer {
    private static final String pattern = "\\s+(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
    private static final String DELIMITERS = "{}[]:,#_-";
    //    private final JsonReader jsonReader;
    private final Queue<String> tokens = new LinkedList<>();


    public Queue<String> getTokens() {
        return tokens;
    }

    private Queue<String> tokenize(JsonReader jsonReader) {
        Scanner scanner = jsonReader.getScanner();

        while (scanner.hasNext()) {
            String line = scanner.nextLine().replaceAll(pattern, "");
            Queue<String> token = tokenizeLine(line);
            tokens.addAll(token);
        }

        return tokens;
    }

    private Queue<String> tokenizeLine(String line) {
        Queue<String> tokens = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            sb.setLength(0);
            char c = line.charAt(i);

            if (DELIMITERS.indexOf(c) != -1) {
                tokens.offer(String.valueOf(c));

            } else if (c == '"') {
                i = tokenizeString(line, tokens, sb, i);

            } else if (Character.toLowerCase(c) == 't' || c == 'f') {
                i = tokenizeBoolean(line, tokens, sb, i);

            } else if (Character.isDigit(c) || c == '-') {
                i = tokenizeNumber(line, tokens, sb, i);

            } else if (Character.toLowerCase(c) == 'n') {
                i = tokenizeNull(line, tokens, sb, i);
            }
        }

        return tokens;
    }

    private static int tokenizeNull(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while (line.charAt(index) != 'l') {
            sb.append(line.charAt(index++));
        }

        sb.append(line.charAt(index++));
        sb.append(line.charAt(index));
        tokens.offer(sb.toString());
        return --index;
    }

    private static int tokenizeNumber(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while ((line.charAt(index) >= '0' && line.charAt(index) <= '9') || line.charAt(index) == '-') {
            sb.append(line.charAt(index++));
        }

        tokens.offer(sb.toString());
        return --index;
    }

    private static int tokenizeBoolean(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while (line.charAt(index) != 'e') {
            sb.append(line.charAt(index++));
        }

        sb.append(line.charAt(index--));
        tokens.offer(sb.toString());
        return index;
    }

    private static int tokenizeString(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i + 1;

        while (line.charAt(index) != '"') {
            sb.append(line.charAt(index++));
        }

        tokens.offer(sb.toString());
        sb.setLength(0);
        return index;
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

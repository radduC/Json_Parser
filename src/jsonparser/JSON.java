package jsonparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class JSON {
    private Queue<String> tokens;
    private List<HashMap<String, Object>> json;

    public JSON(String inputFile) {
        try {
            tokens = readFile(inputFile);
            json = parseJson(tokens);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, Object>> getJson() {
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
                tokens.add(c + "");

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

    private List<HashMap<String, Object>> parseJson(Queue<String> tokens) throws Exception {
        return (List<HashMap<String, Object>>) parse(tokens).get(0);
    }

    private List<Object> parse(Queue<String> tokens) throws Exception {
        List<Object> result = new LinkedList<>();
        String firstToken = tokens.poll();

        if (firstToken.equals("[")) {
            return parseArray(tokens);
        } else if (firstToken.equals("{")) {
            return parseObject(tokens);
        }

        result.add(firstToken);
        result.add(tokens);
        return result;
    }

    private List<Object> parseArray(Queue<String> tokens) throws Exception {
        List<Object> result = new LinkedList<>();
        List<Object> list = new ArrayList<>();
        String firstToken = tokens.peek();

        if (firstToken.equals("]")) {
            result.add(list);
            tokens.poll();
            result.add(tokens);
            return result;
        }

        while (!tokens.isEmpty()) {
            List<Object> parsed = parse(tokens);
            Object json = parsed.get(0);
            tokens = (Queue<String>) parsed.get(1);
            list.add(json);
            firstToken = tokens.peek();

            if (firstToken.equals("]")) {
                result.add(list);
                tokens.poll();
                result.add(tokens);
                return result;

            } else if (!firstToken.equals(",")) {
                throw new Exception("Expected comma after object in array, instead found this: " + firstToken + "\n");

            } else {
                tokens.poll();
            }
        }

        throw new Exception("Expected end-of-array bracket");
    }

    private List<Object> parseObject(Queue<String> tokens) throws Exception {
        List<Object> result = new LinkedList<>();
        HashMap<String, Object> object = new HashMap<>();
        String firstToken = tokens.peek();

        if (firstToken.equals("}")) {
            tokens.poll();
            result.add(object);
            result.add(tokens);
            return result;
        }

        while (!tokens.isEmpty()) {
            String key = tokens.peek();
            String pattern = "[\\w\\s\\#\\-\\_]+";
            // String pattern = "[\\w\\W\\s]+";

            if (key.matches(pattern)) {
                tokens.poll();

            } else {
                // throw new Exception("Expected a string as key, got: " + key);
                result.add(object);
                result.add(tokens);
                return result;
            }

            tokens.poll();
            List<Object> parsed = parse(tokens);
            Object value = parsed.get(0);
            String valueClass = value.getClass().toString();
            tokens = (Queue<String>) parsed.get(1);

            if (valueClass.contains("String")) {
                value = (String) value;

            } else if (valueClass.contains("ArrayList")) {
                value = (List<HashMap<String, Object>>) value;

            } else if (valueClass.contains("HashMap")) {
                value = (HashMap<String, Object>) value;
            }

            object.put(key, value);
            firstToken = tokens.peek();

            if (firstToken.equals("]")) {
                result.add(object);
                tokens.poll();
                result.add(tokens);
                return result;

            } else if (!firstToken.equals(",")) {
                // throw new Exception("Expected comma after pair in object, got:" +
                // firstToken);
            }

            tokens.poll();
        }

        result.add(object);
        result.add(tokens);
        return result;
        // throw new Exception("Expected end-of-object brace");
    }
}

package jsonparser;

import models.*;

import java.util.Queue;

public class JsonDataStructure {
    private JsonReader jsonReader;
    private JsonTokenizer jsonTokenizer;
    private JsonArray json;

    public JsonDataStructure() {

    }

    public JsonDataStructure(JsonReader jsonReader, JsonTokenizer jsonTokenizer) {
        this.jsonReader = jsonReader;
        this.jsonTokenizer = jsonTokenizer;
    }

    public JsonArray getJson() {
        return json;
    }

    public void process(String inputFile) {
        try {
            Queue<String> lines = jsonReader.readFile(inputFile);
            Queue<String> tokens = jsonTokenizer.tokenizeLines(lines);
            json = parseJson(tokens);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonArray parseJson(Queue<String> tokens) throws Exception {
        return (JsonArray) parse(tokens);
    }

    public GenericNode parse(Queue<String> tokens) throws Exception {
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

    public JsonArray parseArray(Queue<String> tokens) throws Exception {
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

    public JsonObject parseObject(Queue<String> tokens) throws Exception {
        JsonObject jsonObject = new JsonObject();
        String firstToken = tokens.peek();

        if (firstToken.equals("}")) {
            tokens.poll();
            return jsonObject;
        }

        while (!tokens.isEmpty()) {
            String key = tokens.peek();
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

            }

            tokens.poll();
        }

        return jsonObject;
    }
}

package jsonparser;

import models.*;

import java.util.Queue;

public class JsonParser {
    public JsonArray parseJson(Queue<String> tokens) throws Exception {
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

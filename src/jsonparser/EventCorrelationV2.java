package jsonparser;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import exceptions.KeyNotFoundException;
import models.*;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class EventCorrelationV2 {
    private final JsonDataStructure json;

    public EventCorrelationV2(JsonDataStructure json) {
        this.json = json;
    }

    public JsonDataStructure getJson() {
        return json;
    }

    public void navigateJson(Deque<GenericNode> itemsStack, Deque<String> previousStrings, String[] array) throws Exception {

        itemsStack.push(json.getJson());

        for (String string : array) {
            if (string.matches("\\d+")) {
                JsonObject jsonObject = (JsonObject) itemsStack.peek().get(Integer.parseInt(string));
                itemsStack.push(jsonObject);

            } else {
                itemsStack.push(itemsStack.peek().get(string));
            }

            previousStrings.push(string);
        }
    }

    public String get(String[] array) {
        Deque<GenericNode> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();
        GenericNode genericNode;

        try {
            navigateJson(itemsStack, previousStrings, array);

        } catch (IndexOutOfBoundsException e) {
            return ("GET_ERROR_INDEX_OUT_OF_RANGE "
                    + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (JsonArrayException e) {
            return ("GET_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (KeyNotFoundException e) {
            return "GET" + e.getMessage();

        } catch (JsonObjectException e) {
            return ("GET_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        genericNode = itemsStack.peek();

        if (genericNode.getType().equals("JSON_ARRAY")) {
            return "JSON_ARRAY";
        }

        if (genericNode.getType().equals("JSON_OBJECT")) {
            return "JSON_OBJECT";
        }

        if (genericNode.toString().matches("[\btrue\b|\bfalse\b|\bnull\b|\\d]+")) {
            return genericNode.toString();
        }

        return "\"" + genericNode + "\"";
    }

    public String put(String[] array) {
        Deque<GenericNode> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();
        GenericNode genericNode;

        String value = array[array.length - 1];
        String key = array[array.length - 2];
        array = Arrays.copyOf(array, array.length - 2);

        try {
            navigateJson(itemsStack, previousStrings, array);

        } catch (IndexOutOfBoundsException e) {
            return ("PUT_ERROR_INDEX_OUT_OF_RANGE "
                    + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (JsonArrayException e) {
            return ("PUT_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (KeyNotFoundException e) {
            return "PUT" + e.getMessage();

        } catch (JsonObjectException e) {
            return ("PUT_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        genericNode = itemsStack.peek();
        GenericNode jsonValue;

        if (value.equals("true") || value.equals("false")) {
            jsonValue = new JsonBoolean(value);

        } else if (value.equals("null")) {
            jsonValue = new JsonNull();

        } else if (value.matches("\\d+")) {
            jsonValue = new JsonNumber(value);

        } else {
            jsonValue = new JsonString(value);
        }

        try {
            genericNode.put(key, jsonValue);

        } catch (IndexOutOfBoundsException e) {
            return ("PUT_ERROR_INDEX_OUT_OF_RANGE "
                    + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (NullPointerException e) {
            return "PUT_KEY_NOT_FOUND " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\"");

        } catch (JsonObjectException e) {
            return ("PUT_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String del(String[] array) {
        Deque<GenericNode> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();

        try {
            navigateJson(itemsStack, previousStrings, array);

        } catch (IndexOutOfBoundsException e) {
            return ("DEL_ERROR_INDEX_OUT_OF_RANGE "
                    + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (JsonArrayException e) {
            return ("DEL_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (KeyNotFoundException e) {
            return "DEL" + e.getMessage();

        } catch (JsonObjectException e) {
            return ("DEL_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        itemsStack.pop();

        try {
            itemsStack.peek().remove(previousStrings.pop());

        } catch (JsonObjectException e) {
            return ("DEL_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        }

        return "";
    }
}

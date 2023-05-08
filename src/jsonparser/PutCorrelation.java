package jsonparser;

import exceptions.JsonObjectException;
import models.*;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class PutCorrelation extends GenericCorrelation {
    public PutCorrelation(JsonDataStructure json) {
        super(json);
    }

    public String put(String[] array) {
        Deque<GenericNode> itemsStack = new LinkedList<>();
        Deque<String> previousStrings = new LinkedList<>();

        String value = array[array.length - 1];
        String key = array[array.length - 2];
        array = Arrays.copyOf(array, array.length - 2);

        String result = checkExceptions(array, itemsStack, previousStrings, "PUT");

        if (result != null)
            return result;

        GenericNode genericNode = itemsStack.peek();
        GenericNode jsonValue = getJsonValue(value);
        String s = itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\"";

        try {
            genericNode.put(key, jsonValue);

        } catch (IndexOutOfBoundsException e) {
            return "PUT_ERROR_INDEX_OUT_OF_RANGE " + s;

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (NullPointerException e) {
            return "PUT_KEY_NOT_FOUND " + s;

        } catch (JsonObjectException e) {
            return "PUT_ERROR_NOT_OBJECT " + s;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static GenericNode getJsonValue(String value) {
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

        return jsonValue;
    }
}

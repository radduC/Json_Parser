package jsonparser;

import models.GenericNode;

import java.util.Deque;
import java.util.LinkedList;

public class GetCorrelation extends GenericCorrelation {
    private static final String operationType = "GET";

    public GetCorrelation(JsonDataStructure json) {
        super(json);
    }

    public String get(String[] array) {
        String result = checkExceptions(array, itemsStack, previousStrings, operationType);

        if (result != null)
            return result;

        GenericNode genericNode = itemsStack.peek();

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
}

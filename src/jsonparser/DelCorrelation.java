package jsonparser;

import exceptions.JsonObjectException;
import models.GenericNode;

import java.util.Deque;
import java.util.LinkedList;

public class DelCorrelation extends GenericCorrelation{
    private static final String operationType = "DEL";

    public DelCorrelation(JsonDataStructure json) {
        super(json);
    }

    public String del(String[] array) {
        String result = checkExceptions(array, itemsStack, previousStrings, operationType);

        if (result != null)
            return result;

        itemsStack.pop();

        try {
            itemsStack.peek().remove(previousStrings.pop());

        } catch (JsonObjectException e) {
            return "DEL_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\"");

        }

        return "";
    }
}

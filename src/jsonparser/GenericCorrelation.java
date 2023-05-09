package jsonparser;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import exceptions.KeyNotFoundException;
import models.GenericNode;
import models.JsonObject;

import java.util.Deque;
import java.util.LinkedList;

public class GenericCorrelation {
    private final JsonDataStructure json;
    protected final Deque<GenericNode> itemsStack;
    protected final Deque<String> previousStrings;

    public GenericCorrelation(JsonDataStructure json) {
        this.json = json;
        itemsStack = new LinkedList<>();
        previousStrings = new LinkedList<>();
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

    String checkExceptions(String[] array, Deque<GenericNode> itemsStack, Deque<String> previousStrings, String operationType) {
        try {
            navigateJson(itemsStack, previousStrings, array);

        } catch (IndexOutOfBoundsException e) {
            return (operationType + "_ERROR_INDEX_OUT_OF_RANGE " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (JsonArrayException e) {
            return (operationType + "_ERROR_NOT_ARRAY " + "\"" + previousStrings.peek() + "\"");

        } catch (NumberFormatException e) {
            return "Number Format Exception";

        } catch (KeyNotFoundException e) {
            return operationType + e.getMessage();

        } catch (JsonObjectException e) {
            return (operationType + "_ERROR_NOT_OBJECT " + (itemsStack.size() == 1 ? "root" : "\"" + previousStrings.peek() + "\""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

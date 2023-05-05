package models;

import exceptions.JsonArrayException;
import exceptions.KeyNotFoundException;

import java.util.HashMap;

public class JsonObject implements GenericNode {
    private HashMap<String, GenericNode> jsonObject;

    public JsonObject() {
        jsonObject = new HashMap<>();
    }

    public JsonObject(HashMap<String, GenericNode> objectNode) {
        jsonObject = objectNode;
    }

    public HashMap<String, GenericNode> getObject() {
        return jsonObject;
    }

    @Override
    public GenericNode get(String key) throws KeyNotFoundException {
        if (jsonObject.get(key) == null) {
            throw new KeyNotFoundException("_KEY_NOT_FOUND " + "\"" + key + "\"");
        }

        return jsonObject.get(key);
    }

    public void put(String string, GenericNode genericNode) {
        jsonObject.put(string, genericNode);
    }

    @Override
    public void remove(String key) {
        jsonObject.remove(key);
    }

    @Override
    public GenericNode get(int index) throws JsonArrayException {
        throw new JsonArrayException("ERROR_NOT_ARRAY");
    }

    @Override
    public void add(JsonObject objectNode) throws JsonArrayException {
        throw new JsonArrayException("ERROR_NOT_ARRAY");
    }

    @Override
    public String getType() {
        return "JSON_OBJECT";
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

}

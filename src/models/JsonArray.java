package models;

import exceptions.JsonObjectException;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements GenericNode {
    private final List<JsonObject> jsonArray;

    public JsonArray() {
        jsonArray = new ArrayList<>();
    }

    public JsonArray(List<JsonObject> jsonArray) {
        this.jsonArray = jsonArray;
    }

    public void add(JsonObject objectNode) {
        jsonArray.add(objectNode);
    }

    @Override
    public JsonObject get(int index) {
        return jsonArray.get(index);
    }

    @Override
    public GenericNode get(String key) throws JsonObjectException {
        throw new JsonObjectException("GET_ERROR_NOT_OBJECT " + key);
    }

    @Override
    public void put(String key, GenericNode value) throws JsonObjectException {
        throw new JsonObjectException("PUT_ERROR_NOT_OBJECT");
    }

    @Override
    public void remove(String key) {
        jsonArray.remove(Integer.parseInt(key));

    }

    @Override
    public String getType() {
        return "JSON_ARRAY";
    }

    @Override
    public String toString() {
        return jsonArray.toString();
    }

}

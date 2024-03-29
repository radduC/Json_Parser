package models;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;

public class JsonNumber implements GenericNode {
    private final String value;

    public JsonNumber(String value) {
        this.value = value;
    }

    public GenericNode get(int index) throws JsonArrayException {
        throw new JsonArrayException("ERROR_NOT_ARRAY ");
    }

    @Override
    public void add(JsonObject objectNode) throws JsonArrayException {
        throw new JsonArrayException("ERROR_NOT_ARRAY");

    }

    @Override
    public GenericNode get(String key) throws JsonObjectException {
        throw new JsonObjectException("GET_ERROR_NOT_OBJECT " + "\"" + key + "\"");
    }

    @Override
    public void put(String key, GenericNode value) throws JsonObjectException {
        throw new JsonObjectException("PUT_ERROR_NOT_OBJECT");

    }

    @Override
    public void remove(String key) throws JsonObjectException {
        throw new JsonObjectException("DEL_ERROR_NOT_OBJECT");

    }

    @Override
    public String getType() {
        return "JsonNumber";
    }

    @Override
    public String toString() {
        return "" + value;
    }
}

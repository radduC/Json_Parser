package models;

import exceptions.*;

public interface GenericNode {
    GenericNode get(int index) throws JsonArrayException;
    GenericNode get(String key) throws JsonObjectException, KeyNotFoundException;
    void add(JsonObject objectNode) throws JsonArrayException;
    void put(String key, GenericNode value) throws JsonObjectException;
    void remove(String key) throws JsonObjectException;
    String getType();
    String toString();
}

package tests;

import exceptions.JsonObjectException;
import models.JsonArray;
import models.JsonObject;
import models.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonArrayTest {
    JsonArray jsonArray = new JsonArray();

    @Test
    void add() {
        JsonObject jsonObject = new JsonObject();
        String key = "name";
        JsonString value = new JsonString("John Doe");
        jsonObject.put(key, value);
        jsonArray.add(jsonObject);
        assertEquals(jsonObject, jsonArray.get(0));
    }

    @Test
    void get() {
        JsonObject jsonObject = new JsonObject();
        String key = "name";
        JsonString value = new JsonString("John Doe");
        jsonObject.put(key, value);
        jsonArray.add(jsonObject);
        assertEquals(jsonObject, jsonArray.get(0));
    }

    @Test
    void testGet() {
        assertThrows(JsonObjectException.class, () -> jsonArray.get("someKey"));
    }

    @Test
    void put() {
        JsonObject jsonObject = new JsonObject();
        String key = "name";
        JsonString value = new JsonString("John Doe");

        assertThrows(JsonObjectException.class, () -> jsonArray.put(key, value));
    }

    @Test
    void remove() {
        JsonObject jsonObject = new JsonObject();
        String key = "name";
        JsonString value = new JsonString("John Doe");
        jsonObject.put(key, value);
        jsonArray.add(jsonObject);

        assertEquals(jsonObject, jsonArray.get(0));
        jsonArray.remove("0");

        assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(0));
    }

    @Test
    void getType() {
        assertEquals("JSON_ARRAY", jsonArray.getType());
    }
}
package tests;

import exceptions.JsonArrayException;
import exceptions.KeyNotFoundException;
import models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonObjectTest {
    JsonObject jsonObject = new JsonObject();


    @Test
    void getKey() throws KeyNotFoundException {
        jsonObject.put("key", new JsonBoolean("true"));
        jsonObject.put("age", new JsonNumber("21"));
        jsonObject.put("spouse", new JsonNull());

        assertThrowsExactly(KeyNotFoundException.class, () -> jsonObject.get("unknown"));

        assertEquals("true", jsonObject.get("key").toString());
        assertEquals("21", jsonObject.get("age").toString());
        assertEquals("null", jsonObject.get("spouse").toString());

    }

    @Test
    void put() throws KeyNotFoundException {
        String key = "name";
        GenericNode value = new JsonString("John Doe");
        jsonObject.put(key, value);
        assertEquals(value, jsonObject.get(key));
    }

    @Test
    void remove() {
        String key = "name";
        GenericNode value = new JsonString("John Doe");
        jsonObject.put(key, value);
        jsonObject.remove(key);
        assertThrows(KeyNotFoundException.class, () -> jsonObject.get(key));
    }

    @Test
    void getIndex() {
        assertThrows(JsonArrayException.class, () -> jsonObject.get(0));
        assertThrows(JsonArrayException.class, () -> jsonObject.get(1));
        assertThrows(JsonArrayException.class, () -> jsonObject.get(12));
    }

    @Test
    void add() {
        assertThrows(JsonArrayException.class, () -> jsonObject.add(new JsonObject()));
    }

    @Test
    void getType() {
        assertEquals("JSON_OBJECT", jsonObject.getType());
    }

    @Test
    void testToString() {
        String key = "name";
        GenericNode value = new JsonString("John Doe");
        jsonObject.put(key, value);
        assertEquals("{name=John Doe}", jsonObject.toString());
    }
}
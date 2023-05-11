package tests;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import models.JsonBoolean;
import models.JsonNull;
import models.JsonObject;
import models.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonStringTest {
    JsonString jsonString = new JsonString("value");

    @Test
    void get() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonString.get(0));
        assertThrowsExactly(JsonArrayException.class, () -> jsonString.get(1));
        assertThrowsExactly(JsonArrayException.class, () -> jsonString.get(10));
    }

    @Test
    void add() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonString.add(new JsonObject()));
    }

    @Test
    void getKeyTest() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.get("key"));
    }

    @Test
    void put() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.put("key", new JsonObject()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.put("key", new JsonNull()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.put("key", new JsonBoolean("23")));
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.put("key", new JsonBoolean("true")));
    }

    @Test
    void remove() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.remove("key"));
        assertThrowsExactly(JsonObjectException.class, () -> jsonString.remove(null));
    }

    @Test
    void getType() {
        assertEquals("JsonString", jsonString.getType());
    }

    @Test
    void testToString() {
        assertEquals("value", jsonString.toString());
    }
}
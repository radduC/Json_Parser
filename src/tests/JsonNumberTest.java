package tests;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import models.JsonBoolean;
import models.JsonNull;
import models.JsonNumber;
import models.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonNumberTest {
    JsonNumber jsonNumber = new JsonNumber("21");

    @Test
    void get() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonNumber.get(0));
        assertThrowsExactly(JsonArrayException.class, () -> jsonNumber.get(1));
        assertThrowsExactly(JsonArrayException.class, () -> jsonNumber.get(2));
        assertThrowsExactly(JsonArrayException.class, () -> jsonNumber.get(22));
    }

    @Test
    void add() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonNumber.add(new JsonObject()));
    }

    @Test
    void testGet() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.get("some key"));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.get("key"));
    }

    @Test
    void put() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.put("key", new JsonObject()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.put("key", new JsonNull()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.put("key", new JsonNumber("23")));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.put("key", new JsonBoolean("true")));
    }

    @Test
    void remove() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.remove("key"));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNumber.remove(null));
    }

    @Test
    void getType() {
        assertEquals("JsonNumber", jsonNumber.getType());
    }

    @Test
    void testToString() {
        assertEquals("21", jsonNumber.toString());
    }
}
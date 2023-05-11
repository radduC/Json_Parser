package tests;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import models.JsonBoolean;
import models.JsonNull;
import models.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class JsonBooleanTest {
    JsonBoolean jsonBoolean = new JsonBoolean(true);

    @Test
    void get() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonBoolean.get(0));
        assertThrowsExactly(JsonArrayException.class, () -> jsonBoolean.get(1));
        assertThrowsExactly(JsonArrayException.class, () -> jsonBoolean.get(2));
        assertThrowsExactly(JsonArrayException.class, () -> jsonBoolean.get(22));
    }

    @Test
    void add() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonBoolean.add(new JsonObject()));
    }

    @Test
    void getKeyTest() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.get("some key"));
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.get("key"));
    }

    @Test
    void put() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.put("key", new JsonObject()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.put("key", new JsonNull()));
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.put("key", new JsonBoolean("23")));
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.put("key", new JsonBoolean("true")));
    }

    @Test
    void remove() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.remove("key"));
        assertThrowsExactly(JsonObjectException.class, () -> jsonBoolean.remove(null));
    }

    @Test
    void getType() {
        assertEquals("JsonBoolean", jsonBoolean.getType());
    }

    @Test
    void testToString() {
        assertEquals("true", jsonBoolean.toString());
    }
}
package tests;

import exceptions.JsonArrayException;
import exceptions.JsonObjectException;
import models.JsonNull;
import models.JsonNumber;
import models.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonNullTest {
    JsonNull jsonNode = new JsonNull();

    @Test
    void getValue() {
        assertNull(jsonNode.getValue());
    }

    @Test
    void get() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonNode.get(2));
    }

    @Test
    void add() {
        assertThrowsExactly(JsonArrayException.class, () -> jsonNode.add(new JsonObject()));
    }

    @Test
    void getKey() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNode.get("number"));
    }

    @Test
    void put() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNode.put("someKey", new JsonNumber("23")));
        assertThrowsExactly(JsonObjectException.class, () -> jsonNode.put("someKey", null));
    }

    @Test
    void remove() {
        assertThrowsExactly(JsonObjectException.class, () -> jsonNode.remove("number"));
    }

    @Test
    void getType() {
        assertEquals("JsonNull", jsonNode.getType());
    }

    @Test
    void testToString() {
        assertEquals("null", jsonNode.toString());
    }
}
package tests;

import jsonparser.JsonDataStructure;
import models.JsonArray;
import models.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class JsonDataStructureTest {
    JsonDataStructure jsonDataStructure = new JsonDataStructure();

    @Test
    void process() {
    }

    @Test
    void parse() {
    }

    @Test
    void parseArray() throws Exception {
        Queue<String> tokens1 = new LinkedList<>();
        tokens1.add("{");
        tokens1.add("key");
        tokens1.add(":");
        tokens1.add("value");
        tokens1.add("}");
        tokens1.add("]");

        JsonArray jsonArray = jsonDataStructure.parseArray(tokens1);
        assertEquals("value", jsonArray.get(0).get("key").toString());

        Queue<String> tokens2 = new LinkedList<>();
        tokens2.add("invalid");
        jsonDataStructure.parseArray(tokens2);
    }

    @Test
    void parseObject() throws Exception {
        Queue<String> tokens1 = new LinkedList<>();
        tokens1.add("key");
        tokens1.add(":");
        tokens1.add("value");
        tokens1.add("}");

        JsonObject jsonObject1 = jsonDataStructure.parseObject(tokens1);
        assertEquals("value", jsonObject1.get("key").toString());

        Queue<String> tokens2 = new LinkedList<>();
        tokens2.add("key1");
        tokens2.add(":");
        tokens2.add("value1");
        tokens2.add(",");
        tokens2.add("key2");
        tokens2.add(":");
        tokens2.add("value2");
        tokens2.add("}");

        JsonObject jsonObject3 = jsonDataStructure.parseObject(tokens2);
        assertEquals("value1", jsonObject3.get("key1").toString());
        assertEquals("value2", jsonObject3.get("key2").toString());
    }
}
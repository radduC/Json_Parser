package tests;

import jsonparser.JsonTokenizerImpl;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class JsonTokenizerImplTest {
    private final JsonTokenizerImpl jsonTokenizer = new JsonTokenizerImpl();
    private Queue<String> tokens = new LinkedList<>();
    private final StringBuilder sb = new StringBuilder();

    @Test
    void tokenizeLinesTest() {
    }

    @Test
    void tokenizeLineTest() {
        tokens = jsonTokenizer.tokenizeLine("{:#,_-[{}]}");
        assertEquals(11, tokens.size());
        assertEquals("{", tokens.poll());
        assertEquals(":", tokens.poll());
        assertEquals("#", tokens.poll());
    }

    @Test
    void tokenizeNullTest() {
    }

    @Test
    void tokenizeNumberTest() {
    }

    @Test
    void tokenizeBooleanTest() {
        jsonTokenizer.tokenizeBoolean("\"active\": \"true\"", tokens, sb, 11);
        sb.setLength(0);
        jsonTokenizer.tokenizeBoolean("\"false\"", tokens, sb, 1);

        System.out.println(tokens);

        assertEquals(2, tokens.size());
        assertEquals("true", tokens.poll());
        assertEquals("false", tokens.poll());


    }

    @Test
    void tokenizeStringTest() {
        int index = jsonTokenizer.tokenizeString("\"name\": \"John Doe\"", tokens, sb, 0);
        System.out.println(tokens);

        assertEquals(5, index);
        assertEquals(1, tokens.size());

        index = jsonTokenizer.tokenizeString("[1, 2, { \"bar\": 2 }]", tokens, sb, 9);
        System.out.println(tokens);

        assertEquals(13, index);
        assertEquals(2, tokens.size());

        assertEquals("name", tokens.poll());
        assertEquals("bar", tokens.poll());
    }
}
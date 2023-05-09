package tests;

import jsonparser.JsonTokenizerImpl;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class JsonTokenizerImplTest {
    private final JsonTokenizerImpl jsonTokenizer = new JsonTokenizerImpl();
    private final Queue<String> tokens = new LinkedList<>();
    private final StringBuilder sb = new StringBuilder();

    @Test
    void tokenizeLinesTest() {
    }

    @Test
    void tokenizeLineTest() {
    }

    @Test
    void tokenizeNullTest() {
    }

    @Test
    void tokenizeNumberTest() {
    }

    @Test
    void tokenizeBooleanTest() {
    }

    @Test
    void tokenizeStringTest() {
        int index = jsonTokenizer.tokenizeString("\"name\": \"John Doe\"", tokens, sb, 6);
        System.out.println(tokens);

        assertEquals(8, index);
        assertEquals(1, tokens.size());
        assertEquals(" ", tokens.poll());
        assertNull(tokens.poll());
    }
}
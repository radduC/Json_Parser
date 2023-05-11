package tests;

import jsonparser.JsonReaderImpl;
import jsonparser.JsonTokenizerImpl;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class JsonTokenizerImplTest {
    private final JsonTokenizerImpl jsonTokenizer = new JsonTokenizerImpl();
    private final JsonReaderImpl jsonReader = new JsonReaderImpl();
    private Queue<String> tokens = new LinkedList<>();
    private final StringBuilder sb = new StringBuilder();

    @Test
    void tokenizeLinesTest() throws FileNotFoundException {
        Queue<String> lines = jsonReader.readFile("resources/input/example.json");
        tokens = jsonTokenizer.tokenizeLines(lines);

        assertEquals("[", tokens.poll());
        assertEquals("{", tokens.poll());
        assertEquals("firstName", tokens.poll());
        assertEquals(":", tokens.poll());
        assertEquals("John", tokens.poll());

        for (int i = 1; i <= 7; i++) {
            tokens.poll();

        }

        assertEquals("true", tokens.poll());

        for (int i = 1; i <= 10; i++) {
            tokens.poll();
        }

        assertEquals("21 2nd Street", tokens.poll());
    }

    @Test
    void tokenizeLineTest() {
        tokens = jsonTokenizer.tokenizeLine("{:#,_-[{}]}");
        assertEquals(11, tokens.size());
        assertEquals("{", tokens.poll());
        assertEquals(":", tokens.poll());
        assertEquals("#", tokens.poll());

        tokens = jsonTokenizer.tokenizeLine("invalid");
        System.out.println(tokens);
    }

    @Test
    void tokenizeNullTest() {
        int i = jsonTokenizer.tokenizeNull("null", tokens, sb, 0);
        assertEquals(2, i);

        sb.setLength(0);
        i = jsonTokenizer.tokenizeNull("\"spouse\": null", tokens, sb, 10);

        assertEquals(12, i);
        assertEquals(2, tokens.size());
        assertEquals("null", tokens.poll());
        assertEquals("null", tokens.poll());
    }

    @Test
    void tokenizeNumberTest() {
        int i = jsonTokenizer.tokenizeNumber("123a", tokens, sb, 0);
        assertEquals(2, i);
        sb.setLength(0);

        i = jsonTokenizer.tokenizeNumber("\"age\": 25\"", tokens, sb, 7);

        assertEquals(8, i);
        assertEquals(2, tokens.size());
        assertEquals("123", tokens.poll());
        assertEquals("25", tokens.poll());
    }

    @Test
    void tokenizeBooleanTest() {
        int i = jsonTokenizer.tokenizeBoolean("\"active\": \"true\"", tokens, sb, 11);
        assertEquals(13, i);
        sb.setLength(0);

        i = jsonTokenizer.tokenizeBoolean("\"false\"", tokens, sb, 1);
        assertEquals(4, i);

        assertEquals(2, tokens.size());
        assertEquals("true", tokens.poll());
        assertEquals("false", tokens.poll());


    }

    @Test
    void tokenizeStringTest() {
        int index = jsonTokenizer.tokenizeString("\"name\": \"John Doe\"", tokens, sb, 0);

        assertEquals(5, index);
        assertEquals(1, tokens.size());

        sb.setLength(0);
        index = jsonTokenizer.tokenizeString("[1, 2, { \"bar\": 2 }]", tokens, sb, 9);

        assertEquals(13, index);
        assertEquals(2, tokens.size());

        assertEquals("name", tokens.poll());
        assertEquals("bar", tokens.poll());
    }
}
package jsonparser;

import java.util.Queue;

public interface JsonTokenizer {
    Queue<String> tokenizeLine(String line);

    Queue<String> tokenizeLines(Queue<String> lines);
}

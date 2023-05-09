package jsonparser;

import java.util.LinkedList;
import java.util.Queue;

public class JsonTokenizerImpl implements JsonTokenizer {
    private static final String DELIMITERS = "{}[]:,#_-";
    private final Queue<String> tokens = new LinkedList<>();

    @Override
    public Queue<String> tokenizeLines(Queue<String> lines) {
        while (!lines.isEmpty()) {
            String line = lines.poll();
            Queue<String> token = tokenizeLine(line);
            tokens.addAll(token);
        }

        return tokens;
    }

    @Override
    public Queue<String> tokenizeLine(String line) {
        Queue<String> tokens = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            sb.setLength(0);
            char c = line.charAt(i);

            if (DELIMITERS.indexOf(c) != -1) {
                tokens.offer(String.valueOf(c));

            } else if (c == '"') {
                i = tokenizeString(line, tokens, sb, i);

            } else if (Character.toLowerCase(c) == 't' || c == 'f') {
                i = tokenizeBoolean(line, tokens, sb, i);

            } else if (Character.isDigit(c) || c == '-') {
                i = tokenizeNumber(line, tokens, sb, i);

            } else if (Character.toLowerCase(c) == 'n') {
                i = tokenizeNull(line, tokens, sb, i);
            }
        }

        return tokens;
    }

    private static int tokenizeNull(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while (line.charAt(index) != 'l') {
            sb.append(line.charAt(index++));
        }

        sb.append(line.charAt(index++));
        sb.append(line.charAt(index));
        tokens.offer(sb.toString());
        return --index;
    }

    private static int tokenizeNumber(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while ((line.charAt(index) >= '0' && line.charAt(index) <= '9') || line.charAt(index) == '-') {
            sb.append(line.charAt(index++));
        }

        tokens.offer(sb.toString());
        return --index;
    }

    private static int tokenizeBoolean(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i;

        while (line.charAt(index) != 'e') {
            sb.append(line.charAt(index++));
        }

        sb.append(line.charAt(index--));
        tokens.offer(sb.toString());
        return index;
    }

    private static int tokenizeString(String line, Queue<String> tokens, StringBuilder sb, int i) {
        int index = i + 1;

        while (line.charAt(index) != '"') {
            sb.append(line.charAt(index++));
        }

        tokens.offer(sb.toString());
        sb.setLength(0);
        return index;
    }
}

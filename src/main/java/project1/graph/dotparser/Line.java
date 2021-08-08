package project1.graph.dotparser;

import lombok.Getter;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Line {
    // Regex patterns to match GraphViz content
    private static final Pattern digraphPattern = Pattern.compile("digraph\\s*\"(.*)\" \\{");
    private static final Pattern nodePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");
    private static final Pattern edgePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*->\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");

    private final LineType type;
    private final Matcher matcher;

    public enum LineType {
        DIGRAPH (digraphPattern),
        NODE (nodePattern),
        EDGE (edgePattern);

        @Getter private final Pattern pattern;
        LineType(Pattern p) {
            this.pattern = p;
        }
    }

    public static class UnknownSyntaxException extends Exception {
        public UnknownSyntaxException(String errorMessage) {
            super(errorMessage);
        }
    }

    public Line(String line) throws UnknownSyntaxException {
        // Patterns corresponding to LineType enum values
        for (LineType t : LineType.values()) {
            Matcher m = t.getPattern().matcher(line);

            // If a matcher matches, return a Line object
            if (m.find()) {
                this.type = t;
                this.matcher = m;
                return;
            }
        }

        throw new UnknownSyntaxException(String.format("The line '%s' does not match any known syntax!", line));
    }
}
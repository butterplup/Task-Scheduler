package Project1;
import Project1.graph.DotParser;

import java.io.IOException;

/**
 * Entry point for the application
 */

public class Run {
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            String filename = args[0];
            DotParser.parse(filename);
        } else {
            System.out.printf("Usage: java -jar Project1.jar DOT_FILE%n");
        }
    }
}

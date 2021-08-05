package project1;
import project1.graph.DotParser;
import project1.graph.Graph;

import java.io.IOException;

/**
 * Entry point for the application
 */

public class Run {
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            String filename = args[0];
            Graph g = DotParser.parse(filename);
            g.print();

            String inputName = filename.substring(0, filename.lastIndexOf("."));
            DotParser.saveToFile(g, String.format("%s-output.dot", inputName));
        } else {
            System.out.printf("Usage: java -jar project1.jar DOT_FILE%n");
        }
    }
}

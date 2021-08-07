package project1;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.graph.DotParser;
import project1.graph.Graph;

import java.io.IOException;

/**
 * Entry point for the application
 */
public class Run {
    public static void main(String[] args) throws IOException {
        try {
            ArgsParser argsParser = new ArgsParser(args);

            String filename = argsParser.getFilename();
            Graph g = DotParser.parse(filename);

            g.sort();
            Schedule s = SequentialDFS.generateOptimalSchedule(g, 4);
            s.printSchedule();

            String outputFilename = argsParser.getOutputFilename();
            DotParser.saveToFile(g, outputFilename);

        } catch (IOException e) {
            e.printStackTrace();
            //TODO Make more explicit usage message?
            System.out.printf("Usage: java -jar project1.jar DOT_FILE P [OPTIONAL args]%n ");
            throw new IOException(e);
        }

    }
}

package project1;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.graph.dotparser.Parser;
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
            Graph g = Parser.parse(filename);

            g.sort();
            Schedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount());
            s.printSchedule();

            String outputFilename = argsParser.getOutputFilename();
            Parser.saveToFile(g, outputFilename);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            //TODO Make more explicit usage message?
            System.out.printf("Usage: java -jar project1.jar DOT_FILE P [OPTIONAL args]%n ");
            throw new IOException(e);
        }

    }
}

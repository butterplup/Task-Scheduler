package project1;
import project1.algorithm.PartialSchedule;
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
            // Parses and stores arguments in an object
            ArgsParser argsParser = new ArgsParser(args);

            String filename = argsParser.getInputFilename();
            Graph g = Parser.parse(filename);

            PartialSchedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount(), argsParser.getParallelCoreCount());
            s.printSchedule();
            System.out.println(s);

            String outputFilename = argsParser.getOutputFilename();
            Parser.saveToFile(g, outputFilename);

        } catch (IOException e) {
            // Relays message of specific error as identified in the ArgsParser class
            System.out.println(e.getMessage());
            // General usage message to inform user when they have not followed it
            System.out.printf("Usage: java -jar project1.jar DOT_FILE P [OPTIONAL args]%n ");
            throw new IOException(e);
        }

    }
}
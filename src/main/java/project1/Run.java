package project1;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.algorithm.ThreadAnalytics;
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
            ArgsParser argsParser = ArgsParser.getInstance(args);
            // Initialises ThreadAnalytics for the desired core count
            ThreadAnalytics.getInstance(argsParser.getParallelCoreCount());

            if (argsParser.isVisualise()) {
                //needs to start a new thread and start the visualisation
                Visualiser.Main(args);

            } else {
                String filename = argsParser.getFilename();
                Graph g = Parser.parse(filename);

                Schedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount());
                s.printSchedule();

                String outputFilename = argsParser.getOutputFilename();
                Parser.saveToFile(g, outputFilename);
            }
        } catch (IOException e) {
            // Relays message of specific error as identified in the ArgsParser class
            System.out.println(e.getMessage());
            // General usage message to inform user when they have not followed it
            System.out.printf("Usage: java -jar project1.jar DOT_FILE P [OPTIONAL args]%n ");
            throw new IOException(e);
        }

    }
}

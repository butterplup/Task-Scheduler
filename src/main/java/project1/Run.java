package project1;
import project1.algorithm.PartialSchedule;
import project1.algorithm.SequentialDFS;
import project1.algorithm.ThreadAnalytics;
import project1.graph.dotparser.Parser;
import project1.graph.Graph;

import java.io.IOException;

/**
 * Entry point for the application
 */
public class Run implements Runnable {
    public static void main(String[] args) throws IOException {
        /* Initialise singletons
            - ArgsParser
            - ThreadAnalytics
         */

        ArgsParser argsParser;
        try {
            argsParser = ArgsParser.getInstance(args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.printf("Usage: java -jar project1.jar DOT_FILE P [OPTIONAL args]%n ");
            throw new IOException(e);
        }

        ThreadAnalytics.getInstance(argsParser.getParallelCoreCount());

        if (argsParser.isVisualise()) {
            // Start a new thread to run the algorithm on
            new Thread(new Run()).start();

            // Use main thread to spin up the visualisation
            Visualiser.Main(args);
        } else {
            // Run in main thread
            new Run().run();
        }
    }

    @Override
    public void run() {
        ArgsParser argsParser = ArgsParser.getInstance();
        String filename = argsParser.getInputFilename();

        Graph g;
        try {
            g = Parser.parse(filename);
        } catch (IOException e) {
            // Failure loading file, exit code 1
            e.printStackTrace();
            System.exit(1);
            return;
        }

        PartialSchedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount(), argsParser.getParallelCoreCount());
        System.out.println(s.printSchedule());

        String outputFilename = argsParser.getOutputFilename();

        try {
            Parser.saveToFile(g, outputFilename);
        } catch (IOException e) {
            // Failure saving file, exit code 2
            e.printStackTrace();
            System.exit(2);
        }
    }
}

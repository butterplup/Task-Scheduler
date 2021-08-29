package project1;

import java.io.IOException;
import lombok.Getter;

/**
 * Class ArgsParser takes the command line arguments and parses them in
 * for use by other classes
 */
public class ArgsParser {
    @Getter private final String inputFilename;
    @Getter private String outputFilename;
    // The number of processors that the tasks can be scheduled on
    @Getter private final int processorCount;
    @Getter private int parallelCoreCount = 1;
    // Whether visualisation should be enabled
    @Getter private boolean visualise = false;

    // A single instance of ArgsParser
    private static ArgsParser instance;

    /**
     * Constructor for an ArgsParser object; takes an array of arguments and stores them
     * @param args - the command line arguments
     * @throws IOException
     */
    private ArgsParser(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IOException("Mandatory arguments missing. Usage: java -jar scheduler.jar INPUT.dot P [OPTION]. See README for more OPTION choices.");
        }

        this.inputFilename = args[0];
        try {
            this.outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf(".")) + "-output.dot";
        } catch (IndexOutOfBoundsException e) {
            // Occurs when String#lastIndexOf finds no matches and returns -1 causing String#substring to access out of bounds
            throw new IOException("DOT_FILE missing .dot extension in command line argument");
        }

        try {
            this.processorCount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IOException("Processor count P not given as an integer");
        }

        // Assign optional arguments (if any)
        for (int i = 2; i < args.length; i++) {
            try {
                switch (args[i]) {
                    case "-p":
                        try {
                            this.parallelCoreCount = Integer.parseInt(args[i+1]);
                        } catch (NumberFormatException e) {
                            // Occurs when the arg after "-p" is not a valid integer used for the count of parallel cores to be used
                            throw new IOException("Parallel core count (for -p) not given as an integer");
                        }
                        i++;
                        break;
                    case "-v":
                        // Visualisation turned on
                        this.visualise = true;
                        break;
                    case "-o":
                        // Rename output file
                        this.outputFilename = args[i+1] + ".dot";
                        i++;
                        break;
                    default:
                        // Current arg does not match any known case
                        throw new IOException("Supplied argument does not match any known optional args");
                }
            } catch (IndexOutOfBoundsException e) {
                // Occurs when there is no arg after "-p" or "-o" despite one being needed
                throw new IOException("No arg given following at least one optional arg -p or -o");
            }
        }
    }

    /**
     * Instantiate an instance of ArgsParser, and return it.
     * @param args User inputted arguments.
     * @return The single ArgsParser instance.
     * @throws IOException If arguments are invalid
     */
    public static ArgsParser getInstance(String[] args) throws IOException {
        instance = new ArgsParser(args);
        return instance;
    }

    /**
     * Return the single ArgsParser instance.
     * @return The single ArgsParser instance.
     */
    public static ArgsParser getInstance() {
        return instance;
    }

}
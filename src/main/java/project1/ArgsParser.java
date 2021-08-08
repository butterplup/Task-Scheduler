package project1;

import java.io.IOException;
import lombok.Getter;

/**
 * Class ArgsParser takes the command line arguments and stores them for use by other classes
 */
public class ArgsParser {

    // Stored settings for the algorithm
    /**
     * The filename of the input dot file including extension
     */
    @Getter private final String filename;
    /**
     * The filename of the output dot file including extension
     */
    @Getter private String outputFilename;
    /**
     * The number of processors that the tasks can be scheduled on
     */
    @Getter private final int processorCount;
    /**
     * The number of cores/threads for the program to be run on
     */
    @Getter private int parallelCoreCount = 1;
    /**
     * Whether visualisation should be enabled
     */
    @Getter private boolean visualise = false;

    /**
     * Goes through the command line arguments and assigns them to attributes to be stored
     * @param args - the cmd line arguments
     * @throws IOException - when cmd line args do not meet expected format
     */
    ArgsParser(String[] args) throws IOException {

        if (args.length < 2) {
            throw new IOException("Mandatory arguments missing, see usage");
        } try {
            // Mandatory to set filename of dot file with input graph
            String filename = args[0];
            this.filename = filename;
            String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
            this.outputFilename = filenameNoExtension + "-output.dot"; // Default output filename
            // The number of processors to schedule the input graph on
            this.processorCount = Integer.parseInt(args[1]);

        } catch (IndexOutOfBoundsException e) {
            // Occurs when String#lastIndexOf finds no matches and returns -1 causing String#substring to access out of bounds
            throw new IOException("DOT_FILE missing .dot extension in cmd line argument");
        } catch (NumberFormatException e) {
            // Occurs when the second arg is not a valid integer used for the processor count
            throw new IOException("Processor count P not given as an integer");
        } try {
            // Loop through remaining optional arguments and assign if different from default
            for (int i=2; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-p":
                        this.parallelCoreCount = Integer.parseInt(args[i+1]);
                        i++;
                        break;
                    case "-v":
                        // Visualisation turned on
                        this.visualise = true;
                        break;
                    case "-o":
                        // Rename output file to given arg
                        this.outputFilename = args[i+1] + ".dot";
                        i++;
                        break;
                    default:
                        // Current arg does not match any known case
                        throw new IOException("Supplied argument does not match any known optional args");
                }
            }
        } catch (NumberFormatException e) {
            // Occurs when the arg after "-p" is not a valid integer used for the count of parallel cores to be used
            throw new IOException("Parallel core count after -p not given as an integer");
        } catch (IndexOutOfBoundsException e) {
            // Occurs when there is no arg after "-p" or "-o" despite one being needed
            throw new IOException("No arg given following at least one optional arg -p or -o");
        }
    }
}

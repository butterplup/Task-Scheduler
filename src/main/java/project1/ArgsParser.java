package project1;

import java.io.IOException;

/**
 * Class ArgsParser takes the command line arguments and stores them for use by other classes
 */
public class ArgsParser {

    // Default settings for the algorithm
    private final String filename; //TODO should set a default?
    private String outputFilename; //TODO should set a default?
    private final int processorCount; //TODO should set a default?
    private int parallelCoreCount = 1;
    private boolean visualise = false;

    /**
     * Goes through the command line arguments and assigns them to attributes to be stored
     * @param args - the cmd line arguments
     * @throws IOException - when cmd line args do not meet expected format
     */
    ArgsParser(String[] args) throws IOException {
        try {
            // Perhaps add something to check whether it is valid string
            String filename = args[0];
            this.filename = filename;
            //TODO Default output name set (could change to boolean "defaultOutputName = true")
            String filenameNoExtension = filename.substring(0,filename.lastIndexOf("."));
            this.outputFilename = filenameNoExtension + "-output.dot";
            // The number of processors to schedule the input graph on
            this.processorCount = Integer.parseInt(args[1]);
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
                        throw new IOException("Invalid argument given");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Something in terms of how the cmd line args are presented is incorrect
            throw new IOException("Invalid argument(s) given");
        }
    }

    // GETTER METHODS

    public boolean isVisualise() {
        return this.visualise;
    }

    public int getParallelCoreCount() {
        return this.parallelCoreCount;
    }

    public int getProcessorCount() {
        return this.processorCount;
    }

    public String getFilename() {
        return filename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }
}

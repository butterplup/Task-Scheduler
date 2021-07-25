package Project1;

/**
 * Entry point for the application
 */

public class Run {
    public static void main(String[] args) {
        if (args.length == 1) {
            String filename = args[0];
            System.out.printf("Reading from %s...%n", filename);
        } else {
            System.out.printf("Usage: java -jar Project1.jar DOT_FILE%n");
        }
    }
}

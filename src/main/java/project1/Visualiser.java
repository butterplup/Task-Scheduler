package project1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project1.algorithm.PartialSchedule;
import project1.algorithm.SequentialDFS;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;

import java.io.IOException;

/**
 * This class extends the JavaFX application class to create and run the GUI
 * which displays the state of the search algorithm
 */
public class Visualiser extends Application {

    /**
     * The start method loads in the fxml file, sets up basic attributes of the stage
     * and then runs the algorithm on a new thread
     * @param primaryStage - the stage which contains all the UI shown to the user
     * @throws Exception - may be caused when input files do not exist or output files already exist
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Team 2: Electric Boogaloo");

        // Start a new thread to run the algorithm on
        new Thread(() -> {
            ArgsParser argsParser = ArgsParser.getInstance();
            String filename = argsParser.getInputFilename();
            Graph g = null;

            try {
                // Attempt to parse in a file with the given filename
                g = Parser.parse(filename);
                // Run the algorithm on this graph
                PartialSchedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount(), argsParser.getParallelCoreCount());
                s.printSchedule();
                // Save this optimal schedule to a file
                String outputFilename = argsParser.getOutputFilename();
                Parser.saveToFile(g, outputFilename);
            // Occurs when input is not found, or output file already exists
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }).start();

        primaryStage.show();
    }

    /**
     * The entry point to this class simply launches the JavaFX application
     * @param args - unused by the JavaFX application
     */
    public static void Main(String[] args) {
        launch();
    }
}


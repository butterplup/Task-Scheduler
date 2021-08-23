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
        new Thread(new Run()).start();
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


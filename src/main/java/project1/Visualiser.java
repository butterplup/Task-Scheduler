package project1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;

import java.io.IOException;

public class Visualiser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Team 2: Electric Boogaloo");

        // Start new thread and start algo
        new Thread(() -> {
            ArgsParser argsParser = ArgsParser.getInstance();
            String filename = argsParser.getFilename();
            Graph g = null;

            try {
                g = Parser.parse(filename);

                Schedule s = SequentialDFS.generateOptimalSchedule(g, argsParser.getProcessorCount());
                s.printSchedule();

                String outputFilename = argsParser.getOutputFilename();
                Parser.saveToFile(g, outputFilename);

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }

        }).start();

        primaryStage.show();
    }

    public static void Main(String[] args) {
        launch();
    }
}


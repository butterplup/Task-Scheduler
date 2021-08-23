package project1.visualisation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import project1.ArgsParser;
import project1.algorithm.ThreadAnalytics;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;
import project1.visualisation.tiles.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class controls how the UI is updated in Main.fxml
 * It updates the visualised elements of the algorithm as it is calculated
 */
public class MainController {
    @FXML private Text bestScheduleTime;
    @FXML private Text TimeText;
    @FXML private Text StatusText;
    @FXML private TextField inputField;
    @FXML private TextField nodeField;
    @FXML private TextField outputField;

    // Boxes for charts
    @FXML private VBox ganttBox;
    @FXML private VBox CpuBox, memBox;
    @FXML private HBox totalThreadBox, activeThreadsBox;

    // Tiles
    private List<VTile> tiles;
    private ScheduleGantt scheduleGantt;

    private long startTime;
    private final ThreadAnalytics threadData = ThreadAnalytics.getInstance();
    private Timeline autoUpdater;

    /**
     * Called following object construction to set up FXML fields and their initial state
     * Also begins the event polling loop and the algorithm timer
     */
    @FXML
    public void initialize() throws IOException {
        tiles = new ArrayList<>();
        ArgsParser argsParser = ArgsParser.getInstance(); // ArgsParser object stores user input data

        // Strip directory info from file path display
        inputField.setText(new File(argsParser.getInputFilename()).getName());
        outputField.setText(new File(argsParser.getOutputFilename()).getName());

        // creates the graph to obtain the number of nodes from it
        Graph g = Parser.parse(argsParser.getInputFilename());
        nodeField.setText(String.valueOf(g.getNodes().size()));

        // set up display elements as tiles
        tiles.add(new MemTile(memBox));
        tiles.add(new CPUTile(CpuBox));
        tiles.add(new TotalThreadsTile(totalThreadBox, threadData));
        tiles.add(new TotalActiveTile(activeThreadsBox, threadData));

        // Set up best schedule gantt graph
        scheduleGantt = new ScheduleGantt(ganttBox, ganttBox.getPrefHeight(), argsParser.getProcessorCount());
        ganttBox.setStyle("-fx-background-color: transparent");

        // start polling
        startPolling();
    }

    /**
     * Called following proper initialisation of all screen elements to continuously update the data for the user
     */
    private void startPolling() {
        startTime = System.currentTimeMillis();

        // Trigger update every 50ms
        autoUpdater = new Timeline(new KeyFrame(Duration.millis(50), event -> update()));
        autoUpdater.setCycleCount(Animation.INDEFINITE);
        autoUpdater.play();
    }

    private void update() {
        // Update timer
        long currentTime = System.currentTimeMillis();
        int roundedTime = (int) ((currentTime-startTime)/1000);
        TimeText.setText(String.valueOf(roundedTime));

        // checks if the algo is done, then runs the update one more time after its finished
        if (threadData.numThreadsAlive() == 0) {
            //if the threadData is finished set the running text to be done
            StatusText.setStyle("-fx-fill: rgb(15,150,100)");
            StatusText.setText("Done");

            // Run once more, then stop updating
            autoUpdater.stop();
        }

        // Update tiles
        tiles.forEach(VTile::update);

        // If a best schedule exists, display it
        if (threadData.getBestSchedule() != null) {
            scheduleGantt.update(threadData.getBestSchedule());
            bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime()));
        }
    }
}

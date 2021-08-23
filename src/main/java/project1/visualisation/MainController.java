package project1.visualisation;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import project1.ArgsParser;
import project1.algorithm.ThreadAnalytics;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;
import project1.visualisation.Tiles.*;

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

    //boolean to determine if the updater needs to trigger again
    private boolean runAgain = true;

    //initialises the fields that will hold all the data for the gui
    private final ThreadAnalytics threadData = ThreadAnalytics.getInstance(); // stores info on best schedule and threads

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

        // set up display elements as Tiles
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
        long startTime = System.currentTimeMillis();

        // timeline that adds a new keyframe every 50 milliseconds
        Timeline autoUpdater = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Update timer
            long currentTime = System.currentTimeMillis();
            int roundedTime = (int) ((currentTime-startTime)/1000);
            TimeText.setText(String.valueOf(roundedTime));

            // checks if the algo is done, then runs the update one more time after its finished
            if (threadData.numThreadsAlive() == 0) {
                //if the threadData is finished set the running text to be done
                StatusText.setStyle("-fx-fill: rgb(15,150,100)");
                StatusText.setText("Done");

                // checks if event loop has run once more following algorithm finishing
                // so idle cpu and mem loads and optimal schedule are updated on GUI
                if (!runAgain) {
                    return;
                }

                // since it has run once, set to false
                runAgain = false;
            }

            // Update tiles
            for (VTile v : tiles) {
                v.update();
            }

            //if a best schedule exists, display on screen
            if(threadData.getBestSchedule() != null){
                // updateBestScheduleGantt(threadData.getBestSchedule());
                scheduleGantt.update(threadData.getBestSchedule());
                // sets the current best time to the the string of the global best time (int)
                bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime())); // will always be an int as is initialised to 999
            }
        }));

        //sets the cycle count to be indefinite so it never stops then starts the auto-updater
        autoUpdater.setCycleCount(Animation.INDEFINITE);
        autoUpdater.play();
    }
}

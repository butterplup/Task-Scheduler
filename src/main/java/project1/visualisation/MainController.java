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
import project1.visualisation.tiles.gantt.ScheduleGantt;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
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
    private long startTime;
    private final ThreadAnalytics threadData = ThreadAnalytics.getInstance();
    private Timeline autoUpdater;

    /**
     * Called following object construction to set up FXML fields and their initial state
     * Also begins the event polling loop and the algorithm timer
     */
    @FXML
    public void initialize() throws IOException {
        ArgsParser argsParser = ArgsParser.getInstance();

        // Strip directory info from file path display
        inputField.setText(new File(argsParser.getInputFilename()).getName());
        outputField.setText(new File(argsParser.getOutputFilename()).getName());

        tiles = new ArrayList<>();
        tiles.add(
                new SystemTile(memBox,
                        "Current Memory Usage",
                        "MB",
                        (int) (Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0)),
                        () -> ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d)))
        );

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        tiles.add(
                new SystemTile(CpuBox,
                        "Current CPU Usage",
                        "%",
                        100,
                        () -> osBean.getSystemLoadAverage() * 10d)
        );

        tiles.add(
                new ThreadTile(totalThreadBox,
                        "Total Threads Created",
                        threadData::numThreadsSpawned));

        tiles.add(
                new ThreadTile(activeThreadsBox,
                        "Active Threads",
                        threadData::numThreadsAlive));

        // Set up best schedule gantt graph
        tiles.add(
                new ScheduleGantt(ganttBox,
                        ganttBox.getPrefHeight(),
                        argsParser.getProcessorCount(),
                        threadData));

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

        // Display best schedule time
        if (threadData.getBestSchedule() != null) {
            bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime()));
            nodeField.setText(Integer.toString(threadData.getBestSchedule().getNodesVisited()));
        }
    }
}

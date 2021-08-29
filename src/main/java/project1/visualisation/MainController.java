package project1.visualisation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import project1.ArgsParser;
import project1.algorithm.ThreadAnalytics;
import project1.visualisation.tiles.*;
import project1.visualisation.tiles.gantt.ScheduleGantt;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class controls how the UI is updated in Main.fxml
 * It updates the visualised elements of the algorithm as it is calculated
 */
public class MainController {
    @FXML private Text bestScheduleTime, timeText, statusText, nodeField;
    @FXML private TextField inputField, outputField;

    // Boxes for charts
    @FXML private VBox ganttBox, cpuBox, memBox;
    @FXML private HBox totalThreadBox, activeThreadsBox;

    private List<VTile> tiles;
    private long startTime;
    private final ThreadAnalytics threadData = ThreadAnalytics.getInstance();
    private Timeline autoUpdater;

    /**
     * Called following object construction to set up FXML fields and their initial state
     * Also begins the event polling loop and the algorithm timer
     * @throws IOException if invalid arguments are passed
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
                        "MB",
                        Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0),
                        () -> ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d)))
        );

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        tiles.add(
                new SystemTile(cpuBox,
                        "%",
                        100.0,
                        osBean::getProcessCpuLoad)
        );

        tiles.add(
                new ThreadTile(totalThreadBox,
                        threadData::numThreadsSpawned));

        tiles.add(
                new ThreadTile(activeThreadsBox,
                        threadData::numThreadsAlive));

        // Set up best schedule gantt graph
        tiles.add(
                new ScheduleGantt(ganttBox,
                        ganttBox.getPrefHeight(),
                        argsParser.getProcessorCount(),
                        threadData));

        ganttBox.setStyle("-fx-background-color: transparent");

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

    /**
     * Start the video background in the second child of the root StackPane
     */
    private void startVideo() {
        MediaPlayer player;
        try {
            player = new MediaPlayer( new Media(getClass().getResource("/media/bg720slow.mp4").toURI().toString()));
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
            return;
        }

        // Loop background and sound media
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();

        // Add to MediaView (Second child of root)
        MediaView mV = (MediaView) cpuBox.getScene().getRoot().getChildrenUnmodifiable().get(1);
        mV.setMediaPlayer(player);
    }

    /**
     * Update all visualisation tiles
     */
    private void update() {
        // Update timer
        long currentTime = System.currentTimeMillis();
        int roundedTime = (int) ((currentTime-startTime)/1000);
        timeText.setText(String.valueOf(roundedTime));

        // Checks if the algorithm is done, then runs the update one more time after its finished
        if (threadData.numThreadsAlive() == 0) {
            // If the threadData is finished set the running text to be done
            statusText.setStyle("-fx-fill: #39FF14;-fx-effect: dropshadow(gaussian, #39FF14, 5, 0, 0, 0)");
            statusText.setText("Done");

            // Run once more, then stop updating
            autoUpdater.stop();

            startVideo();
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

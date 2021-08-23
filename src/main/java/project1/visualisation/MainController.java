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
import com.sun.management.OperatingSystemMXBean;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;
import project1.visualisation.Tiles.CPUTile;
import project1.visualisation.Tiles.MemTile;
import project1.visualisation.Tiles.SystemTile;
import project1.visualisation.Tiles.ThreadTiles;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * This class controls how the UI is updated in Main.fxml
 * It updates the visualised elements of the algorithm as it is calculated
 */
public class MainController {

    //the max number of elements it can hold and the number that can be removed at ounce
    private final int  MAX_ELEMENTS = 500;
    private final int REMOVAL_VALUE = 50;

    @FXML private Text bestScheduleTime;
    @FXML private Text TimeText;
    @FXML private Text StatusText;
    @FXML private VBox ganttBox;
    @FXML private HBox totalThreadBox;
    @FXML private HBox activeThreadsBox;
    @FXML private TextField inputField;
    @FXML private TextField nodeField;
    @FXML private TextField outputField;

    @FXML private VBox CpuBox;
    @FXML private VBox memBox;

    //tiles
    private SystemTile memoryTile;
    private SystemTile cpuTile;
    private Tile totalThreadsTile;
    private Tile totalActiveTile;

    private ScheduleGantt scheduleGantt;
    private GanttChart<Number,String> chart;

    //timer fields
    private double startTime;
    private double currentTime;

    //boolean to determine if the updater needs to trigger again
    private boolean runAgain = true;

    //timeline for the poller
    private Timeline timerHandler;

    //initialises the fields that will hold all the data for the gui
    private final ThreadAnalytics threadData = ThreadAnalytics.getInstance(); // stores info on best schedule and threads
    private final ArgsParser argsParser = ArgsParser.getInstance(); // ArgsParser object stores user input data

    /**
     * Called following object construction to set up FXML fields and their initial state
     * Also begins the event polling loop and the algorithm timer
     */
    @FXML
    public void initialize() throws IOException {
        // Strip directory info from file path display
        inputField.setText(new File(argsParser.getInputFilename()).getName());
        outputField.setText(new File(argsParser.getOutputFilename()).getName());

        // creates the graph to obtain the number of nodes from it
        Graph g = Parser.parse(argsParser.getInputFilename());
        nodeField.setText(String.valueOf(g.getNodes().size()));

        // set up display elements as Tiles
        memoryTile = new MemTile();
        cpuTile = new CPUTile();

        totalThreadsTile = ThreadTiles.setupSmoothAreaChartTile("Total Threads Created");
        totalActiveTile = ThreadTiles.setupSmoothAreaChartTile("Total Threads Active");

        // add display Tiles onto screen
        memBox.getChildren().add(buildFlowGridPane(this.memoryTile.tile));
        CpuBox.getChildren().add(buildFlowGridPane(this.cpuTile.tile));
        totalThreadBox.getChildren().add(buildFlowGridPane(this.totalThreadsTile));
        activeThreadsBox.getChildren().add(buildFlowGridPane(this.totalActiveTile));

        // Set up best schedule gantt graph
        scheduleGantt = new ScheduleGantt(ganttBox.getPrefHeight(), argsParser.getProcessorCount());
        chart = scheduleGantt.setupBestScheduleGantt();
        ganttBox.getChildren().add(chart);
        ganttBox.setStyle("-fx-background-color: transparent");

        // start polling
        startPolling();
        // begin timer
        startTimer();
    }

    /**
     * Called following proper initialisation of all screen elements to continuously update the data for the user
     */
    private void startPolling() {
        //timeline that adds a new keyframe every 50 milliseconds
        Timeline autoUpdater = new Timeline(new KeyFrame(Duration.millis(50), event -> {

            //checks if the algo is done, then runs the update one more time after its finished
            if(threadData.numThreadsAlive() == 0) {

                //stops the timer from running as the algorithm is finished
                stopTimer();

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
            cpuTile.update();
            memoryTile.update();

            //if a best schedule exists, display on screen
            if(threadData.getBestSchedule() != null){
                // updateBestScheduleGantt(threadData.getBestSchedule());
                chart = scheduleGantt.updateBestScheduleGantt(threadData.getBestSchedule());
                //sets the current best time to the the string of the global best time (int)
                bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime())); // will always be an int as is initialised to 999
            }

            //removes elements if the quantity becomes too large to prevent slowdown
            if(totalActiveTile.getChartData().size() > MAX_ELEMENTS){
                totalActiveTile.getChartData().remove(0, REMOVAL_VALUE);
            }

            if(totalThreadsTile.getChartData().size() > MAX_ELEMENTS){
                totalThreadsTile.getChartData().remove(0, REMOVAL_VALUE);
            }

            //then gets the additional data and adds it to the existing data
            totalActiveTile.addChartData(new ChartData(threadData.numThreadsAlive()));
            totalThreadsTile.addChartData(new ChartData(threadData.numThreadsSpawned()));

        }));
            //sets the cycle count to be indefinite so it never stops then starts the auto-updater
            autoUpdater.setCycleCount(Animation.INDEFINITE);
            autoUpdater.play();
    }

    /**
     * Initialises a 1x1 FlowGridPane to house the inputted Tile
     * @param tile - desired tile to initialise
     * @return new FlowGridPane object to contain that tile
     */
    private FlowGridPane buildFlowGridPane(Tile tile) {
        return new FlowGridPane(1, 1, tile);
    }

    /**
     * Creates a Timeline object which handles updating the timer state to the user
     * This displays how long the algorithm has been currently running
     */
    private void startTimer(){

        //using specified time interval it creates an event to change the timer in the gui
        startTime=System.currentTimeMillis();
        timerHandler = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> {
            currentTime=System.currentTimeMillis();

            //converts the time into an int so the decimal numbers are not displayed as well
            int roundedTime = (int) ((currentTime-startTime)/1000);
            TimeText.setText(String.valueOf(roundedTime));
        }));
        timerHandler.setCycleCount(Timeline.INDEFINITE);
        timerHandler.play();
    }

    /**
     * Stops the Timeline object refreshing the timer to freeze its state at the final algorithm runtime
     */
    private void stopTimer(){
        timerHandler.stop();
    }
}

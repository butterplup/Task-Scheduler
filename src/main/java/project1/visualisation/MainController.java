package project1.visualisation;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import project1.ArgsParser;
import project1.algorithm.Schedule;
import project1.algorithm.TaskScheduled;
import project1.algorithm.ThreadAnalytics;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

import static javafx.scene.paint.Color.rgb;

/**
 * This class controls how the UI is updated in Main.fxml
 * It updates the visualised elements of the algorithm as it is calculated
 */
public class MainController {

    private final int  MAX_ELEMENTS = 500;

    @FXML
    private Text bestScheduleTime;

    @FXML
    private Text TimeText;

    @FXML
    private Text StatusText;

    @FXML
    private VBox ganttBox;

    @FXML
    private HBox totalThreadBox;

    @FXML
    private HBox activeThreadsBox;

    @FXML
    private TextField inputField;

    @FXML
    private TextField outputField;

    @FXML
    private TextField numProField;

    @FXML
    private VBox CpuBox;

    @FXML
    private VBox memBox;

    //tiles
    private Tile memoryTile;
    private Tile cpuTile;
    private Tile totalThreadsTile;
    private Tile totalActiveTile;

    //timer fields
    private double startTime;
    private double currentTime;
    private double finishTime;

    //boolean to determine if the updater needs to trigger again
    private boolean runAgain = true;

    //timeline for the poller
    private Timeline timerHandler;

    //GantChart used to display best schedule
    private GanttChart<Number,String> chart;

    private ThreadAnalytics threadData; // stores info on best schedule and threads
    private java.lang.management.OperatingSystemMXBean osBean;
    private ArgsParser argsParser; // ArgsParser object stores user input data

    /**
     * Called following object construction to set up FXML fields and their initial state
     * Also begins the event polling loop and the algorithm timer
     */
    @FXML
    public void initialize() {

        //initialises the fields that will hold all the data for the gui
        threadData = ThreadAnalytics.getInstance();
        argsParser = ArgsParser.getInstance();
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        //setup display text
        inputField.setText(argsParser.getFilename());
        outputField.setText(argsParser.getOutputFilename());
        numProField.setText(String.valueOf(argsParser.getProcessorCount()));

        // set up display elements
        setUpMemoryTile();
        setUpCpuTile();
        setUpTotalThreadTile();
        setUpActiveThreadTile();
        setUpBestScheduleGantt();

        // start polling
        startPolling();

        // initialise the tile values so they function properly
        memoryTile.setValue(0);
        cpuTile.setValue(0);

        // begin timer
        startTimer();
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


    /**
     * Called following proper initialisation of all screen elements to continuously update the data for the user
     */
    private void startPolling() {
        //timeline that adds a new keyframe every 50 milliseconds
        Timeline autoUpdater = new Timeline(new KeyFrame(Duration.millis(50), event -> {

            //checks if the algo is done, then runs the update one more time after its finished
            if(threadData.isFinished()) {

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

            //updates the memory in the memory tile
            double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
            memoryTile.setValue(memoryUsage);

            //gets the cpu usage over the entire system
            double cpuUsage = osBean.getSystemLoadAverage();
            cpuTile.setValue(cpuUsage);

            //if a best schedule exists, display on screen
            if(threadData.getBestSchedule() != null){
                updateBestScheduleGantt(threadData.getBestSchedule());

                //sets the current best time to the the string of the global best time (int)
                bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime())); // will always be an int as is initialised to 999
            }

            //then updates the active thread and total thread counts
            ObservableList<ChartData> allocData = totalActiveTile.getChartData();
            ObservableList<ChartData> orderData = totalThreadsTile.getChartData();

            //then gets the additional data and adds it to the existing data
            totalActiveTile.addChartData(new ChartData(threadData.numThreadsAlive()));
            totalThreadsTile.addChartData(new ChartData(threadData.numThreadsSpawned()));

            //needs to check if the chart data is above the max elements


        }));
            //sets the cycle count to be indefinite so it never stops then starts the auto-updater
            autoUpdater.setCycleCount(Animation.INDEFINITE);
            autoUpdater.play();
        }



    /**
     * Creates a smooth area chart for the number of active threads running
     */
    private void setUpActiveThreadTile(){
        this.totalActiveTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("Total Threads Active")
                .titleColor(Color.WHITE)
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        activeThreadsBox.getChildren().addAll(buildFlowGridPane(this.totalActiveTile));
    }

    /**
     * Sets up the tile to display total thread count history over time
     */
    private void setUpTotalThreadTile(){
        this.totalThreadsTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("Total Threads Created")
                .titleColor(Color.WHITE)
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        totalThreadBox.getChildren().addAll(buildFlowGridPane(this.totalThreadsTile));
    }

    /**
     * Sets up the current memory usage tile as a gauge for the graphical user interface
     */
    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .title("Current Memory Usage")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit("MB")
                .backgroundColor(Color.TRANSPARENT)
                .maxValue(Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0))
                .gradientStops(new Stop(0, rgb(251,206,66)),
                        new Stop(0.8, rgb(251,145,66)),
                        new Stop(1.0, rgb(245,22,118)))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .barBackgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(251,237,66))
                .unitColor(rgb(251,237,66))
                .thresholdColor(rgb(128, 84, 1))
                .needleColor(rgb(251,206,66))
                .build();



        memBox.getChildren().addAll(buildFlowGridPane(this.memoryTile));

    }

    /**
     * Sets up the current cpu usage tile as a gauge for the graphical user interface
     */
    private void setUpCpuTile() {
        this.cpuTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .title("Current Cpu Usage")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit("%")
                .maxValue(100)
                .gradientStops(new Stop(0, rgb(251,206,66)),
                        new Stop(0.8, rgb(251,145,66)),
                        new Stop(1.0, rgb(245,22,118)))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(251,237,66))
                .unitColor(rgb(251,237,66))
                .barBackgroundColor(rgb(242, 242, 242))
                .needleColor(rgb(251,206,66))
                .build();

        CpuBox.getChildren().addAll(buildFlowGridPane(this.cpuTile));

    }

    /**
     * Initialises a 1x1 FlowGridPane to house the inputted Tile
     * @param tile - desired tile to initialise
     * @return new FlowGridPane object to contain that tile
     */
    private FlowGridPane buildFlowGridPane(Tile tile) {
        return new FlowGridPane(1, 1, tile);
    }

     //Following code refactored from visualisation.VisualController by Joel

    /**
     * Initialises the blank Gantt chart to be used for displaying the current best schedule. Heavy inspiration taken
     * from Roland, author of GanttChart.java, in terms of proper usage of the class. Proper reference can be found in
     * that class.
     */
    private void setUpBestScheduleGantt() {

        int processorCount = argsParser.getProcessorCount();
        String[] processors = new String[processorCount];
        // For each processor format a user-friendly string to display on gantt
        for (int i = 0; i < processorCount; i++) {
            processors[i] = "Processor " + (i + 1);
        }

        // Init the axis for time (x)
        final NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.WHITE);
        timeAxis.setMinorTickCount(5);

        // Init the axis for processors (y)
        final CategoryAxis processorAxis = new CategoryAxis();
        processorAxis.setLabel("");
        processorAxis.setTickLabelFill(Color.WHITE);
        processorAxis.setTickLabelGap(1);
        processorAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(processors)));

        // Setting up chart
        chart = new GanttChart<>(timeAxis,processorAxis);
        chart.setLegendVisible(false);
        chart.setTitle("Current Best Schedule");
        // Make sure height per task is smaller when scheduled on more processors
        chart.setBlockHeight(100.0/processorCount);
        // Styles the appearance of tasks on the chart
        chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());
        chart.setMaxHeight(ganttBox.getPrefHeight());
        ganttBox.getChildren().add(chart);
        ganttBox.setStyle("-fx-background-color: transparent");

    }

    /**
     * Updates the data in the GanttChart to display the desired schedule to the user
     * @param bestSchedule - the current best schedule to be displayed
     */
    private void updateBestScheduleGantt(Schedule bestSchedule) {
        // number of processors the tasks are assigned to
        int processorCount = argsParser.getProcessorCount();

        // new array of series to write schedule data onto
        XYChart.Series<Number,String>[] seriesArray = new XYChart.Series[processorCount];
        // init series objs
        for (int i = 0; i < processorCount; i++){
            seriesArray[i] = new XYChart.Series<>();
        }

        // for every task in schedule, write its data onto the specific series
        for (TaskScheduled taskScheduled: bestSchedule.getCurrentSchedule()){
            // Get the processor which this task is scheduled on
            int processorForTask = taskScheduled.getProcessor();
            int displayProcessor = processorForTask + 1;

            XYChart.Data newTaskData = new XYChart.Data(taskScheduled.getStartingTime(),
                    "Processor "+ displayProcessor,
                    new GanttChart.ExtraData(taskScheduled, "task-styles"));
            // Add this task to its respective processor
            seriesArray[processorForTask].getData().add(newTaskData);

        }

        // clear out the old data and add new schedule
        chart.getData().clear();
        for (XYChart.Series<Number,String> series: seriesArray){
            chart.getData().add(series);
        }
    }


}

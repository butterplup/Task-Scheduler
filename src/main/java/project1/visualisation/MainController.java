package project1.visualisation;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.Duration;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import project1.algorithm.ThreadAnalytics;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import static javafx.scene.paint.Color.rgb;

public class MainController {

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

    //tile initialisation
    private Tile memoryTile;
    private Tile cpuTile;
    private Tile totalActive;
    private Tile totalThreadsTile;
    private Tile totalActiveTile;

    //timer set up
    private double startTime;
    private double currentTime;
    private double finishTime;

    //timeline for the poller
    private Timeline timerHandler;

    //this is where we ge thte scehudel daat from
    private ThreadAnalytics threadData;
    private java.lang.management.OperatingSystemMXBean osBean;


    //initialisation call
    public void init() {

        //intilisses the fields that will hold all the data for the gui
        threadData = ThreadAnalytics.getInstance();
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // set up display elements
        setUpMemoryTile();
        setUpCpuTile();
        setUpTotalThreadTile();
        setUpActiveThreadTile();

        // start polling
        startPolling();

        // initialize the tile values so they can work
        memoryTile.setValue(0);
        cpuTile.setValue(0);

        // begin timer
        startTimer();
    }

    //starts the timer
    private void startTimer(){

        //ever specified time interval it creates an event to change the time on the gui
        startTime=System.currentTimeMillis();
        timerHandler = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentTime=System.currentTimeMillis();
                TimeText.setText(""+((currentTime-startTime)/1000));
            }
        }));
        timerHandler.setCycleCount(Timeline.INDEFINITE);
        timerHandler.play();
    }

    private void stopTimer(){
        timerHandler.stop();
    }


    //poller to update visual display
    private void startPolling() {
        //timeline that adds a enw keyframe every 50miliseconds
        Timeline autoUpdater = new Timeline(new KeyFrame(Duration.millis(50), event -> {

            if(threadData.determineFinished()){

                //stops the timer form running as the alogrithm is finished
                stopTimer();

                //if the threadData is finsihed set the running text to be done
                StatusText.setStyle("-fx-fill: rgb(15,150,100)");
                StatusText.setText("Done");
            }

            //updates  the memory in the memory tile
            double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
            memoryTile.setValue(memoryUsage);

            //gets the cpu usage over the entire system
            double cpuUsage = osBean.getSystemLoadAverage();
            cpuTile.setValue(cpuUsage);

            if(threadData.getBestSchedule() != null){

                //needs to update gant
            }

            //sets the current best time to the the string of the global best time (int)
            bestScheduleTime.setText(String.valueOf(threadData.getGlobalBestTime()));


        }));
            //sets the cycle count to be indefinite so it never stops then starts the autoupdater
            autoUpdater.setCycleCount(Animation.INDEFINITE);
            autoUpdater.play();
        }



    /**
     * Creates the smooth area charts for the number of active threads and the number of total threads spawned
     */
    private void setUpActiveThreadTile(){
        this.totalActiveTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("total threads active")
                .titleColor(rgb(15,50,50))
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(0,216,244))
                .build();

        activeThreadsBox.getChildren().addAll(buildFlowGridPane(this.totalActiveTile));
    }

    private void setUpTotalThreadTile(){
        this.totalThreadsTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("total threads run")
                .titleColor(rgb(15,50,50))
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(0,216,244))
                .build();

        totalThreadBox.getChildren().addAll(buildFlowGridPane(this.totalThreadsTile));
    }

    /**
     * Sets up the memory and cpu usage tiles for the graphical user interface
     */
    //sets up the memory tile
    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .unit("MB")
                .maxValue(Runtime.getRuntime().maxMemory() / (1024 * 1024))
                .gradientStops(new Stop(0, rgb(244,160,0)),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(244,160,0))
                .unitColor(rgb(244,160,0))
                .barBackgroundColor(rgb(242, 242, 242))
                .thresholdColor(rgb(128, 84, 1))
                .needleColor(rgb(244,160,0))
                .build();

        memBox.getChildren().addAll(buildFlowGridPane(this.memoryTile));

    }

    //sets up the cpu tile
    private void setUpCpuTile() {
        this.cpuTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .unit("%")
                .maxValue(100)
                .gradientStops(new Stop(0, rgb(244,160,0)),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(244,160,0))
                .unitColor(rgb(244,160,0))
                .barBackgroundColor(rgb(242, 242, 242))
                .needleColor(rgb(244,160,0))
                .build();

        CpuBox.getChildren().addAll(buildFlowGridPane(this.cpuTile));

    }


    private FlowGridPane buildFlowGridPane(Tile tile) {
        return new FlowGridPane(1, 1, tile);
    }


}

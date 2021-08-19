package project1.Visualisation;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
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

import static javafx.scene.paint.Color.rgb;

public class MainController {

    @FXML
    private Text PlaceHolderText;

    @FXML
    private Text TimeText;

    @FXML
    private Text StatusText;

    @FXML
    private VBox ganttBox;

    @FXML
    private HBox allocBox;

    @FXML
    private HBox orderBox;

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

    //tile intilisation
    private Tile memoryTile;
    private Tile cpuTile;

    //timer set up
    private double startTime;
    private double currentTime;
    private double finishTime;

    //timeline for the pooller
    private Timeline timerHandler;



    //intilisation call
    public void init() {


        // set up display elements
        setUpMemoryTile();
        setUpCpsuTile();

        // start polling
        startPolling();

        // initialize the tile values so they can work
        memoryTile.setValue(0);
        cpuTile.setValue(0);

        // begin timer
        startTimer();
    }

    //statrs the timer
    private void startTimer(){

        //ever specifed time interval it creates an event to change the time on the gui
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

    }

    //ssets up the memory tile
    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .unit("MB")
                .maxValue(Runtime.getRuntime().maxMemory() / (1024 * 1024))
                .threshold(Runtime.getRuntime().maxMemory() * 0.8 / (1024 * 1024))
                .gradientStops(new Stop(0, rgb(244,160,0)),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .thresholdVisible(true)
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
    private void setUpCpsuTile() {
        this.cpuTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .unit("%")
                .maxValue(100)
                .threshold(100 * 0.8)
                .gradientStops(new Stop(0, rgb(244,160,0)),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .thresholdVisible(true)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(244,160,0))
                .unitColor(rgb(244,160,0))
                .barBackgroundColor(rgb(242, 242, 242))
                .thresholdColor(rgb(128, 84, 1))
                .needleColor(rgb(244,160,0))
                .build();

        CpuBox.getChildren().addAll(buildFlowGridPane(this.cpuTile));

    }


    private FlowGridPane buildFlowGridPane(Tile tile) {
        return new FlowGridPane(1, 1, tile);
    }


}

package project1.visualisation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import project1.ArgsParser;
import project1.algorithm.Schedule;
import project1.algorithm.TaskScheduled;

import java.util.Arrays;

public class VisualController {

    @FXML
    private VBox ganttBox;

    private GanttChart<Number,String> chart;

    private ArgsParser argsParser;

    //TODO PROPERLY INJECT THIS FROM VISUALISER
    public void injectArgs(ArgsParser argsParser) {
        this.argsParser = argsParser;
    }

    public void init() {

        // Initialise screen elements
        setUpBestScheduleGantt();

        // TODO POLL FOR UPDATED SCHEDULES FROM THREAD ANALYTICS
        // Possibly use TimerTask

    }

    /**
     * Initialises the blank Gantt chart to be used for displaying the current best schedule. Heavy inspiration taken
     * from Roland, author of GanttChart.java, in terms of proper usage of the class. Proper reference can be found in
     * that class.
     */
    private void setUpBestScheduleGantt() {

        int processorCount = argsParser.getProcessorCount();
        String[] processors = new String[processorCount];

        for (int i = 0; i < processorCount; i++) {
            processors[i] = "Processor " + String.valueOf(i);
        }

        // Init the axis for time (x)
        final NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.rgb(0,128,0));
        timeAxis.setMinorTickCount(5);

        // Init the axis for processors (y)
        final CategoryAxis processorAxis = new CategoryAxis();
        processorAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.rgb(0,128,0));
        processorAxis.setTickLabelGap(1);
        processorAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));

        // Setting up chart
        chart = new GanttChart<Number,String>(timeAxis,processorAxis);
        chart.setLegendVisible(false);
        // Make sure height per task is smaller when scheduled on more processors
        chart.setBlockHeight(280/processorCount);
        // Styles the appearance of tasks on the chart
        chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());
        chart.setMaxHeight(ganttBox.getPrefHeight());
        ganttBox.getChildren().add(chart);
        ganttBox.setStyle("-fx-background-color: WHITE");

    }

    private void updateBestScheduleGantt(Schedule bestSchedule) {

        int processorCount = argsParser.getProcessorCount();

        // new array of series to write schedule data onto
        XYChart.Series[] seriesArray = new XYChart.Series[processorCount];
        // init series objs
        for (int i = 0; i < processorCount; i++){
            seriesArray[i] = new XYChart.Series();
        }

        // for every task in schedule, write its data onto the specific series
        for (TaskScheduled taskScheduled: bestSchedule.getCurrentSchedule()){
            // Get the processor which this task is scheduled on
            int processorForTask = taskScheduled.getProcessor();

            XYChart.Data newTaskData = new XYChart.Data(taskScheduled.getStartingTime(),
                    "Processor "+ String.valueOf(processorForTask),
                    new GanttChart.ExtraData(taskScheduled, "task-styles"));
            // Add this task to its respective processor
            seriesArray[processorForTask].getData().add(newTaskData);
        }

        // clear out the old data and add new schedule
        chart.getData().clear();
        for (XYChart.Series series: seriesArray){
            chart.getData().add(series);
        }
    }

}

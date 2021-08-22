package project1.visualisation;

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import project1.algorithm.PartialSchedule;
import project1.algorithm.TaskScheduled;

import java.util.Arrays;

/**
 * This class initialises and updates a GanttChart to display a schedule
 * Output used for rendering in the visualisation. Sets up some basic styles
 * as well as writing data from TaskScheduled class in a readable format for
 * the application.
 */
public class ScheduleGantt {

    //GantChart used to display best schedule
    private GanttChart<Number,String> chart;
    private double height;
    private int processorCount;

    public ScheduleGantt(double height, int processorCount) {
        this.height = height;
        this.processorCount = processorCount;

    }

    /**
     * Initialises the blank Gantt chart to be used for displaying the current best schedule. Heavy inspiration taken
     * from Roland, author of GanttChart.java, in terms of proper usage of the class. Proper reference can be found in
     * that class.
     */
    public GanttChart<Number,String> setupBestScheduleGantt() {

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
        timeAxis.setMinorTickVisible(false);

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
        chart.setMaxHeight(height);

        return chart;

    }

    /**
     * Updates the data in the GanttChart to display the desired schedule to the user
     * @param bestSchedule - the current best schedule to be displayed
     */
    public GanttChart<Number,String> updateBestScheduleGantt(PartialSchedule bestSchedule) {

        // new array of series to write schedule data onto
        XYChart.Series<Number,String>[] seriesArray = new XYChart.Series[processorCount];
        // init series objs
        for (int i = 0; i < processorCount; i++){
            seriesArray[i] = new XYChart.Series<>();
        }

        // for every task in schedule, write its data onto the specific series
        for (TaskScheduled taskScheduled: bestSchedule.getScheduledTasks()){
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

        return chart;
    }
}

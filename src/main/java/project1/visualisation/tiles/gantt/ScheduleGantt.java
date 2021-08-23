package project1.visualisation.tiles.gantt;

import eu.hansolo.tilesfx.Tile;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import project1.algorithm.PartialSchedule;
import project1.algorithm.TaskScheduled;
import project1.algorithm.ThreadAnalytics;
import project1.visualisation.tiles.VTile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class initialises and updates a GanttChart to display a schedule
 * Output used for rendering in the visualisation. Sets up some basic styles
 * as well as writing data from TaskScheduled class in a readable format for
 * the application.
 */
public class ScheduleGantt extends VTile {
    // GanttChart used to display best schedule
    private final GanttChart<Number,String> chart;
    private final int processorCount;
    private final ThreadAnalytics ta;

    /**
     * Constructor for an object that initialises and updates a GanttChart object
     * @param parent The parent pane to render the Gantt Chart on
     * @param height Height of the Gantt Chart
     * @param processorCount Number of processor "rows" to display
     * @param ta ThreadAnalytics to obtain schedule data from
     */
    public ScheduleGantt(Pane parent, double height, int processorCount, ThreadAnalytics ta) {
        this.processorCount = processorCount;
        this.ta = ta;

        List<String> processors = new ArrayList<>();
        IntStream.range(0, processorCount).forEach(
                (n) -> processors.add("Processor " + (n + 1)) // First processor is processor 1
        );

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
        processorAxis.setCategories(FXCollections.observableArrayList(processors));

        // Setting up chart
        chart = new GanttChart<>(timeAxis,processorAxis);
        chart.setLegendVisible(false);
        chart.setTitle("Current Best Schedule");
        // Make sure height per task is smaller when scheduled on more processors
        chart.setBlockHeight(100.0/processorCount);
        // Styles the appearance of tasks on the chart
        chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());
        chart.setMaxHeight(height);

        parent.getChildren().add(chart);
    }

    /**
     * Updates the data in the GanttChart to display the best schedule
     */
    public void update() {
        PartialSchedule bestSchedule = ta.getBestSchedule();
        if (bestSchedule == null) {
            return;
        }

        // Create n Series
        List<XYChart.Series<Number, String>> seriesList = new ArrayList<>();
        IntStream.range(0, processorCount).forEach(
                (n) -> seriesList.add(new XYChart.Series<>())
        );

        // For every task in schedule, write its data to the relevant series
        for (TaskScheduled taskScheduled : bestSchedule.getScheduledTasks()) {
            // Get the processor which this task is scheduled on
            int p = taskScheduled.getProcessor();

            XYChart.Data<Number, String> newTaskData =
                    new XYChart.Data<>(
                            taskScheduled.getStartingTime(),
                            "Processor " + (p + 1), // First processor is processor 1
                             new GanttChart.ExtraData(taskScheduled, "task-styles")
                    );

            // Add this task to its respective processor
            seriesList.get(p).getData().add(newTaskData);
        }

        // Clear old values and replace with new ones
        chart.getData().clear();
        chart.getData().addAll(seriesList);
    }

    public Tile getTile() {
        return null;
    }
}

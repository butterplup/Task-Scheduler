package project1.visualisation.tiles;

import eu.hansolo.tilesfx.chart.ChartData;
import javafx.scene.layout.Pane;
import project1.algorithm.ThreadAnalytics;

public class TotalThreadsTile extends AreaChartTile {
    private final ThreadAnalytics ta;

    public TotalThreadsTile(Pane parent, ThreadAnalytics ta) {
        super(parent, "Total Threads Created");
        this.ta = ta;
    }

    @Override
    public void update() {
        super.update();
        getTile().addChartData(new ChartData(ta.numThreadsSpawned()));
    }
}

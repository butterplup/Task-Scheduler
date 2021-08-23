package project1.visualisation.Tiles;

import eu.hansolo.tilesfx.chart.ChartData;
import javafx.scene.layout.Pane;
import project1.algorithm.ThreadAnalytics;

public class TotalActiveTile extends AreaChartTile {
    private final ThreadAnalytics ta;

    public TotalActiveTile(Pane parent, ThreadAnalytics ta) {
        super(parent,"Total Threads Created");
        this.ta = ta;
    }

    @Override
    public void update() {
        super.update();
        getTile().addChartData(new ChartData(ta.numThreadsAlive()));
    }
}

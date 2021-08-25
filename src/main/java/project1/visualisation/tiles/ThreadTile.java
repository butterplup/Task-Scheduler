package project1.visualisation.tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.function.IntSupplier;

import static javafx.scene.paint.Color.rgb;

/**
 * This class is concerned with the tiles displaying thread information.
 * It initialises the tiles displaying both the total generated threads
 * and the current active threads as smooth area charts and adds minor styling.
 */
public class ThreadTile extends VTile {
    @Getter private final Tile tile;
    private final IntSupplier data;

    /**
     * Create a ThreadTile
     * @param parent Parent pane to append tile to
     * @param update Value of data
     */
    public ThreadTile(Pane parent, IntSupplier update) {
        tile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        this.data = update;
        addTo(parent);
    }

    /**
     * Get data from the IntSupplier and add it to the chart
     */
    @Override
    public void update() {
        int MAX_ELEMENTS = 500;
        if (getTile().getChartData().size() > MAX_ELEMENTS){
            int REMOVAL_VALUE = 50;
            getTile().getChartData().remove(0, REMOVAL_VALUE);
        }

        getTile().addChartData(new ChartData(data.getAsInt()));
    }

    /**
     * House this node in a 1x1 FlowGridPane before parenting
     * @return The FlowGridPane
     */
    public Node getNode() {
        return new FlowGridPane(1, 1, tile);
    }
}

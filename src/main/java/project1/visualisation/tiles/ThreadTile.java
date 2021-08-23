package project1.visualisation.tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
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
     * Set up a tile to display a Smooth Area Chart.
     * @param title The title of the tile.
     */
    public ThreadTile(Pane parent, String title, IntSupplier update) {
        tile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title(title)
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(Color.WHITE)
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        this.data = update;
        addTo(parent);
    }

    @Override
    public void update() {
        int MAX_ELEMENTS = 500;
        if (getTile().getChartData().size() > MAX_ELEMENTS){
            int REMOVAL_VALUE = 50;
            getTile().getChartData().remove(0, REMOVAL_VALUE);
        }

        getTile().addChartData(new ChartData(data.getAsInt()));
    }
}

package project1.visualisation.Tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import static javafx.scene.paint.Color.rgb;

/**
 * This class is concerned with the Tiles displaying thread information.
 * It initialises the tiles displaying both the total generated threads
 * and the current active threads as smooth area charts and adds minor styling.
 */
public abstract class AreaChartTile extends VTile {
    @Getter private final Tile tile;

    /**
     * Set up a tile to display a Smooth Area Chart.
     * @param title The title of the tile.
     * @return The built Tile, to be displayed.
     */
    public AreaChartTile(Pane parent, String title) {
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

        addTo(parent);
    }

    @Override
    public void update() {
        int MAX_ELEMENTS = 500;
        if (getTile().getChartData().size() > MAX_ELEMENTS){
            int REMOVAL_VALUE = 50;
            getTile().getChartData().remove(0, REMOVAL_VALUE);
        }
    }
}

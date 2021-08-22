package project1.visualisation;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.scene.paint.Color;

import static javafx.scene.paint.Color.rgb;

public class ThreadTiles {

    /**
     * Sets up the tile to display total thread count history over time
     */
    public static Tile setupTotalThreadTile(){
        Tile totalThreadsTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("Total Threads Created")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(Color.WHITE)
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        return totalThreadsTile;
    }

    /**
     * Creates a smooth area chart for the number of active threads running
     */
    public static Tile setupActiveThreadTile() {
        Tile totalActiveTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
                .chartData(new ChartData(0), new ChartData(0))
                .title("Total Threads Active")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(Color.WHITE)
                .textSize(Tile.TextSize.BIGGER)
                .animated(false)
                .smoothing(true)
                .decimals(0)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(0,216,244))
                .build();

        return totalActiveTile;
    }
}

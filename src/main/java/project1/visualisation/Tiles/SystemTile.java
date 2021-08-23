package project1.visualisation.Tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

import static javafx.scene.paint.Color.rgb;

/**
 * A static class used for generating the required Tiles used to display system data
 * It initialises the tiles displaying both the memory usage and cpu usage
 * as bar gauges and adds minor styling.
 */
public abstract class SystemTile {
    public Tile tile;

    /**
     * Set up a tile to display a system stat
     * @param title The title of the tile.
     * @param maxValue The maximum value of the tile (-1 if no specified max)
     * @return The built Tile, to be displayed.
     */
    public SystemTile(String title, int maxValue) {
        TileBuilder tileBuilder = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .title(title)
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit("%")
                .gradientStops(new Stop(0, rgb(251,206,66)),
                        new Stop(0.8, rgb(251,145,66)),
                        new Stop(1.0, rgb(245,22,118)))
                .animated(true)
                .decimals(0)
                .barBackgroundColor(Color.color(0.8,0.8,0.8, 0.9))
                .strokeWithGradient(true)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(251,237,66))
                .unitColor(rgb(251,237,66))
                .needleColor(rgb(251,206,66))
                ;

        if (maxValue != -1) {
            tileBuilder.maxValue(maxValue);
        }

        Tile builtTile = tileBuilder.build();
        builtTile.setValue(0);
        tile = builtTile;
    }

    public abstract void update();
}

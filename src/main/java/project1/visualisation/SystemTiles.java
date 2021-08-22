package project1.visualisation;

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
public class SystemTiles {

    /**
     * Sets up the current memory usage tile as a gauge for the graphical user interface
     */
    public static Tile setupMemoryTile() {

        Tile memoryTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .title("Current Memory Usage")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit("MB")
                .backgroundColor(Color.TRANSPARENT)
                .barBackgroundColor(Color.color(0.8,0.8,0.8, 0.9))
                .maxValue(Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0))
                .gradientStops(new Stop(0, rgb(251,206,66)),
                        new Stop(0.8, rgb(251,145,66)),
                        new Stop(1.0, rgb(245,22,118)))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .valueColor(rgb(251,237,66))
                .unitColor(rgb(251,237,66))
                .thresholdColor(rgb(128, 84, 1))
                .needleColor(rgb(251,206,66))
                .build();

        // Initialised to 0 before polled data is received
        memoryTile.setValue(0);

        return memoryTile;
    }

    /**
     * Sets up the current cpu usage tile as a gauge for the graphical user interface
     */
    public static Tile setupCPUTile() {
        Tile cpuTile = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .title("Current Cpu Usage")
                .textSize(Tile.TextSize.BIGGER)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit("%")
                .maxValue(100)
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
                .build();

        // Initialises to a 0 value to start before polled data received
        cpuTile.setValue(0);
        return cpuTile;
    }

}

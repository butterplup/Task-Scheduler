package project1.visualisation.tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import lombok.Getter;

import java.util.function.DoubleSupplier;

import static javafx.scene.paint.Color.rgb;

/**
 * A static class used for generating the required tiles used to display system data
 * It initialises the tiles displaying both the memory usage and cpu usage
 * as bar gauges and adds minor styling.
 */
public class SystemTile extends VTile {
    @Getter private final Tile tile;
    private final DoubleSupplier data;
    // Set to true if the unit is "%"
    private final boolean percentage;

    /**
     * Set up a tile to display a system stat
     * @param parent Parent pane to append tile to.
     * @param unit Unit of the data values.
     * @param maxValue The maximum value of the tile.
     * @param update Value to update tile with.
     */
    public SystemTile(Pane parent, String unit, double maxValue, DoubleSupplier update) {
        percentage = unit.equals("%");

        TileBuilder tileBuilder = TileBuilder.create().skinType(Tile.SkinType.BAR_GAUGE)
                .titleColor(rgb(255,255,255))
                .titleAlignment(TextAlignment.CENTER)
                .unit(unit)
                .gradientStops(new Stop(0, rgb(251,237,66)),
                        new Stop(0.2, rgb(255,165,0)),
                        new Stop(0.4, rgb(255,80,0)),
                        new Stop(0.6, rgb(255,0,153)),
                        new Stop(0.8, rgb(255,0,255)),
                        new Stop (1, rgb(145,0,255)))
                .animated(true)
                .decimals(0)
                .barBackgroundColor(Color.color(0.8,0.8,0.8, 0.9))
                .strokeWithGradient(true)
                .backgroundColor(Color.TRANSPARENT)
                .valueColor(rgb(255,0,193))
                .unitColor(rgb(255,0,193))
                .needleColor(rgb(255,0,193))
                ;

        if (maxValue != -1) {
            tileBuilder.maxValue(maxValue);
        }

        Tile builtTile = tileBuilder.build();
        builtTile.setValue(0);

        this.data = update;
        this.tile = builtTile;
        addTo(parent);
    }

    /**
     * Get data from the DoubleSupplier and add it to the chart
     */
    @Override
    public void update() {
        getTile().setValue(
                data.getAsDouble() * (percentage ? 100 : 1)
        );
    }

    /**
     * House this node in a 1x1 FlowGridPane before parenting
     * @return The FlowGridPane
     */
    public Node getNode() {
        return new FlowGridPane(1, 1, tile);
    }
}

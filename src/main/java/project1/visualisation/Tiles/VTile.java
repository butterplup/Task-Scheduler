package project1.visualisation.Tiles;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.layout.Pane;

public abstract class VTile {
    public abstract void update();
    public abstract Tile getTile();

    /**
     * Initialises a 1x1 FlowGridPane to house the inputted Tile
     * @param p - Add the tile to the parent
     */
    protected void addTo(Pane p) {
        p.getChildren().add(new FlowGridPane(1, 1, getTile()));
    }
}

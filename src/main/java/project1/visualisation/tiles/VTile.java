package project1.visualisation.tiles;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class VTile {
    public abstract void update();
    public abstract Node getNode();

    /**
     * Initialises a 1x1 FlowGridPane to house Tile
     * @param p - Add the tile to the parent
     */
    protected void addTo(Pane p) {
        p.getChildren().add(getNode());
    }
}

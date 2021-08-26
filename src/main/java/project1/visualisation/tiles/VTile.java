package project1.visualisation.tiles;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * A VTile is an updatable tile in the visualisation
 */
public abstract class VTile {
    /**
     * Called on every GUI update
     */
    public abstract void update();

    /**
     * Get the node to place in the scene
     * @return A Node
     */
    public abstract Node getNode();

    /**
     * Parent this VTile to a Pane
     * @param p - Add the tile to the parent
     */
    protected void addTo(Pane p) {
        p.getChildren().add(getNode());
    }
}

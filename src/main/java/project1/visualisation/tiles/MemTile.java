package project1.visualisation.tiles;

import javafx.scene.layout.Pane;

public class MemTile extends BarGaugeTile {
    public MemTile(Pane parent) {
        super(parent, "Current Memory Usage", "MB", (int) (Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0)));
    }

    @Override
    public void update() {
        /// updates the memory in the memory tile
        double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
        getTile().setValue(memoryUsage);
    }
}

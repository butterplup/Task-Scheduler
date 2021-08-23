package project1.visualisation.Tiles;

import javafx.scene.layout.Pane;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class MemTile extends BarGaugeTile {
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public MemTile(Pane parent) {
        super(parent, "Current Memory Usage",  -1);
    }

    @Override
    public void update() {
        ///updates the memory in the memory tile
        double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
        getTile().setValue(memoryUsage);
    }
}

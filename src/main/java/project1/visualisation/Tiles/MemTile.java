package project1.visualisation.Tiles;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class MemTile extends SystemTile {
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public MemTile() {
        super("Current Memory Usage",  -1);
    }

    @Override
    public void update() {
        ///updates the memory in the memory tile
        double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
        tile.setValue(memoryUsage);
    }
}

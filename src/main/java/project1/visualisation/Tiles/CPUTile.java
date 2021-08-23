package project1.visualisation.Tiles;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CPUTile extends SystemTile {
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public CPUTile() {
        super("Current CPU Usage", 100);
    }

    @Override
    public void update() {
        //gets the cpu usage over the entire system
        double cpuUsage = osBean.getSystemLoadAverage();
        tile.setValue(cpuUsage * 10d);
    }
}

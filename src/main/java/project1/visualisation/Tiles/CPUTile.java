package project1.visualisation.Tiles;

import javafx.scene.layout.Pane;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CPUTile extends BarGaugeTile {
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public CPUTile(Pane parent) {
        super(parent,"Current CPU Usage", 100);
    }

    @Override
    public void update() {
        //gets the cpu usage over the entire system
        double cpuUsage = osBean.getSystemLoadAverage();
        getTile().setValue(cpuUsage * 10d);
    }
}

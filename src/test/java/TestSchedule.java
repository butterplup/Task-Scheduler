import org.junit.Assert;
import org.junit.Test;
import project1.algorithm.Schedule;
import project1.algorithm.TaskScheduled;
import project1.graph.DotParser;
import project1.graph.Graph;
import project1.graph.Node;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class TestSchedule {
    // @Test(expected = Exception.class)
    public void testNoProcessor() {
        // Create a Schedule with no processors - should throw an error
        Schedule noProcessors = new Schedule(0);
    }

    // @Test
    public void testCopySchedule() {
        // Create a Schedule
        Schedule schedule = new Schedule(2);
        // Create a Copy of the Schedule
        Schedule scheduleCopy = new Schedule(schedule);

        // Check they are equal
        assertEquals(schedule, scheduleCopy);
    }
}

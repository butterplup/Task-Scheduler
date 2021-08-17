import org.junit.Test;
import project1.algorithm.Schedule;
import project1.algorithm.TaskScheduled;
import project1.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSchedule {
    @Test(expected = IllegalArgumentException.class)
    public void testNoProcessor() {
        // Create a Schedule with no processors - should throw an error
        Schedule noProcessors = new Schedule(0, new ArrayList<>());
    }

    @Test
    public void testCreateSchedule() {
        // Create a Schedule
        Schedule schedule = new Schedule(2, new ArrayList<>());
    }

    @Test
    public void testCopySchedule() {
        // Create a Schedule
        Schedule schedule = new Schedule(2, new ArrayList<>());
        // Create a Copy of the Schedule
        Schedule scheduleCopy = new Schedule(schedule);
        schedule.printSchedule();
        scheduleCopy.printSchedule();
    }

    @Test
    public void testAddTask() {
        // Create a Schedule
        Schedule schedule = new Schedule(2, new ArrayList<>(Arrays.asList(new Node(0, "1"), new Node(0, "2"))));
        schedule.addTask(new TaskScheduled(new Node(0, ""), 0, 1));
    }
}

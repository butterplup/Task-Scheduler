import org.junit.Before;
import org.junit.Test;
import project1.algorithm.PartialSchedule;
import project1.algorithm.TaskScheduled;
import project1.graph.Graph;
import project1.graph.dotparser.Parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit testing for the PartialSchedule Class
 */
public class TestPartialSchedule {
    private static final Graph graph = load("exampleTaskGraphs/Nodes_7_OutTree.dot");
    private static final Graph small = load("small.dot");
    private static PartialSchedule complete;
    private static TaskScheduled taskOne;
    private static TaskScheduled taskTwo;

    /**
     * Method that converts a dot file to graph object.
     *
     * @param resource Location of the dot file
     * @return A graph object generated based on the dot file provided
     */
    private static Graph load(String resource) {
        URL url = TestPartialSchedule.class.getResource(resource);
        try {
            return Parser.parse(url.getPath());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Set up instances to be used for testing.
     */
    @Before
    public void setUp() {
        complete = new PartialSchedule(small, 2);
        taskOne = new TaskScheduled(small.getNodes().get(0), 0, 0);
        complete = new PartialSchedule(complete, taskOne);
        taskTwo = new TaskScheduled(small.getNodes().get(1), 2, 0);
        complete = new PartialSchedule(complete, taskTwo);
    }

    /**
     * Tests invalid schedule creation for 0 processors
     *
     * @throws IllegalArgumentException The exception expected to be thrown with
     *                                  illegal constructor arguments.
     */
    @Test
    public void testNoProcessor() throws IllegalArgumentException {
        // Create a Schedule with no processors - should throw an error
        PartialSchedule noProcessors = new PartialSchedule(graph, 0);
    }

    /**
     * Tests the constructor method for partial schedule
     */
    @Test
    public void testCreatePartialSchedule() {
        // Create a Schedule
        PartialSchedule schedule = new PartialSchedule(graph, 2);
        assertEquals(0, schedule.getNodesVisited());
        assertEquals(0, schedule.getFinishTime());
        assertEquals(graph, PartialSchedule.getSchedulingGraph());
        assertEquals(2, PartialSchedule.getProcessors());
    }

    /**
     * Tests the second constructor method for partial schedule which
     * takes in a reference to the previous partial schedule and the
     * new schedule task as input arguments.
     */
    @Test
    public void testCreatePartialScheduleWithNewTaskScheduled() {
        // Create a Schedule
        PartialSchedule pre = new PartialSchedule(graph, 2);
        TaskScheduled ts = new TaskScheduled(graph.getNodes().get(1), 0, 0);
        PartialSchedule schedule = new PartialSchedule(pre, ts);
        assertEquals(1, schedule.getNodesVisited());
        assertEquals(ts.getFinishTime(), schedule.getFinishTime());
        assertEquals(pre, schedule.getPrev());
        assertEquals(ts, schedule.getTs());
        assertEquals(graph, PartialSchedule.getSchedulingGraph());
    }

    /**
     * Tests the list of schedules returned by the expandSchedule() method contains all the expected
     * partial schedule objects.
     */
    @Test
    public void testExpandSchedule() {
        PartialSchedule pre = new PartialSchedule(small, 2);
        TaskScheduled ts = new TaskScheduled(small.getNodes().get(0), 0, 0);
        pre = new PartialSchedule(pre, ts);
        PartialSchedule possibilityOne = new PartialSchedule(pre, new TaskScheduled(small.getNodes().get(1),
                2, 0));
        PartialSchedule possibilityTwo = new PartialSchedule(pre, new TaskScheduled(small.getNodes().get(1),
                3, 1));
        List<PartialSchedule> verify = new ArrayList<>(2);
        verify.add(possibilityOne);
        verify.add(possibilityTwo);

        List<PartialSchedule> ps = pre.expandSchedule(Integer.MAX_VALUE);
        int index = 0;
        for (PartialSchedule p : ps) {
            assertEquals(verify.get(index).printSchedule(), p.printSchedule());
            ++index;
        }
    }

    /**
     * Tests all taskScheduled objects in the schedule are returned when the
     * getScheduledTasks() method call is performed.
     */
    @Test
    public void testgetScheduledTasks() {
        TaskScheduled[] check = new TaskScheduled[2];
        check[0] = taskOne;
        check[1] = taskTwo;
        TaskScheduled[] scheduledTasks = complete.getScheduledTasks();
        int i = 0;
        for (TaskScheduled t : scheduledTasks) {
            assertEquals(check[i], t);
            i++;
        }
    }

    /**
     * Tests the isCompleteSchedule() method returns the correct boolean
     * value for a complete schedule.
     */
    @Test
    public void testIsCompleteSchedule() {
        assertTrue(complete.isCompleteSchedule(2));
    }

}


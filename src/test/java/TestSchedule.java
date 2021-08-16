import project1.algorithm.Schedule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestSchedule {
    // @Test(expected = Exception.class)
    public void testNoProcessor() {
        // Create a Schedule with no processors - should throw an error
        Schedule noProcessors = new Schedule(0, new ArrayList<>());
    }

    // @Test
    public void testCopySchedule() {
        // Create a Schedule
        Schedule schedule = new Schedule(2, new ArrayList<>());
        // Create a Copy of the Schedule
        Schedule scheduleCopy = new Schedule(schedule);

        // Check they are equal
        assertEquals(schedule, scheduleCopy);
    }
}

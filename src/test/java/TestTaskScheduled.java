import static org.junit.Assert.assertEquals;
import org.junit.Test;
import project1.algorithm.TaskScheduled;
import project1.graph.Node;

/**
 * Unit testing for TaskScheduled class
 */
public class TestTaskScheduled {

    /**
     * Testing the getFinishTime() method of a scheduled task.
     */
    @Test
    public void testGetFinishTime(){
        Node n=new Node(3,"a");
        TaskScheduled ts=new TaskScheduled(n,0,0);
        assertEquals(3,ts.getFinishTime());
    }

}

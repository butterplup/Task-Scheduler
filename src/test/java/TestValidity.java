import org.junit.Before;
import org.junit.Test;
import project1.algorithm.PartialSchedule;
import project1.algorithm.TaskScheduled;
import project1.algorithm.TopologicalSort;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;
import project1.graph.dotparser.Parser;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Tests the validity of schedules
 */
public class TestValidity {
    private static Graph g;

    /**
     * This checks that a PartialSchedule is complete and valid
     * based on the taskGraph it stores and number of processors
     * it stores.
     * @param s A partial Schedule object to be checked.
     * @return true if valid and complete, false if invalid/not complete.
     */
    public boolean validityCheck(PartialSchedule s){
        Graph taskGraph=PartialSchedule.getSchedulingGraph();
        if (!s.isCompleteSchedule(taskGraph.getNodes().size())){
            return false;
        }
        TaskScheduled[] alltasks=s.getScheduledTasks();
        for (TaskScheduled ts:alltasks){
            Node taskNode=ts.getTaskNode();
            int startTime=0;
            for (Edge in:taskNode.getIncomingEdges()){
                //Check this task is scheduled after all predecessors and at correct start time
                int parentID=in.getStart().getId();
                int delay=0;
                //Check parent is scheduled
                if (alltasks[parentID]==null){
                    return false;
                }
                if (ts.getProcessor()!=alltasks[parentID].getProcessor()){
                    delay=in.getWeight();
                }
                startTime=Math.max(startTime,alltasks[parentID].getFinishTime()+delay);
            }
            //Check if start time is after/equal to the expected start time
            if (ts.getStartingTime()<startTime){
                return false;
            }
            //check if finish time is correct
            if (ts.getFinishTime()!= ts.getStartingTime()+taskNode.getWeight()){
                return false;
            }
        }

        return true;
    }

    @Before
    public void setUp(){
        URL url = TestValidity.class.getResource("exampleTaskGraphs/Nodes_7_OutTree.dot");
        try {
            g=Parser.parse(url.getPath());
        } catch (IOException e) {
            g=null;
        }
    }

    @Test
    public void testCompleteSchedule() {
        PartialSchedule topo=TopologicalSort.getSchedule(g,2);
        assertTrue(validityCheck(topo));
    }

    @Test
    public void testIncompleteSchedule(){
        PartialSchedule partial=new PartialSchedule(g,2);
        TaskScheduled oneTask=new TaskScheduled(g.getNodes().get(0),0,0 );
        partial=new PartialSchedule(partial,oneTask);
        assertFalse(validityCheck(partial));
    }
}

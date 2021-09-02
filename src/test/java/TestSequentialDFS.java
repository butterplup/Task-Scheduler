import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import project1.algorithm.PartialSchedule;
import project1.algorithm.SequentialDFS;
import project1.algorithm.ThreadAnalytics;
import project1.graph.dotparser.Parser;
import project1.graph.Graph;

import java.io.IOException;
import java.net.URL;

/**
 * Unit testing sequentialDFS algorithm, checking whether the algorithm is able to
 * find an optimal schedule.
 */
public class TestSequentialDFS {
    private static final Graph graph7 = load("exampleTaskGraphs/Nodes_7_OutTree.dot");
    private static final Graph graph8 = load("exampleTaskGraphs/Nodes_8_Random.dot");
    private static final Graph graph9 = load("exampleTaskGraphs/Nodes_9_SeriesParallel.dot");
    private static final Graph graph10 = load("exampleTaskGraphs/Nodes_10_Random.dot");
    private static final Graph graph11 = load("exampleTaskGraphs/Nodes_11_OutTree.dot");
    private static final Graph graph12 = load("exampleTaskGraphs/16Nodes4Processors.dot");
    private static final Graph graph13 = load("exampleTaskGraphs/16nodes2.dot");
    private static final Graph graph14 = load("exampleTaskGraphs/Nodes_21_Fork_Join.dot");
    private static final Graph graph15 = load("exampleTaskGraphs/Nodes_16_Pipeline.dot");
    private static final Graph graph16 = load("exampleTaskGraphs/Nodes_16_InTree.dot");
    private static final Graph graph17 = load("exampleTaskGraphs/Nodes_16_SeriesParallel.dot");
    private static final Graph graph18 = load("exampleTaskGraphs/Nodes_16_Random.dot");
    private static final Graph graph19 = load("exampleTaskGraphs/Nodes_16_Stencil_Nodes.dot");
    private static final Graph graph20 = load("exampleTaskGraphs/Nodes_16_Independant.dot");
    private static final Graph graph21 = load("exampleTaskGraphs/graph21.dot");
    private static final Graph graphEmpty = load("graph_empty.dot");

    /**
     * Method to load task graph, parses dot file and turns it into a graph object.
     * @param resource Location of task graph
     * @return A graph object generated based on the dot file provided
     */
    private static Graph load(String resource) {
        URL url = TestSequentialDFS.class.getResource(resource);
        try {
            return Parser.parse(url.getPath());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Performs DFSBnB search on the graph passed in.
     * @param g Task graph to perform the algorithm on.
     * @param processors Number of processors allowed for scheduling.
     * @return An optimal schedule found by the algorithm
     */
    private PartialSchedule run(Graph g, int processors) {
        ThreadAnalytics.getInstance(4);
        return SequentialDFS.generateOptimalSchedule(g, processors, 4);
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 7 nodes and 2 processors.
     */
    @Test
    public synchronized void test7Node2Processors() {
        PartialSchedule s = run(graph7, 2);
        Assert.assertEquals(28, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 7 nodes and 4 processors.
     */
    @Test
    public synchronized void test7Node4Processors() {
        PartialSchedule s = run(graph7, 4);
        Assert.assertEquals(22, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 8 nodes and 2 processors.
     */
    @Test
    public synchronized void test8Node2Processors() {
        PartialSchedule s = run(graph8, 2);
        Assert.assertEquals(581, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 8 nodes and 4 processors.
     */
    @Test
    public synchronized void test8Node4Processors() {
        PartialSchedule s = run(graph8, 4);
        Assert.assertEquals(581, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 9 nodes and 2 processors.
     */
    @Test
    public synchronized void test9Node2Processors() {
        PartialSchedule s = run(graph9, 2);
        Assert.assertEquals(55, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 9 nodes and 4 processors.
     */
    @Test
    public synchronized void test9Node4Processors() {
        PartialSchedule s = run(graph9, 4);
        Assert.assertEquals(55, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 10 nodes and 2 processors.
     */
    @Test
    public synchronized void test10Node2Processors() {
        PartialSchedule s = run(graph10, 2);
        Assert.assertEquals(50, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 10 nodes and 4 processors.
     */
    @Test
    public synchronized void test10Node4Processors() {
        PartialSchedule s = run(graph10, 4);
        Assert.assertEquals(50, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 11 nodes and 2 processors.
     */
    @Test
    public synchronized void test11Node2Processors() {
        PartialSchedule s = run(graph11, 2);
        Assert.assertEquals(350, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 11 nodes and 4 processors.
     */
    @Test
    public synchronized void test11Node4Processors() {
        PartialSchedule s = run(graph11, 4);
        Assert.assertEquals(227, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 16 nodes and 4 processors.
     */
    @Test
    public synchronized void test16Node4Processors() {
        PartialSchedule s = run(graph12, 4);
        Assert.assertEquals(520, s.getFinishTime());
    }

    /**
     * Tests if an optimal schedule is found for a graph
     * with 16 nodes and 2 processors.
     */
    @Test
    public synchronized void test16Node2Processors() {
        PartialSchedule s = run(graph13, 2);
        System.out.println(s.getScheduledTasks());
        Assert.assertEquals(582, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Fork Join graph
     * with 21 nodes and 2 processors.
     */
    @Test
    public synchronized void test21Node2ProcessorsForkJoin() {
        PartialSchedule s = run(graph14, 2);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(148, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Pipeline graph
     * with 16 nodes and 4 processors.
     */
    @Test
    public synchronized void test16Node4ProcessorsPipeLine() {
        PartialSchedule s = run(graph15, 4);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(985, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for an In Tree graph
     * with 16 nodes and 6 processors.
     */
    @Test
    public synchronized void test16Node6ProcessorsInTree() {
        PartialSchedule s = run(graph16, 6);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(30, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Series Parallel graph
     * with 16 nodes and 2 processors.
     */
    @Test
    public synchronized void test16Node2ProcessorsSeriesParallel() {
        PartialSchedule s = run(graph17, 2);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(725, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Random graph
     * with 16 nodes and 2 processors.
     */
    @Test
    public synchronized void test16Node2ProcessorsRandom() {
        PartialSchedule s = run(graph18, 2);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(90, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Stencil graph
     * with 16 nodes and 4 processors.
     */
    @Test
    public synchronized void test16Node4ProcessorsStencil() {
        PartialSchedule s = run(graph19, 4);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(52, s.getFinishTime());
    }
    /**
     * Tests if an optimal schedule is found for a Independant graph
     * with 16 nodes and 4 processors.
     */
    @Test
    public synchronized void test16Node4ProcessorsIndependant() {
        PartialSchedule s = run(graph20, 4);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(23, s.getFinishTime());
    }
    @Test
    public synchronized void test10Node6ProcessorsFork() {
        PartialSchedule s = run(graph21, 6);
        System.out.println(s.getFinishTime());
        Assert.assertEquals(22, s.getFinishTime());
    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Tests if no schedule found for an empty graph
     */
    @Test
    public synchronized void testNoSchedules() throws IOException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("No schedules generated!");
        PartialSchedule s = run(graphEmpty, 4);
    }
}

import org.junit.Assert;
import org.junit.Test;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.graph.dotparser.Parser;
import project1.graph.Graph;

import java.io.IOException;
import java.net.URL;

public class TestSequentialDFS {
    private static final Graph graph7 = load("exampleTaskGraphs/Nodes_7_OutTree.dot");
    private static final Graph graph8 = load("exampleTaskGraphs/Nodes_8_Random.dot");
    private static final Graph graph9 = load("exampleTaskGraphs/Nodes_9_SeriesParallel.dot");
    private static final Graph graph10 = load("exampleTaskGraphs/Nodes_10_Random.dot");
    private static final Graph graph11 = load("exampleTaskGraphs/Nodes_11_OutTree.dot");

    private static Graph load(String resource) {
        URL url = TestSequentialDFS.class.getResource(resource);
        try {
            return Parser.parse(url.getPath());
        } catch (IOException e) {
            return null;
        }
    }

    private Schedule run(Graph g, int processors) {
        return SequentialDFS.generateOptimalSchedule(g, processors, 4);
    }

    @Test
    public synchronized void test7Node2Processors() {
        Schedule s = run(graph7, 2);
        Assert.assertEquals(28, s.getFinishTime());
    }

    @Test
    public synchronized void test7Node4Processors() {
        Schedule s = run(graph7, 4);
        Assert.assertEquals(22, s.getFinishTime());
    }

    @Test
    public synchronized void test8Node2Processors() {
        Schedule s = run(graph8, 2);
        Assert.assertEquals(581, s.getFinishTime());
    }

    @Test
    public synchronized void test8Node4Processors() {
        Schedule s = run(graph8, 4);
        Assert.assertEquals(581, s.getFinishTime());
    }

    @Test
    public synchronized void test9Node2Processors() {
        Schedule s = run(graph9, 2);
        Assert.assertEquals(55, s.getFinishTime());
    }

    @Test
    public synchronized void test9Node4Processors() {
        Schedule s = run(graph9, 4);
        Assert.assertEquals(55, s.getFinishTime());
    }

    @Test
    public synchronized void test10Node2Processors() {
        Schedule s = run(graph10, 2);
        Assert.assertEquals(50, s.getFinishTime());
    }

    @Test
    public synchronized void test10Node4Processors() {
        Schedule s = run(graph10, 4);
        Assert.assertEquals(50, s.getFinishTime());
    }

    @Test
    public synchronized void test11Node2Processors() {
        Schedule s = run(graph11, 2);
        Assert.assertEquals(350, s.getFinishTime());
    }

    @Test
    public synchronized void test11Node4Processors() {
        Schedule s = run(graph11, 4);
        Assert.assertEquals(227, s.getFinishTime());
    }
}

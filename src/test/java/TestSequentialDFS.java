import org.junit.Assert;
import org.junit.Test;
import project1.algorithm.Schedule;
import project1.algorithm.SequentialDFS;
import project1.algorithm.ThreadAnalytics;
import project1.graph.dotparser.Parser;
import project1.graph.Graph;

import java.io.IOException;
import java.net.URL;

public class TestSequentialDFS {
    private Schedule run(String resource, int processors) throws IOException {
        URL url = getClass().getResource(resource);
        Graph g = Parser.parse(url.getPath());
        ThreadAnalytics.getInstance(4);
        return SequentialDFS.generateOptimalSchedule(g, processors);
    }

    @Test
    public synchronized void test7Node2Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_7_OutTree.dot", 2);
        Assert.assertEquals(28, s.getFinishTime());
    }

    @Test
    public synchronized void test7Node4Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_7_OutTree.dot", 4);
        Assert.assertEquals(22, s.getFinishTime());
    }

    @Test
    public synchronized void test8Node2Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_8_Random.dot", 2);
        Assert.assertEquals(581, s.getFinishTime());
    }

    @Test
    public synchronized void test8Node4Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_8_Random.dot", 4);
        Assert.assertEquals(581, s.getFinishTime());
    }

    @Test
    public synchronized void test9Node2Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_9_SeriesParallel.dot", 2);
        Assert.assertEquals(55, s.getFinishTime());
    }

    @Test
    public synchronized void test9Node4Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_9_SeriesParallel.dot", 4);
        Assert.assertEquals(55, s.getFinishTime());
    }

    @Test
    public synchronized void test10Node2Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_10_Random.dot", 2);
        Assert.assertEquals(50, s.getFinishTime());
    }

    @Test
    public synchronized void test10Node4Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_10_Random.dot", 4);
        Assert.assertEquals(50, s.getFinishTime());
    }

    @Test
    public synchronized void test11Node2Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_11_OutTree.dot", 2);
        Assert.assertEquals(350, s.getFinishTime());
    }

    @Test
    public synchronized void test11Node4Processors() throws IOException {
        Schedule s = run("exampleTaskGraphs/Nodes_11_OutTree.dot", 4);
        Assert.assertEquals(227, s.getFinishTime());
    }
}

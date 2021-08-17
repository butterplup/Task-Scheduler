import org.junit.Assert;
import org.junit.Test;
import project1.graph.dotparser.Parser;
import project1.graph.Graph;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestDotParser {
    @Test
    public void testEmpty() throws IOException {
        // Pull a graph with no nodes from src/test/resources
        URL empty = getClass().getResource("graph_empty.dot");
        Graph g = Parser.parse(empty.getPath());

        // Check that the graph name is correct and that there are no nodes or edges
        Assert.assertEquals(g.getName(), "empty");
        Assert.assertEquals(g.getEdges().size(), 0);
        Assert.assertEquals(g.getNodes().size(), 0);
    }

    @Test
    public void testGraph() throws IOException {
        // Pull a graph with nodes from src/test/resources
        URL graph = getClass().getResource("graph.dot");
        Graph g = Parser.parse(graph.getPath());

        // Check that the graph name is correct and there are the correct amount of nodes and edges
        Assert.assertEquals(g.getName(), "graph");
        Assert.assertEquals(g.getEdges().size(), 6);
        Assert.assertEquals(g.getNodes().size(), 7);
    }

    @Test
    public void testGraphWithDescription() throws IOException {
        // Pull a graph with nodes and a description from src/test/resources
        URL graph = getClass().getResource("graph_with_description.dot");
        Graph g = Parser.parse(graph.getPath());

        // Check that the graph name is correct and there are the correct amount of nodes and edges
        Assert.assertEquals(g.getName(), "graph with description");
        Assert.assertEquals(g.getEdges().size(), 19);
        Assert.assertEquals(g.getNodes().size(), 10);
    }

    @Test
    public void testGraphWithNoName() throws IOException {
        // Pull a graph with nodes and no name from src/test/resources
        URL graph = getClass().getResource("graph_with_no_name.dot");
        Graph g = Parser.parse(graph.getPath());

        // Check that the graph name is set to default and there are the correct amount of nodes and edges
        Assert.assertEquals(g.getName(), "digraph");
        Assert.assertEquals(g.getEdges().size(), 6);
        Assert.assertEquals(g.getNodes().size(), 7);
    }

    @Test
    public void testOutputFile() throws IOException {
        // Pull a graph with nodes from src/test/resources
        URL graph = getClass().getResource("graph.dot");
        Graph g = Parser.parse(graph.getPath());

        Parser.saveToFile(g, "test_name.dot");

        // Test file exists
        File file = new File("test_name.dot");
        Assert.assertTrue(file.exists());

        // Test file can be read in correctly
        URL testGraph = getClass().getResource("test_name.dot");
        Graph testG = Parser.parse(graph.getPath());
        Assert.assertEquals(testG.getName(), "graph");
        Assert.assertEquals(testG.getEdges().size(), 6);
        Assert.assertEquals(testG.getNodes().size(), 7);

        // Delete test file
        file.delete();
    }

    @Test
    public void testGraphWithNodesThatAreLetters() throws IOException {
        // Pull a graph that contains nodes that are letters from src/test/resources
        URL graph = getClass().getResource("graph_with_letter_nodes.dot");
        Graph g = Parser.parse(graph.getPath());

        // Check that the graph name is set to default and there are the correct amount of nodes and edges
        Assert.assertEquals(g.getName(), "digraph");
        Assert.assertEquals(g.getEdges().size(), 6);
        Assert.assertEquals(g.getNodes().size(), 7);
    }

    @Test
    public void testGraphFunctions() throws IOException {
        // Pull a graph with nodes from src/test/resources
        URL graph = getClass().getResource("graph.dot");
        Graph g = Parser.parse(graph.getPath());
        g.print();
        Assert.assertEquals(g.getTotalTasksCount(),7);
    }
}
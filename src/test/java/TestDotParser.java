import org.junit.Assert;
import org.junit.Test;
import project1.graph.DotParser;
import project1.graph.Graph;

import java.io.IOException;
import java.net.URL;

public class TestDotParser {
    @Test
    public void testEmpty() throws IOException {
        // Pull a graph with no nodes from src/test/resources
        URL empty = getClass().getResource("empty.dot");
        Graph g = DotParser.parse(empty.getPath());

        // Check that the graph name is correct and that there are no nodes or edges
        Assert.assertEquals(g.getName(), "empty");
        Assert.assertEquals(g.getEdges().size(), 0);
        Assert.assertEquals(g.getNodes().size(), 0);
    }
}
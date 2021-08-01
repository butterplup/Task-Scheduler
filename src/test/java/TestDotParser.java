import org.junit.Assert;
import org.junit.Test;
import project1.graph.DotParser;
import project1.graph.Graph;

import java.io.IOException;

public class TestDotParser {
    @Test
    public void testEmpty() throws IOException {
        Graph g = DotParser.parse("target/test-classes/empty.dot");
        Assert.assertEquals(g.getName(), "empty");
        Assert.assertEquals(g.getEdges().size(), 0);
        Assert.assertEquals(g.getNodes().size(), 0);
    }
}
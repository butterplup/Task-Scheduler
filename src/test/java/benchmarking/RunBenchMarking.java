package benchmarking;



import project1.graph.Graph;
import project1.graph.dotparser.Parser;
import org.junit.Test;
import java.io.IOException;


public class RunBenchMarking {

    String thisfile="./src/test/resources/exampleTaskGraphs/Nodes_11_OutTree.dot";
    Graph g = Parser.parse(thisfile);

    public RunBenchMarking() throws IOException {
    }

    @Test
    public void testDFS(){

        new BenchMarking(g,2).benchMarkingDFS(20);
    }
    @Test
    public void testTopo(){
        new BenchMarking(g,2).benchmarkingTopologicalSort(10000000);
    }


}



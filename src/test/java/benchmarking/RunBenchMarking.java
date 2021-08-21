package benchmarking;



import project1.graph.Graph;
import project1.graph.dotparser.Parser;

import java.io.IOException;

public class RunBenchMarking {
    public static void main(String[] args) throws IOException {
        String thisfile="./src/test/resources/exampleTaskGraphs/Nodes_11_OutTree.dot";
        Graph g = Parser.parse(thisfile);
        new BenchMarking(g,2).benchMarkingDFS(20);
    }
}

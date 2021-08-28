package project1.graph.dotparser;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.io.*;

public class Parser {
    /**
     * Use the PayPal dot parser and convert it to our internal representaion.
     *
     * @param filename The .dot file to read in
     * @return The Graph object
     * @throws IOException When the file cannot be opened
     */
    public static Graph parse(String filename) throws IOException {
        GraphParser parser = new GraphParser(new FileInputStream(filename));

        // Create a graph object, removing trailing and leading double quotes
        Graph graph = new Graph("digraph");

        // Set name if non-empty
        String name = parser.getGraphId().replaceAll("^\"|\"$", "");
        if (name.length() > 0) {
            graph.setName(name);
        }



        // Add all graph nodes
        for (GraphNode n : parser.getNodes().values()) {
            int weight = Integer.parseInt((String) n.getAttribute("Weight"));
            Node newNode = new Node(weight, n.getId());
            graph.addNode(newNode);
            boolean[] predecessors=new boolean[parser.getNodes().values().size()];
            newNode.setPredecessors(predecessors);
        }

        // Add all graph edges
        for (GraphEdge e : parser.getEdges().values()) {
            int weight = Integer.parseInt((String) e.getAttribute("Weight"));
            Node start = graph.getNodeMap().get(e.getNode1().getId());
            Node end = graph.getNodeMap().get(e.getNode2().getId());

            Edge newEdge = new Edge(weight, start, end);
            graph.addEdge(newEdge);
        }

        graph.markNodeOrder();

        return graph;
    }

    /**
     * Save a Graph to the filename specified
     *
     * @param g The Graph object
     * @param filename The filename to save to
     * @throws IOException When the file cannot be opened
     */
    public static void saveToFile(Graph g, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(String.format("digraph \"%s\" {%n", g.getName()));

        // Write nodes and weights
        for (Node n : g.getNodes()) {
            bw.write(String.format("\t%s\t [Weight=%d, Start=%d, Processor=%d];%n",
                    n.getName(), n.getWeight(), n.getStart(), n.getProcessor()));
        }

        for (Edge e : g.getEdges()) {
            bw.write(String.format("\t%s-> %s\t [Weight=%d];%n",
                    e.getStart().getName(), e.getEnd().getName(), e.getWeight()));
        }

        bw.write(String.format("}%n%n"));
        bw.close();
    }
}

package project1.graph.dotparser;

import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.GraphObject;
import project1.graph.Node;

import java.util.HashMap;
import java.io.*;

public class Parser {
    /**
     * Read a given file into a Graph object
     *
     * @param filename The .dot file to read in
     * @return The Graph object
     * @throws IOException When the file cannot be opened
     */
    public static Graph parse(String filename) throws IOException {
        // Create a buffered reader from the specified file
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Create an empty graph named "digraph"
        Graph graph = new Graph("digraph");

        // Read line-by-line
        String st;
        while ((st = br.readLine()) != null) {
            // Try to parse the line
            try {
                GraphObject o = GraphObjectFactory.getGraphObject(st, graph.getNodeMap());
                o.addTo(graph);
            } catch (GraphObjectFactory.UnknownSyntaxException e) {
                // Failing that, ignore the line
            }
        }

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

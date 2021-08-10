package project1.graph.dotparser;

import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.util.HashMap;
import java.io.*;

public class Parser {
    /**
     * Convert an option string to a HashMap
     * @param optionString Option string from GraphViz file
     * @return HashMap representing options and their values
     */
    private static HashMap<String, String> optionsToHashmap(String optionString) {
        HashMap<String, String> map = new HashMap<>();

        for (String i: optionString.split(",")) {
            String[] keyVal = i.split("=");
            String key = keyVal[0].trim();
            String value = keyVal[1].trim();

            map.put(key, value);
        }

        return map;
    }

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
            Line l;

            // Try to parse the line
            try {
                l = new Line(st);
            } catch (Line.UnknownSyntaxException e) {
                // Failing that, ignore the line
                continue;
            }

            // Name of the node / digraph
            String nodeName = l.getMatcher().group(1);
            // Option string as a hashmap
            HashMap<String, String> options;

            switch (l.getType()) {
                case DIGRAPH:
                    // Override the graph name if a non-empty one is specified
                    if (nodeName.trim().length() > 0) {
                        graph.setName(nodeName);
                    }

                    break;
                case NODE:
                    // Extract node options
                    options = optionsToHashmap(l.getMatcher().group(2));

                    // Add the node to the graph
                    int value = Integer.parseInt(options.get("Weight"));
                    graph.addNode(nodeName, value);

                    break;
                case EDGE:
                    // Extract the other party to this edge and edge options
                    String to = l.getMatcher().group(2);
                    options = optionsToHashmap(l.getMatcher().group(3));

                    // Add the edge to the graph
                    int weight = Integer.parseInt(options.get("Weight"));
                    graph.addEdge(weight, nodeName, to);
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

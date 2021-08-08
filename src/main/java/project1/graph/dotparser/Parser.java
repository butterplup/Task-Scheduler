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

    public static Graph parse(String filename) throws IOException {
        // Create a buffered reader from the specified file
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));

        Graph graph = new Graph("digraph");

        String st;
        while ((st = br.readLine()) != null) {
            Line l;

            try {
                l = new Line(st);
            } catch (Line.UnknownSyntaxException e) {
                continue;
            }

            // Skip if we don't recognise the line
            if (l.getType() == null) {
                continue;
            }

            // Name of the node/digraph
            String nodeName = l.getMatcher().group(1);
            // Option string as a hashmap
            HashMap<String, String> options;

            switch (l.getType()) {
                case DIGRAPH:
                    if (nodeName.trim().length() > 0) {
                        graph.setName(nodeName);
                    }

                    break;
                case NODE:
                    options = optionsToHashmap(l.getMatcher().group(2));

                    int value = Integer.parseInt(options.get("Weight"));
                    graph.addNode(nodeName, value);

                    break;
                case EDGE:
                    String to = l.getMatcher().group(2);
                    options = optionsToHashmap(l.getMatcher().group(3));

                    int weight = Integer.parseInt(options.get("Weight"));
                    graph.addEdge(weight, nodeName, to);
            }
        }

        return graph;
    }

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

package project1.graph;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class DotParser {
    /**
     * Parse in a graph dot file, into a graph representation
     * @param filename String containing filename of the file
     * @return a Graph instance, representing the graph in the file
     * @throws IOException
     */
    public static Graph parse(String filename) throws IOException {
        //User inputs path to DOT file then number of threads to use
        //start by getting the strings
        List<String> nodeLines = new ArrayList<>();
        List<String> edgeLines = new ArrayList<>();

        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Name of the graph
        String graphName = "digraph";

        // This string will store each line as it is read in
        String st;

        // Read in each line of the file
        while ((st = br.readLine()) != null) {
            if (st.contains("{")) {
                // Pull name of graph from the String between double quotes
                int start = st.indexOf("\"");
                int end = st.lastIndexOf("\"");
                String subString = st.substring(start+1, end);

                // Ensure the graph name is non-empty before overriding the default
                if (subString.trim().length() > 0) {
                    graphName = subString;
                }
            } else if (st.contains("}")) {
                break;
            } else {
                if (st.toLowerCase().contains("Weight".toLowerCase())) {
                    // Check if edge or node
                    if (st.contains("->")) {
                        edgeLines.add(st);
                    } else {
                        nodeLines.add(st);
                    }
                }
            }
        }

        Graph graph = new Graph(graphName);

        //Make Node instances
        for (String line : nodeLines) {
            String myLine = line.replaceAll(" ", "");
            myLine = myLine.replaceAll("\t", "");
            String name = myLine.substring(0,myLine.indexOf('['));
            String value = myLine.substring(myLine.indexOf('=') + 1,myLine.indexOf(']'));
            graph.addNode(name, Integer.valueOf(value));
        }

        //Make Edge instances
        for (String line : edgeLines) {
            String myLine = line.replaceAll(" ", "");
            myLine = myLine.replaceAll("\t", "");
            String start = myLine.substring(0,myLine.indexOf('-'));
            String end = myLine.substring(myLine.indexOf('>') + 1,myLine.indexOf('['));
            String value = myLine.substring(myLine.indexOf('=') + 1,myLine.indexOf(']'));
            graph.addEdge(Integer.valueOf(value), start, end);
        }

        return graph;
    }

    /**
     * Output a graph into a dot file
     * @param g the graph to be outputted
     * @param filename the filename of the file to be outputted to
     * @throws IOException
     */
    public static void saveToFile(Graph g, String filename) throws IOException {
        // Create file with specified name
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

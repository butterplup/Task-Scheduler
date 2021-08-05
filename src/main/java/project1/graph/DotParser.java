package project1.graph;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class DotParser {
    public static Graph parse(String filename) throws IOException {
        //presumably user inputs path to DOT file then number of threads to use
        //start by getting the strings
        List<String> nodeLines = new ArrayList<>();
        List<String> edgeLines = new ArrayList<>();

        File file = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(file));

        // Name of the graph
        String graphName = "digraph";

        String st;
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
                // Check if first character of line is a number
                char c = st.replaceAll(" ", "").replaceAll("\t", "").charAt(0);
                if (c >= '0' && c <= '9') {
                    if (st.contains("->")) {
                        edgeLines.add(st);
                    } else {
                        nodeLines.add(st);
                    }
                }
            }
        }

        Graph graph = new Graph(graphName);

        //Make nodes
        for (String line : nodeLines) {
            String myLine = line.replaceAll(" ", "");
            myLine = myLine.replaceAll("\t", "");
            String name = myLine.substring(0,myLine.indexOf('['));
            String value = myLine.substring(myLine.indexOf('=') + 1,myLine.indexOf(']'));
            graph.addNode(name, Integer.valueOf(value));
        }

        //Make edges
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

    public static void saveToFile(Graph g, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(String.format("digraph \"%s\" {%n", g.getName()));

        // Write nodes and weights
        for (Node n : g.getNodes()) {
            bw.write(String.format("\t%s\t [Weight=%d];%n", n.getName(), n.getWeight()));
        }

        for (Edge e : g.getEdges()) {
            bw.write(String.format("\t%s-> %s\t [Weight=%d];%n",
                    e.getStart().getName(), e.getEnd().getName(), e.getWeight()));
        }

        bw.write(String.format("}%n%n"));
        bw.close();
    }
}

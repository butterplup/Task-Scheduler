package project1.graph;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class DotParser {
    public static Graph parse(String filename) throws IOException {
        Graph graph = new Graph();
        //presumably user inputs path to DOT file then number of threads to use
        //start by getting the strings
        List<String> nodeLines = new ArrayList<>();
        List<String> edgeLines = new ArrayList<>();

        File file = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            //skip first and last line?
            if (!st.contains("{") && !st.contains("}")) {
                if (st.contains("−>")) {
                    edgeLines.add(st);
                } else {
                    nodeLines.add(st);
                }
            }
        }

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
            String start = myLine.substring(0,myLine.indexOf('−'));
            String end = myLine.substring(myLine.indexOf('>') + 1,myLine.indexOf('['));
            String value = myLine.substring(myLine.indexOf('=') + 1,myLine.indexOf(']'));
            graph.addEdge(Integer.valueOf(value), start, end);
        }

        return graph;
    }
}

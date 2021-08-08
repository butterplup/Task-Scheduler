package project1.graph;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotParser {
    // Regex patterns to match GraphViz content
    private static final Pattern digraphPattern = Pattern.compile("digraph\\s*\"(.*)\" \\{");
    private static final Pattern nodePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");
    private static final Pattern edgePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*->\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");

    @Getter
    @AllArgsConstructor
    private static class Line {
        private final LineType type;
        private final Matcher m;

        public enum LineType {
            DIGRAPH, NODE, EDGE, OTHER
        }
    }

    /**
     * Takes a line and determines its type in addition to its Regex matcher object
     * @param line Line to tokenize
     *
     * @return Line object
     */
    private static Line tokenize(String line) {
        // Patterns corresponding to LineType enum values
        Pattern[] patterns = {digraphPattern, nodePattern, edgePattern};

        for (int i = 0; i < patterns.length; i++) {
            Matcher m = patterns[i].matcher(line);

            // If a matcher matches, return a Line object
            if (m.find()) {
                return new Line(Line.LineType.values()[i], m);
            }
        }

        return new Line(Line.LineType.OTHER, null);
    }

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
            Line l = tokenize(st);

            // Name of the node/digraph
            String name;
            // Option string as a hashmap
            HashMap<String, String> options;

            switch (l.getType()) {
                case DIGRAPH:
                    name = l.getM().group(1);

                    if (name.trim().length() > 0) {
                        graph.setName(name);
                    }

                    break;
                case NODE:
                    name = l.getM().group(1);
                    options = optionsToHashmap(l.getM().group(2));

                    int value = Integer.parseInt(options.get("Weight"));
                    graph.addNode(name, value);
                    break;
                case EDGE:
                    String from = l.getM().group(1);
                    String to = l.getM().group(2);

                    options = optionsToHashmap(l.getM().group(3));

                    int weight = Integer.parseInt(options.get("Weight"));
                    graph.addEdge(weight, from, to);
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

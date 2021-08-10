package project1.graph.dotparser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project1.graph.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class GraphObjectFactory {
    // Regex patterns to match GraphViz content
    private static final Pattern digraphPattern = Pattern.compile("digraph\\s*\"(.*)\" \\{");
    private static final Pattern nodePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");
    private static final Pattern edgePattern = Pattern.compile("^\\s*([a-zA-Z0-9]*)\\s*->\\s*([a-zA-Z0-9]*)\\s*\\[([a-zA-Z0-9=]*)]\\s*;");

    /**
     * An exception thrown by Line when the syntax of a line is unknown to the parser
     */
    public static class UnknownSyntaxException extends Exception {
        public UnknownSyntaxException(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * Build a Line object from a line in the .dot file
     *
     * @param line The line
     * @throws UnknownSyntaxException When we don't know how to parse this line
     */
    public static GraphObject getGraphObject(String line, HashMap<String, Node> nodeMap) throws UnknownSyntaxException {
        // Check if this line sets the graph name
        Matcher matcher = digraphPattern.matcher(line);
        if (matcher.find()) {
            return new GraphName(matcher.group(1));
        }

        // Check if this line defines a node
        matcher = nodePattern.matcher(line);
        if (matcher.find()) {
            // Name of the node
            String nodeName = matcher.group(1);

            // Extract node options
            HashMap<String, String> options = optionsToHashmap(matcher.group(2));

            // Add the node to the graph
            int value = Integer.parseInt(options.get("Weight"));
            return new Node(value, nodeName);
        }

        // Check if this line defines an edge
        matcher = edgePattern.matcher(line);
        if (matcher.find()) {
            // Name of the start edge
            String from = matcher.group(1);

            // Extract the other party to this edge and edge options
            String to = matcher.group(2);
            HashMap<String, String> options = optionsToHashmap(matcher.group(3));

            // Add the edge to the graph
            int weight = Integer.parseInt(options.get("Weight"));
            return new Edge(weight, nodeMap.get(from), nodeMap.get(to));
        }

        throw new UnknownSyntaxException("Unknown syntax!");
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
}
package project1.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Node {
    private int weight;
    private String name;
    private List<Edge> edges;
    @Setter private int processor;
    @Setter private int start;
    private HashMap<Node, Integer> incomingEdges = new HashMap<>();
    private HashMap<Node,Integer> outgoingEdges = new HashMap<>();

    /**
     * Constructor for a Node object
     * @param value the value (weight) of the specified node
     * @param nodeName the name of the node
     */
    public Node(int value, String nodeName) {
        weight = value;
        name = nodeName;
        edges = new ArrayList<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

}

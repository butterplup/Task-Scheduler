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

    /**
     * Checks if a given node is pointed to by this node
     * @param node the node to be checked
     * @return whether the node is pointed to by this node
     */
    public boolean checkNode(Node node){
        for(Edge cEdge: edges){
            if(cEdge.getEnd().equals(node)){
                return true;
            }
        }
        return false;
    }

    /**
     * Add an edge
     * @param edge the edge that this node is part of
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

}

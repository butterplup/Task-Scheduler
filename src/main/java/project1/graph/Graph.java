package project1.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Class representing a task graph
 */
@Getter
public class Graph {
    @Setter private String name;
    private final List<Edge> edges;
    private final List<Node> nodes;
    private final HashMap<String, Node> nodeMap;

    public Graph(String name) {
        edges = new ArrayList<>();
        nodes = new ArrayList<>();
        nodeMap = new HashMap<>();
        this.name = name;
    }

    /**
     * Add a node to the graph
     * @param n Node to add
     */
    public void addNode(Node n) {
        // No duplicate nodes
        if (!nodeMap.containsKey(n.getName())) {
            n.setId(nodes.size());
            nodes.add(n);
            nodeMap.put(n.getName(), n);
        }
    }

    /**
     * Add an edge to the graph
     * @param e Edge to add
     */
    public void addEdge(Edge e) {
        // Add to edges in graph
        edges.add(e);

        // Start to end
        e.getStart().getOutgoingEdges().add(e);
        // End from start
        e.getEnd().getIncomingEdges().add(e);
    }

    /**
     * Display the task graph to the console
     */
    public void print() {
        System.out.printf("Graph \"%s\":%n", name);

        // Display the nodes in the graph
        for (Node n : nodes) {
            System.out.printf("Node: %s value: %d%n", n.getName(), n.getWeight());
        }

        // Display the edges in the graph
        for (Edge e : edges) {
            System.out.printf("Edge from: %s to: %s weight: %d%n", e.getStart().getName(), e.getEnd().getName(), e.getWeight());
        }
    }

    /**
     * Get the number of tasks in the task graph
     * @return The number of tasks as an int
     */
    public int getTotalTasksCount(){return this.nodeMap.size();}
}

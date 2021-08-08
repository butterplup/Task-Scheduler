package project1.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Class representing a task graph
 */
@Getter
public class Graph {
    private String name;
    private List<Edge> edges;
    private List<Node> nodes;
    private HashMap<String,Node> nodeMap;

    public Graph(String name) {
        edges = new ArrayList<>();
        nodes = new ArrayList<>();
        nodeMap = new HashMap<>();
        this.name = name;
    }

    /**
     * Topologically sort the task graph in-place, producing a DAG
     */
    public void sort() {
        // For every node excluding the last
        for (int i = 0; i < nodes.size() - 1; i++){
            Node cNode = nodes.get(i);
            Node nNode = nodes.get(i+1);

            for (int j = i; j < nodes.size(); j++) {
                if (cNode.checkNode(nNode)) {
                    swap(i, i + 1);

                    // Reset outer loop
                    i = -1;
                    break;
                }
            }
        }
    }

    /**
     * Swap two nodes, given their indices in the nodes List
     * @param indexA The index of the first node
     * @param indexB The index of the second node
     */
    public void swap(int indexA, int indexB){
        Node nodeA = nodes.get(indexA);
        Node nodeB = nodes.get(indexB);
        nodes.set(indexA, nodeB);
        nodes.set(indexB, nodeA);
    }

    /**
     * Add a node to the graph
     * @param name Name of the node
     * @param value Weight of the node
     */
    public void addNode(String name, int value) {
        //no duplicate nodes
        if (!nodeMap.containsKey(name)) {
            Node node = new Node(value, name);
            nodes.add(node);
            nodeMap.put(name, node);
        }
    }

    /**
     * Add an edge to the graph
     * @param weight Weight of the edge
     * @param start "From" Node
     * @param end "To" Node
     */
    public void addEdge(int weight, String start, String end) {
        Node from = nodeMap.get(start);
        Node to = nodeMap.get(end);
        Edge edge = new Edge(weight, from, to);
        edges.add(edge);
        from.addEdge(edge);
        //add incoming edge to node
        to.getIncomingEdges().put(from,weight);
        //add outgoing edge to node
        from.getOutgoingEdges().put(to,weight);
    }

    /**
     * Get a node, given its name
     * @param value Name of the node as a String
     * @return The Node
     */
    public Node getNode(String value) {
        return nodeMap.get(value);
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

package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Graph {
    private List<Edge> edges;
    private List<Node> nodes;
    private HashMap<String,Node> nodeMap;

    public Graph() {
        edges = new ArrayList<>();
        nodes = new ArrayList<>();
        nodeMap = new HashMap<>();
    }

    public void addNode(String name, int value) {
        //no duplicate nodes
        if (!nodeMap.containsKey(name)) {
            Node node = new Node(value, name);
            nodes.add(node);
            nodeMap.put(name, node);
        }
    }

    public void addEdge(int weight, String start, String end) {
        Node from = nodeMap.get(start);
        Node to = nodeMap.get(end);
        Edge edge = new Edge(weight, from, to);
        edges.add(edge);
        from.addEdge(edge);
    }

    public Node getNode(int value) {
        return nodeMap.get(value);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}

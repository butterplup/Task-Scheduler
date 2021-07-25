package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int weight;
    private String name;
    private List<Edge> edges;

    public Node(int value, String nodeName) {
        weight = value;
        name = nodeName;
        edges = new ArrayList<Edge>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() { return name; }
}

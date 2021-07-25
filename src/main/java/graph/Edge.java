package graph;

public class Edge {
    private int weight;
    private Node start;
    private Node end;

    public Edge(int value, Node startNode, Node endNode) {
        weight = value;
        start = startNode;
        end = endNode;
    }

    public int getEdgeWeight() {
        return weight;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }
}

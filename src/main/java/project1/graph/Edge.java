package project1.graph;

import lombok.Getter;

@Getter
public class Edge {
    private int weight;
    private Node start;
    private Node end;

    public Edge(int value, Node startNode, Node endNode) {
        weight = value;
        start = startNode;
        end = endNode;
    }
}

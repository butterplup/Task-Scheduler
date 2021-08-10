package project1.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Node implements GraphObject {
    private final int weight;
    private final String name;
    @Setter private int processor;
    @Setter private int start;

    // Incoming and outgoing edges, set when an edge is added to the graph
    private final List<Edge> incomingEdges = new ArrayList<>();
    private final List<Edge> outgoingEdges = new ArrayList<>();

    public void addTo(Graph g) {
        g.addNode(this);
    }
}

package project1.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
public class Node {
    @Getter private final int weight;
    @Getter private final String name;
    @Getter @Setter private int processor;
    @Getter @Setter private int start;
    @Getter @Setter private int id = 0;
    @Getter @Setter private int order=0;

    // Incoming and outgoing edges, set when an edge is added to the graph
    @Getter private final List<Edge> incomingEdges = new ArrayList<>();
    @Getter private final List<Edge> outgoingEdges = new ArrayList<>();
    @Getter private final HashSet<Integer> predecessors = new HashSet<>();

    // Cache critical path length
    private int criticalPath = 0;

    /**
     * Get the longest path length from this node in the graph.
     * @return The path length
     */
    public int getCriticalPath(){
        // Calculate path if not done already
        if (criticalPath == 0) {
            // For all outgoing edges
            for (Edge e : outgoingEdges) {
                criticalPath = Math.max(e.getEnd().getCriticalPath() + e.getEnd().getWeight(), criticalPath);
            }
        }

        return criticalPath;
    }
}

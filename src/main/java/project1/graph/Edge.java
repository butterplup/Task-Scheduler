package project1.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An Edge in the Graph with a weight + start and end Nodes
 */
@Getter
@AllArgsConstructor
public class Edge implements GraphObject {
    private final int weight;
    private final Node start;
    private final Node end;

    @Override
    public void addTo(Graph g) {
        g.addEdge(this);
    }
}

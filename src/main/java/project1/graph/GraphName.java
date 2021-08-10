package project1.graph;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GraphName implements GraphObject {
    private final String name;

    public void addTo(Graph g) {
        if (name.trim().length() > 0) {
            g.setName(name);
        }
    }
}

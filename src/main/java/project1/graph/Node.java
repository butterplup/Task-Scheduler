package project1.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Node {
    private final int weight;
    private int criticalPath = 0;
    private final String name;
    @Setter private int processor;
    @Setter private int start;
    @Setter private int id = 0;

    //Calculates the longest path length originating from this node
    public int CalcPath(){
        //If the path has been calculated then no need to recalculate
        if(criticalPath != 0){
            return criticalPath;
        }
        int CPath = 0;
        for(Edge e: outgoingEdges){
            if( e.getEnd().CalcPath() + e.getEnd().getWeight() > CPath) {
                CPath = e.getEnd().getCriticalPath() + e.getEnd().getWeight();
            }
        }

        this.criticalPath = CPath;
        return CPath;
    }

    // Incoming and outgoing edges, set when an edge is added to the graph
    private final List<Edge> incomingEdges = new ArrayList<>();
    private final List<Edge> outgoingEdges = new ArrayList<>();
}

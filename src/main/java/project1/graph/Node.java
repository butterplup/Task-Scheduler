package project1.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Node {
    @Getter private final int weight;
    @Getter private final String name;
    @Getter @Setter private int processor;
    @Getter @Setter private int start;
    @Getter @Setter private int id = 0;
    @Getter @Setter private int order=0;
    @Getter @Setter private boolean[] predecessors;

    // Incoming and outgoing edges, set when an edge is added to the graph
    @Getter private final List<Edge> incomingEdges = new ArrayList<>();
    @Getter private final List<Edge> outgoingEdges = new ArrayList<>();

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

    //Calculates if this node and another node are on the same 'level' with the same parents and children
    public boolean sameLevel(Node other){
        if(other.outgoingEdges.size() != this.outgoingEdges.size() && other.incomingEdges.size() != this.incomingEdges.size()){
            return false;
        }
        for(Edge te : this.incomingEdges){
            for (Edge oe : other.incomingEdges){
                if(te.getStart()!= oe.getStart()){
                    return false;
                }
            }
        }
        for(Edge te : this.outgoingEdges){
            for (Edge oe : other.outgoingEdges){
                if(te.getEnd()!= oe.getEnd()){
                    return false;
                }
            }
        }
//        System.out.println("true " + this.name + " " + other.getName());
        return true;
    }
    public int getMinIncoming(){
        int min = Integer.MAX_VALUE;
        for(Edge e : incomingEdges){
            min = Math.min(min, e.getWeight());
        }
        return min;
    }
    public int getMaxIncoming(){
        int max = Integer.MIN_VALUE;
        for(Edge e : incomingEdges){
            max = Math.max(max, e.getWeight());
        }
        return max;
    }
    public int getMinOutgoing(){
        int min = Integer.MAX_VALUE;
        for(Edge e : outgoingEdges){
            min = Math.min(min, e.getWeight());
        }
        return min;
    }
    public int getMaxOutgoing(){
        int max = Integer.MIN_VALUE;
        for(Edge e : outgoingEdges){
            max = Math.max(max, e.getWeight());
        }
        return max;
    }

    public boolean strictLater(Node other){

        if (this.sameLevel(other) && this.id != other.getId()) {
            if (this.getMinIncoming() > other.getMaxIncoming()) {
                if (this.weight < other.getWeight()) {
                    if (this.getMaxOutgoing()  < other.getMinOutgoing()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPrecededBy(int nodeId){
        return predecessors[nodeId];
    }
}

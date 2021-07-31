package project1.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    private int weight;
    private String name;
    private List<Edge> edges;
    private HashMap<Node, Integer> incomingEdges=new HashMap<>();
    private HashMap<Node,Integer> outgoingEdges=new HashMap<>();

    public Node(int value, String nodeName) {
        weight = value;
        name = nodeName;
        edges = new ArrayList<>();
    }

    public boolean checkNode(Node node){
        for(Edge cEdge: edges){
            if(cEdge.getEnd().equals(node)){
                return true;
            }
        }
        return false;
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

    public HashMap<Node,Integer> getIncomingEdges(){ return this.incomingEdges; }

    public HashMap<Node,Integer> getOutgoingEdges(){ return this.outgoingEdges; }




}

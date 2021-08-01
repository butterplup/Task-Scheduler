package project1.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Graph {
    private String name;
    private List<Edge> edges;
    private List<Node> nodes;
    private HashMap<String,Node> nodeMap;

    public Graph(String name) {
        edges = new ArrayList<>();
        nodes = new ArrayList<>();
        nodeMap = new HashMap<>();
        this.name = name;
    }

    public void sort(){
        int i= 0,j;
        Node cNode, nNode;
        while(i < nodes.size()){
            cNode = nodes.get(i);
            nNode = nodes.get(i+1);
            for(j = i; j < nodes.size(); j++) {
                if (cNode.checkNode(nNode)) {
                    swap(i, i + 1);
                    i = -1;
                    break;
                }
            }
            i++;
        }
    }

    public void swap(int indexA, int indexB){
        Node nodeA = nodes.get(indexA);
        Node nodeB = nodes.get(indexB);
        nodes.remove(indexA);
        nodes.remove(indexB);
        nodes.add(indexA,nodeB);
        nodes.add(indexB,nodeA);
    }

    public void addNode(String name, int value) {
        //no duplicate nodes
        if (!nodeMap.containsKey(name)) {
            Node node = new Node(value, name);
            nodes.add(node);
            nodeMap.put(name, node);
        }
    }

    public void addEdge(int weight, String start, String end) {
        Node from = nodeMap.get(start);
        Node to = nodeMap.get(end);
        Edge edge = new Edge(weight, from, to);
        edges.add(edge);
        from.addEdge(edge);
        //add incoming edge to node
        to.getIncomingEdges().put(from,weight);
        //add outgoing edge to node
        from.getOutgoingEdges().put(to,weight);
    }

    public String getName() {
        return name;
    }

    public Node getNode(int value) {
        return nodeMap.get(value);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void print() {
        System.out.printf("Graph \"%s\":%n", name);

        // Display the nodes in the graph
        for (Node n : nodes) {
            System.out.printf("Node: %s value: %d%n", n.getName(), n.getWeight());
        }

        // Display the edges in the graph
        for (Edge e : edges) {
            System.out.printf("Edge from: %s to: %s weight: %d%n", e.getStart().getName(), e.getEnd().getName(), e.getEdgeWeight());
        }
    }

    public int getTotalTasksCount(){return this.nodeMap.size();}
}

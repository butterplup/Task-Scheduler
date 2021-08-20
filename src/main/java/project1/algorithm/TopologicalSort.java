package project1.algorithm;

import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TopologicalSort {

    public TopologicalSort(){
    }

    //Using Kahn's algorithm
    public PartialSchedule getSchedule(Graph g, int processorsCount){

        LinkedList<Node> nodesDone = new LinkedList<>();

        ArrayList<Node> tempNodes = (ArrayList<Node>) g.getNodes();

        //Sorts list of nodes from graph based on critical path
        Collections.sort(tempNodes, (n1, n2) -> n2.getCriticalPath() - n1.getCriticalPath());

        //Converts array list to linked list
        for(Node n: tempNodes){
            nodesDone.add(n);
        }

        PartialSchedule topoSchedule=new PartialSchedule(g,processorsCount);

        while (!nodesDone.isEmpty()){
            Node currentNode= nodesDone.removeFirst();  //Get a node to schedule

            int index=-1;
            int bestProcessor=index;
            int currentEarliest=Integer.MAX_VALUE;
            TaskScheduled[] scheduled= topoSchedule.getScheduledTasks();
            int[] procinfo=topoSchedule.getProcessorInfo();

            //go through all processors,Use earliestStartTime as selection rule
            for (int free : topoSchedule.getProcessorInfo()) {
                index++; //processor number
                int startTime;
                int communicationCost = 0;
                // Find the earliest starting time on this processor p
                for (Edge e : currentNode.getIncomingEdges()) {
                    Node pre = e.getStart(); // Get parent node
                    TaskScheduled predecessor = scheduled[pre.getId()];
                    if (predecessor.getProcessor() != index) { //communication cost
                        communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + e.getWeight());
                    }
                }
                startTime = Math.max(free, communicationCost);

                if (startTime<currentEarliest){
                    currentEarliest=startTime;
                    bestProcessor=index;
                }

                if (currentEarliest==0){
                    //can not have better EST time
                    break;
                }
            }
            //Assign task to this processor
            TaskScheduled ts = new TaskScheduled(currentNode, currentEarliest, bestProcessor);
            topoSchedule=new PartialSchedule(topoSchedule,ts);
        }
        return topoSchedule;
    }
}

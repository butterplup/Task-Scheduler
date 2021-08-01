package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;
import project1.processor.Processor;
import java.util.LinkedList;
import java.util.List;

public class SequentialDFS {
    private final Graph taskgraph;
    private final Integer processorCount;
    private List<Processor> processors= new LinkedList<>();
    /**
     * Construtor method for sequentialDFS schedule generator
     * @param taskgraph directed acyclic graph
     * @param n number of processors
     */
    public SequentialDFS(Graph taskgraph,Integer n){
        this.taskgraph=taskgraph;
        this.processorCount=n;
    }

    public Schedule generateOptimalSchedule(){
        Schedule best=new Schedule(this.processorCount); //initial best schedule's finish time is an infinite value
        LinkedList<Schedule> scheduleStack=new LinkedList<>();
        scheduleStack.add(best);
        int bestFinishTime=Integer.MAX_VALUE;

        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.getLast(); //O(1)
            scheduleStack.removeLast(); //pop
                if (current.getNodesVisited() == this.taskgraph.getTotalTasksCount()) { //current schedule is a complete schedule
                    best = current;
                    bestFinishTime= current.getFinishTime();
                }
                else{ //explore deeper down the graph
                    Scheduler helper=new Scheduler(current);
                    List<Node> returningPoint= helper.getTasksCanBeScheduled(taskgraph.getNodes()); //branch
                    for (Node option:returningPoint) { //add all possible sub-schedules to stack
                        helper.scheduleTaskToProcessor(option,bestFinishTime,scheduleStack); //many schedules possible!, think about reducing duplicates here
                    }
                }
        }
        return best;
    }

}

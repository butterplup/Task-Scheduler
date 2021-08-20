package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;
import java.util.LinkedList;
import java.util.List;


public class PartialSchedule {
    @Getter private PartialSchedule prev;
    @Getter private TaskScheduled ts;
    private int[] processorInfo;
    private final int nodesVisited;
    @Getter int finishTime;

    // Global vars
    @Getter private static int processors;
    @Getter private static Graph schedulingGraph;

    public PartialSchedule(Graph g, int p) {
        this.nodesVisited=0;
        this.finishTime =0;

        // Fields that we can persist across schedules (improves perf.)
        processors=p;
        schedulingGraph=g;
    }

    /**
     * Build a new schedule based on a previous one and a TaskScheduled
     * @param ps Previous schedule
     * @param ts New TaskScheduled
     */
    public PartialSchedule(PartialSchedule ps,TaskScheduled ts){
        this.prev=ps;
        this.ts=ts;
        this.nodesVisited=ps.nodesVisited+1;
        this.finishTime =Math.max(ps.finishTime,ts.getFinishTime());
    }

    /**
     * Return all schedules that branch from this one
     * @param best Best complete schedule time thus far
     * @return List of schedules
     */
    public List<PartialSchedule> expandSchedule(int best){
        List<PartialSchedule> expanded=new LinkedList<>();

        // Find nodes that are already scheduled
        TaskScheduled[] alreadyScheduled=this.getScheduledTasks();

        // Node hasn't been Scheduled
        for (Node n : schedulingGraph.getNodes()){
            //cannot be scheduled if it has been scheduled already
            if (alreadyScheduled[n.getId()]!=null){
                continue;
            }

            // Check that all dependencies are scheduled
            boolean canBeScheduled = true;
            for (Edge in : n.getIncomingEdges()) {
                if (alreadyScheduled[in.getStart().getId()] == null) {
                    canBeScheduled = false;
                    break;
                }
            }

            if (!canBeScheduled){
                continue;
            }

            boolean foundEmpty = false;
            for (int p=0; p < processors; p++){
                int start=this.processorInfo[p];
                int startTime;
                // Get processor start time
                // Break if we encounter two empty processors
                boolean empty = start == 0;
                if (foundEmpty && empty){
                    break;
                }

                foundEmpty = empty || foundEmpty;

                int communicationCost = 0;
                List<Edge> in = n.getIncomingEdges();

                // Find the earliest starting time on this processor p
                for (Edge e : in) {
                    Node pre = e.getStart(); // Get parent node
                    TaskScheduled predecessor = alreadyScheduled[pre.getId()];

                    if (predecessor.getProcessor() != p) { //communication cost
                        communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + e.getWeight());
                    }
                }

                startTime = Math.max(start, communicationCost);
                TaskScheduled scheduled = new TaskScheduled(n, startTime, p);
                PartialSchedule possibility = new PartialSchedule(this, scheduled);

                //Only add to Schedule to stack if its finish time<current best "complete" schedule
                if (possibility.getFinishTime() < best) {
                    expanded.add(possibility);
                }
            }
        }

        return expanded;
    }

    /**
     * Get all scheduled tasks
     * @return Array of TaskScheduled, indexed by Node ID
     */
    public TaskScheduled[] getScheduledTasks(){
        this.processorInfo = new int[processors];
        TaskScheduled[] currentTasks = new TaskScheduled[schedulingGraph.getNodes().size()];

        // If this Schedule schedules a Node
        if (this.ts != null) {
            // Add the TaskScheduled to currentTasks
            currentTasks[this.ts.getTaskNode().getId()] = this.ts;

            // Update the relevant processor finish time if need be
            int start=ts.getStartingTime() + ts.getTaskNode().getWeight();
            if (this.processorInfo[ts.getProcessor()]<start) {
                this.processorInfo[ts.getProcessor()] = start;
            }
        }

        // Traverse the PartialSchedule hierarchy
        PartialSchedule current=this.prev;
        while (current != null && current.getTs() != null){
            TaskScheduled ts = current.getTs();
            // Add to currentTasks
            currentTasks[ts.getTaskNode().getId()] = ts;

            // Update the relevant processor finish time if need be
            int pStart = ts.getStartingTime() + ts.getTaskNode().getWeight();
            if (pStart > this.processorInfo[ts.getProcessor()]){
                this.processorInfo[ts.getProcessor()] = pStart;
            }

            // Ascend
            current = current.getPrev();
        }

        return currentTasks;
    }

    public int[] getProcessorInfo(){
        return this.processorInfo;
    }

    public boolean isCompleteSchedule(int total){
        return this.nodesVisited == total;
    }

    public void printSchedule(){
        TaskScheduled[] tasks=this.getScheduledTasks();
        for (TaskScheduled t:tasks){
            if (t!=null) {
                System.out.println(t.getTaskNode().getName() + "is scheduled to" + t.getProcessor() + "Starting at" + t.getStartingTime());
            }
        }
    }
}

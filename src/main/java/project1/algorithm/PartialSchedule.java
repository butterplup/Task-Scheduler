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
    @Getter private int processors;
    @Getter private Graph schedulingGraph;
    private int[] processorInfo;
    private int nodesVisited;
    @Getter int FinishTime;

    public PartialSchedule(Graph g, int p){
        this.processors=p;
        this.schedulingGraph=g;
        this.nodesVisited=0;
        this.FinishTime=0;
    }

    public PartialSchedule(PartialSchedule ps,TaskScheduled ts){
        this.processors=ps.getProcessors();
        this.schedulingGraph=ps.getSchedulingGraph();
        this.prev=ps;
        this.ts=ts;
        this.nodesVisited=ps.nodesVisited+1;
        this.FinishTime=Math.max(ps.FinishTime,ts.getFinishTime());
    }

    public List<PartialSchedule> expandSchedule(int best){
        //Find nodes can be scheduled
        List<Node> all=this.schedulingGraph.getNodes();
        TaskScheduled[] alreadyScheduled=this.getScheduledTasks();
        List<PartialSchedule> expanded=new LinkedList<>();
        //Node hasn't been Scheduled
        for (Node n:all){
            //cannot be scheduled if it has been scheduled already
            if (alreadyScheduled[n.getId()]!=null){
                continue;
            }
            boolean canBeScheduled=true;
            //Can not be scheduled if parents have not been scheduled
            for (Edge in:n.getIncomingEdges()){
                Node pre=in.getStart();
                if (alreadyScheduled[pre.getId()]==null){
                    canBeScheduled=false;
                    break;
                }
            }

            if (!canBeScheduled){
                continue;
            }
            boolean foundEmpty = false;
            for (int p=0;p<this.processors;p++){
                int start=this.processorInfo[p];
                int startTime;
                //Get processor start time
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

    public TaskScheduled[] getScheduledTasks(){
        this.processorInfo=new int[this.processors];
        TaskScheduled[] currentTasks=new TaskScheduled[this.schedulingGraph.getNodes().size()];
        if (this.ts!=null) {
            currentTasks[this.ts.getTaskNode().getId()] = this.ts;
            int start=ts.getStartingTime() + ts.getTaskNode().getWeight();
            if (this.processorInfo[ts.getProcessor()]<start) {
                this.processorInfo[ts.getProcessor()] = start;
            }
        }
        PartialSchedule parent=this.prev;
        while (parent!=null&&parent.getTs()!=null){
            currentTasks[parent.getTs().getTaskNode().getId()]=parent.getTs();
            int pstart=parent.getTs().getStartingTime()+parent.getTs().getTaskNode().getWeight();
            if (pstart> this.processorInfo[parent.getTs().getProcessor()]){
                this.processorInfo[parent.getTs().getProcessor()]=pstart;
            }
            parent=parent.getPrev();
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

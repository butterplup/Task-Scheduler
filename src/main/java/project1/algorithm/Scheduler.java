package project1.algorithm;

import project1.graph.Node;
import project1.processor.Processor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Scheduler {
    private List<Processor> processors;
    private Schedule current;

    /**
     * Constructor method for Scheduler
     *
     */
    public Scheduler(Schedule c) {
        this.processors=c.getProcessors();
        this.current=c;
    }

    /**
     * Generate all possible schedules when task t is scheduled
     * @param t task to be scheduled
     * @param best current best finish time of a complete schedule
     * @param scheduleStack All generated schedules will be added to the current schedule stack
     */
    public void scheduleTaskToProcessor(Node t,int best,LinkedList<Schedule> scheduleStack) { //current schedule+node t
        int index = 0;
        int emptyProcessorCount=0;
        //Loop through all processors to find all possible schedules, every schedule is a potential solution
        for (Processor p : this.processors) {
            if (emptyProcessorCount>0){ //This is another empty processor-> WILL produce duplicate as previous schedule
                continue;
            }
            if (p.getEmptyValue()){
                emptyProcessorCount++;
            }
            Schedule possibility=new Schedule(this.current); //deep copy
            int startTime;
            int communicationCost = 0;
            HashMap<Node, Integer> in = t.getIncomingEdges();
            //find the earliest starting time on this processor p
            for (HashMap.Entry<Node, Integer> i : in.entrySet()) {
                Node pre = i.getKey(); //get parent node
                HashMap<String, TaskScheduled> scheduled = this.current.getCurrentSchedule(); //get current schedule
                TaskScheduled predecessor = scheduled.get(pre.getName()); //lookup O(1)
                if (predecessor.getProcessor() != p.getProcessorId()) { //communication cost
                    communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + i.getValue());
                }
            }
            startTime = Math.max(p.getEarliestStartTime(), communicationCost);
            TaskScheduled scheduled = new TaskScheduled(t, startTime, this.processors.get(index).getProcessorId());
            possibility.addTask(t,scheduled);
            //Only add to Schedule to stack if its finish time<current best "complete" schedule
            if (possibility.getFinishTime()<best) {
                scheduleStack.add(possibility);
            }
            index++;
        }
    }

    public List<Node> getTasksCanBeScheduled(List<Node> taskList){
        List<Node> candidates=new LinkedList<>();
        for (Node s:taskList) {
            if (checkTaskCanBeScheduled(s)) {
                candidates.add(s);
            }
        }
        return candidates;
    }

    private boolean checkTaskCanBeScheduled(Node t){
        //A node that has no yet been scheduled
        if (checkTaskIsScheduled(t)){
            return false;
        }
        // THIS NODE'S PREDECESSORS MUST HAVE ALREADY BEEN SCHEDULED !!
        HashMap<Node,Integer> incomingEdges=t.getIncomingEdges();
        for (HashMap.Entry<Node, Integer> parent : incomingEdges.entrySet()) {
            if (!checkTaskIsScheduled(parent.getKey())){
                return false;
            }
        }
        return true;
    }

    private boolean checkTaskIsScheduled(Node t){
        return current.getCurrentSchedule().get(t.getName()) != null;
    }
}

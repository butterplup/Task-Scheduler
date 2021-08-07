package project1.algorithm;

import project1.graph.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * A scheduler for each sub-schedule is responsible for scheduling more
 * task nodes to the current sub-schedule.
 */
public class Scheduler {
    private Schedule current;

    /**
     * Constructor method for Scheduler
     *
     */
    public Scheduler(Schedule c) {
        this.current=c;
    }

    /**
     * Generate all possible schedules when task t is scheduled
     * @param t task to be scheduled
     * @param best current best finish time of a complete schedule
     * @param scheduleStack All generated schedules will be added to the current schedule stack
     */
    public void scheduleTaskToProcessor(Node t,int best, Deque<Schedule> scheduleStack) { //current schedule+node t
        boolean foundEmpty = false;

        // Loop through all processors to find all possible schedules, every schedule is a potential solution
        int index = -1;
        for (int free : this.current.getFreeTime()) {
            index++;

            // Break if we encounter two empty processors
            boolean empty = free == 0;
            if (foundEmpty && empty){
                break;
            }

            foundEmpty = empty || foundEmpty;

            Schedule possibility=new Schedule(this.current); //deep copy
            int startTime;
            int communicationCost = 0;
            HashMap<Node, Integer> in = t.getIncomingEdges();

            // Find the earliest starting time on this processor p
            for (HashMap.Entry<Node, Integer> i : in.entrySet()) {
                Node pre = i.getKey(); //get parent node
                HashMap<String, TaskScheduled> scheduled = this.current.getCurrentSchedule(); //get current schedule
                TaskScheduled predecessor = scheduled.get(pre.getName()); //lookup O(1)
                if (predecessor.getProcessor() != index) { //communication cost
                    communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + i.getValue());
                }
            }
            startTime = Math.max(free, communicationCost);
            TaskScheduled scheduled = new TaskScheduled(t, startTime, index);
            possibility.addTask(scheduled);
            //Only add to Schedule to stack if its finish time<current best "complete" schedule
            if (possibility.getFinishTime() < best) {
                scheduleStack.push(possibility);
            }
        }
    }

    public Stream<Node> getTasksCanBeScheduled(List<Node> taskList){
        return taskList.stream().filter(this::checkTaskCanBeScheduled);
    }

    /**
     * Checks if a task satisfies the scheduling constraints and is ready to be scheduled.
     * @param t The task node to check if constraints are satisfied.
     * @return Returns true if the constraints are satisfied, false otherwise
     */
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

    /**
     * Checks if a task has already been scheduled to the current schedule.
     * @param t The task node to check
     * @return Returns true if this task hasn't been scheduled yet, false otherwise.
     */
    private boolean checkTaskIsScheduled(Node t){
        return current.getCurrentSchedule().get(t.getName()) != null;
    }
}

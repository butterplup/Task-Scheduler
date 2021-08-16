package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.util.Deque;
import java.util.List;

/**
 * A scheduler for each sub-schedule is responsible for scheduling more
 * task nodes to the current sub-schedule.
 */
public class Scheduler extends Thread {
    private final Schedule current;

    /**
     * Constructor method for scheduler, creates a scheduler for the Schedule object passed in.
     * @param c A Schedule object to be added with more tasks.
     * @param taskGraph The Graph object representing the task graph to generate schedules for.
     */
    public Scheduler(Schedule c) {
        this.current = c;
    }

    /**
     * Generate all possible schedules when task t is scheduled.
     * @param t task to be scheduled.
     * @param best current best finish time of a complete schedule.
     * @param scheduleStack All generated schedules will be added to the current schedule stack.
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
            List<Edge> in = t.getIncomingEdges();

            // Find the earliest starting time on this processor p
            for (Edge e : in) {
                Node pre = e.getStart(); // Get parent node

                TaskScheduled[] scheduled = this.current.getCurrentSchedule();
                TaskScheduled predecessor = scheduled[pre.getId()];

                if (predecessor.getProcessor() != index) { //communication cost
                    communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + e.getWeight());
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

    /**
     *
     */
    public List<Node> getTasksCanBeScheduled() {
        return current.getSchedulable();
    }
}

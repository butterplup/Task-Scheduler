
package project1.algorithm;

import lombok.Getter;
import project1.graph.Node;
import project1.processor.Processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A Schedule object holds information for tasks in the current Schedule and the processors used.
 */
@Getter
public class Schedule {
    private int finishTime;
    private HashMap<String, TaskScheduled> currentSchedule = new HashMap<>();
    private List<Processor> processors = new ArrayList<>();
    private int nodesVisited;

    public Schedule(int n) {
        this.finishTime = 0;
        for (int i = 0; i < n; i++) {
            this.processors.add(new Processor(i + 1));
        }
        this.nodesVisited = 0;
    }

    public Schedule(Schedule s) { //deep copy
        this.finishTime = s.getFinishTime();
        for (HashMap.Entry<String, TaskScheduled> i : s.getCurrentSchedule().entrySet()) {
            this.currentSchedule.put(i.getKey(), i.getValue());
        }
        for (Processor p : s.getProcessors()) {
            this.processors.add(new Processor(p));
        }
        this.nodesVisited = s.getNodesVisited();

    }

    /**
     * Adds a scheduled task to the current schedule and updates the processor information
     * @param s A newly scheduled task object.
     */
    public void addTask(TaskScheduled s) {
        this.currentSchedule.put(s.getTaskNode().getName(), s);
        //Change the assigned processor's earliest start time
        this.processors.get(s.getProcessor() - 1).setEarliestStartTime(s.getFinishTime());
        if (s.getFinishTime() > this.finishTime) {
            this.finishTime = s.getFinishTime(); //schedule's finish time
        }
        this.nodesVisited++;
    }

    public void printSchedule() {
        for (HashMap.Entry<String, TaskScheduled> i : this.currentSchedule.entrySet()) {
            System.out.println(i.getKey() + " is scheduled to Processor " + i.getValue().getProcessor() + "," +
                    "Starting time: " + i.getValue().getStartingTime() + "Finishing time: " + i.getValue().getFinishTime());
        }
    }
}



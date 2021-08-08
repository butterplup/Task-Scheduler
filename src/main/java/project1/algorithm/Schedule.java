
package project1.algorithm;

import lombok.Getter;

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
    private List<Integer> freeTime = new ArrayList<>();
    private int nodesVisited;

    /**
     * A constructor method which initialises an empty schedule.
     * @param n Number of processors available to the schedule
     */
    public Schedule(int n) {
        try {
            if (n < 1) {
                throw new IllegalArgumentException("Can not generate a schedule when the number of processors is less than 1.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        this.finishTime = 0;
        for (int i = 0; i < n; i++) {
            this.freeTime.add(0);
        }
        this.nodesVisited = 0;
    }

    /**
     * Creates a deep copy of a sub-schedule
     * @param s A sub-schedule to be copied
     */
    public Schedule(Schedule s) { //deep copy
        this.finishTime = s.getFinishTime();
        for (HashMap.Entry<String, TaskScheduled> i : s.getCurrentSchedule().entrySet()) {
            this.currentSchedule.put(i.getKey(), i.getValue());
        }

        this.freeTime.addAll(s.getFreeTime());
        this.nodesVisited = s.getNodesVisited();
    }

    /**
     * Adds a scheduled task to the current schedule and updates the processor information
     * @param s A newly scheduled task object.
     */
    public void addTask(TaskScheduled s) {
        this.currentSchedule.put(s.getTaskNode().getName(), s);
        //Change the assigned processor's earliest start time
        this.freeTime.set(s.getProcessor(), s.getFinishTime());
        if (s.getFinishTime() > this.finishTime) {
            this.finishTime = s.getFinishTime(); //schedule's finish time
        }
        this.nodesVisited++;
    }

    /**
     * Prints a schedule to console.
     */
    public void printSchedule() {
        for (HashMap.Entry<String, TaskScheduled> i : this.currentSchedule.entrySet()) {
            System.out.println(i.getKey() + " is scheduled to Processor " + i.getValue().getProcessor() + "," +
                    "Starting time: " + i.getValue().getStartingTime() + "Finishing time: " + i.getValue().getFinishTime());
        }
    }
}

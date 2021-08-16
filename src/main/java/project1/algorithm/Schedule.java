
package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Node;

import java.util.*;
import java.util.stream.Stream;

/**
 * A Schedule object holds information for tasks in the current Schedule and the processors used.
 */
@Getter
public class Schedule {
    private int finishTime;
    // List of TaskScheduled, indexed by node id
    private final List<TaskScheduled> currentSchedule;
    private final List<Node> schedulable;
    private final List<Integer> freeTime;
    private int nodesVisited;

    /**
     * A constructor method which initialises an empty schedule.
     * @param n Number of processors available to the schedule
     */
    public Schedule(int n, List<Node> nodes) {
        try {
            if (n < 1) {
                throw new IllegalArgumentException("Can not generate a schedule when the number of processors is less than 1.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        this.finishTime = 0;
        this.currentSchedule = new ArrayList<>(Collections.nCopies(nodes.size(), null));
        this.schedulable = new ArrayList<>();
        nodes.stream().filter(node -> node.getIncomingEdges().size() == 0).forEach(this.schedulable::add);

        this.freeTime = new ArrayList<>(Collections.nCopies(n, 0));
        this.nodesVisited = 0;
    }

    /**
     * Creates a deep copy of a sub-schedule
     * @param s A sub-schedule to be copied
     */
    public Schedule(Schedule s) { //deep copy
        this.finishTime = s.getFinishTime();
        this.nodesVisited = s.getNodesVisited();
        this.currentSchedule = new ArrayList<>(s.currentSchedule);
        this.schedulable = new ArrayList<>(s.schedulable);
        this.freeTime = new ArrayList<>(s.getFreeTime());
    }

    /**
     * Adds a scheduled task to the current schedule and updates the processor information
     * @param s A newly scheduled task object.
     */
    public void addTask(TaskScheduled s) {
        this.currentSchedule.set(s.getTaskNode().getId(), s);
        // Cannot reschedule
        this.schedulable.remove(s.getTaskNode());

        // Change the assigned processor's earliest start time
        this.freeTime.set(s.getProcessor(), s.getFinishTime());
        if (s.getFinishTime() > this.finishTime) {
            this.finishTime = s.getFinishTime(); //schedule's finish time
        }

        // Check if nodes out from this can be scheduled
        for (Edge e : s.getTaskNode().getOutgoingEdges()) {
            Node n = e.getEnd();

            if (currentSchedule.get(n.getId()) == null && !this.schedulable.contains(n)) {
                // If the node was previously inviable, check if it is now
                boolean canSchedule = n.getIncomingEdges().stream().allMatch(
                        inEdge -> this.currentSchedule.get(inEdge.getStart().getId()) != null
                );

                if (canSchedule) {
                    schedulable.add(n);
                }
            }
        }

        this.nodesVisited++;
    }

    /**
     * Prints a schedule to console.
     */
    public void printSchedule() {
        /*
        for (HashMap.Entry<String, TaskScheduled> i : this.currentSchedule.entrySet()) {
            System.out.println(i.getKey() + " is scheduled to Processor " + i.getValue().getProcessor() + "," +
                    "Starting time: " + i.getValue().getStartingTime() + "Finishing time: " + i.getValue().getFinishTime());
        }*/
    }
}


package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Node;

import java.util.*;

/**
 * A Schedule object holds information for tasks in the current Schedule and the processors used.
 */
@Getter
public class Schedule {
    private int finishTime;
    // List of TaskScheduled, indexed by node id
    private final TaskScheduled[] currentSchedule;
    private final int[] freeTime;
    private final List<Node> schedulable;
    private int nodesVisited;

    /**
     * A constructor method which initialises an empty schedule.
     * @param n Number of processors available to the schedule
     * @param nodes List of nodes to add to the schedule
     */
    public Schedule(int n, List<Node> nodes) {
        try {
            if (n < 1) {
                throw new IllegalArgumentException("");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can not generate a schedule when the number of processors is less than 1.");
        }

        this.finishTime = 0;
        this.nodesVisited = 0;

        this.currentSchedule = new TaskScheduled[nodes.size()];
        // We can schedule any node with no incoming edges
        this.schedulable = new LinkedList<>();
        nodes.stream().filter(node -> node.getIncomingEdges().size() == 0).forEach(this.schedulable::add);
        this.freeTime = new int[n];
    }

    /**
     * Creates a deep copy of a sub-schedule and adds a TaskScheduled
     * @param s A sub-schedule to be copied
     */
    public Schedule(Schedule s, TaskScheduled ts) {
        this.finishTime = s.getFinishTime();
        this.nodesVisited = s.getNodesVisited();

        // Shallow copy arrays
        this.currentSchedule = s.currentSchedule.clone();
        this.freeTime = s.freeTime.clone();
        this.schedulable = new LinkedList<>(s.schedulable);

        this.currentSchedule[ts.getTaskNode().getId()] = ts;
        // Cannot reschedule
        this.schedulable.remove(ts.getTaskNode());

        // Change the assigned processor's earliest start time
        this.freeTime[ts.getProcessor()] = ts.getFinishTime();
        if (ts.getFinishTime() > this.finishTime) {
            this.finishTime = ts.getFinishTime(); //schedule's finish time
        }

        // Check if nodes out from this can be scheduled
        for (Edge e : ts.getTaskNode().getOutgoingEdges()) {
            Node n = e.getEnd();

            if (currentSchedule[n.getId()] == null && !this.schedulable.contains(n)) {
                // If the node was previously inviable, check if it is now
                boolean canSchedule = n.getIncomingEdges().stream().allMatch(
                        inEdge -> this.currentSchedule[inEdge.getStart().getId()] != null
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

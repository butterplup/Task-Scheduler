package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the current schedule with scheduled nodes and a reference
 * to its parent schedule
 */
@Getter
public class PartialSchedule {
    // Global vars
    @Getter private static int processors;
    @Getter private static Graph schedulingGraph;
    private final int nodesVisited;
    int finishTime;
    private PartialSchedule prev;
    private TaskScheduled ts;
    private int[] processorInfo;

    /**
     * Creates an initial empty PartialSchedule object.
     *
     * @param g The task graph that we create partial schedule for.
     * @param p Number of processors.
     */
    public PartialSchedule(Graph g, int p) {
        try {
            if (p < 1) {
                throw new IllegalArgumentException("Can not generate a schedule when the number of processors is less than 1.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        this.nodesVisited = 0;
        this.finishTime = 0;

        // Fields that we can persist across schedules (improves perf.)
        processors = p;
        schedulingGraph = g;
    }

    /**
     * Build a new schedule based on a previous one and a TaskScheduled
     *
     * @param ps Previous schedule
     * @param ts New TaskScheduled
     */
    public PartialSchedule(PartialSchedule ps, TaskScheduled ts) {
        this.prev = ps;
        this.ts = ts;
        this.nodesVisited = ps.nodesVisited + 1;
        this.finishTime = Math.max(ps.finishTime, ts.getFinishTime());
    }

    /**
     * Return all schedules that branch from this one
     *
     * @param best Best complete schedule time thus far
     * @return List of schedules
     */
    public List<PartialSchedule> expandSchedule(int best) {
        List<PartialSchedule> expanded = new LinkedList<>();

        // Find nodes that are already scheduled
        TaskScheduled[] alreadyScheduled = this.getScheduledTasks();

        // Node hasn't been Scheduled
        for (Node n : schedulingGraph.getNodes()) {
            //cannot be scheduled if it has been scheduled already
            if (alreadyScheduled[n.getId()] != null) {
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

            if (!canBeScheduled) {
                continue;
            }

            boolean foundEmpty = false;
            for (int p = 0; p < processors; p++) {
                int start = this.processorInfo[p];
                int startTime;
                // Get processor start time
                // Break if we encounter two empty processors
                boolean empty = start == 0;
                if (foundEmpty && empty) {
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
                if (possibility.getFinishTime() + scheduled.getTaskNode().getCriticalPath() < best) {
                    expanded.add(possibility);
                }
            }
        }

        return expanded;
    }

    /**
     * Get all scheduled tasks
     *
     * @return Array of TaskScheduled, indexed by Node ID
     */
    public TaskScheduled[] getScheduledTasks() {
        this.processorInfo = new int[processors];
        TaskScheduled[] currentTasks = new TaskScheduled[schedulingGraph.getNodes().size()];

        // If this Schedule schedules a Node
        if (this.ts != null) {
            // Add the TaskScheduled to currentTasks
            currentTasks[this.ts.getTaskNode().getId()] = this.ts;

            // Update the relevant processor finish time if need be
            int start = ts.getStartingTime() + ts.getTaskNode().getWeight();
            if (this.processorInfo[ts.getProcessor()] < start) {
                this.processorInfo[ts.getProcessor()] = start;
            }
        }

        // Traverse the PartialSchedule hierarchy
        PartialSchedule current = this.prev;
        while (current != null && current.getTs() != null) {
            TaskScheduled ts = current.getTs();
            // Add to currentTasks
            currentTasks[ts.getTaskNode().getId()] = ts;

            // Update the relevant processor finish time if need be
            int pStart = ts.getStartingTime() + ts.getTaskNode().getWeight();
            if (pStart > this.processorInfo[ts.getProcessor()]) {
                this.processorInfo[ts.getProcessor()] = pStart;
            }

            // Ascend
            current = current.getPrev();
        }

        return currentTasks;
    }

    /**
     * Checks if the partial schedule is a complete schedule
     *
     * @param total Expected number of nodes to be visited for a complete schedule
     * @return true if the number of nodes visited=total
     * false if the number of nodes visited does not equal to total
     */
    public boolean isCompleteSchedule(int total) {
        return this.nodesVisited == total;
    }

    /**
     * Prints out the current schedule
     */
    public String printSchedule() {
        TaskScheduled[] tasks = this.getScheduledTasks();
        StringBuilder s=new StringBuilder();
        for (TaskScheduled t : tasks) {
            if (t != null) {
                s.append(t.getTaskNode().getName() + " is scheduled to Processor " + t.getProcessor() +
                        " Starting at time " + t.getStartingTime()+"\n");
            }
        }
        //System.out.println(s.toString());
        return s.toString();
    }
}

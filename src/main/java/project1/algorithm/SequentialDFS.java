package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * sequentialDFS Algorithm
 */
public class SequentialDFS {
    /**
     * Generate an optimal schedule
     * @param taskGraph Task graph, given as a DAG
     * @param processorCount The number of processors to schedule for
     *
     * @return The optimal Schedule
     */
    public static Schedule generateOptimalSchedule(Graph taskGraph, int processorCount){
        // Current best schedule and its finish time
        Schedule best = new Schedule(processorCount);
        int bestFinishTime = Integer.MAX_VALUE;

        // Stack of schedules to be evaluated
        Deque<Schedule> scheduleStack = new LinkedList<>();
        scheduleStack.push(best);

        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.pop();

            // If this schedule includes all nodes in the taskGraph
            if (current.getNodesVisited() == taskGraph.getTotalTasksCount()) {
                best = current;
                bestFinishTime = current.getFinishTime();
            } else {
                // Otherwise, explore branches
                Scheduler scheduler = new Scheduler(current);

                // Get a list of tasks that can be scheduled next
                List<Node> branches = scheduler.getTasksCanBeScheduled(taskGraph.getNodes());

                for (Node branch : branches) {
                    // Add possible schedules to the schedule stack
                    scheduler.scheduleTaskToProcessor(branch, bestFinishTime, scheduleStack);
                }
            }
        }

        return best;
    }
}

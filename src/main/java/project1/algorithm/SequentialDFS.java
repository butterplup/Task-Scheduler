package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A sequential depth-first branch and bound search algorithm that finds an
 * optimal schedule for assigning tasks from a DAG task graph onto a set number
 * of processors.
 *
 * An optimal schedule allocates all tasks to processors in a way that gives a
 * minimum required time to complete all tasks.
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
                Stream<Node> branches = scheduler.getTasksCanBeScheduled(taskGraph.getNodes());

                // For each branch, add possible schedules to the stack
                int finalBestFinishTime = bestFinishTime;
                branches.forEach(branch ->
                        scheduler.scheduleTaskToProcessor(branch, finalBestFinishTime, scheduleStack)
                );
            }
        }

        // Annotate nodes in the task graph with the processor its scheduled on
        for (Map.Entry<String, TaskScheduled> i : best.getCurrentSchedule().entrySet()) {
            String taskName = i.getKey();
            int processor = i.getValue().getProcessor();
            int startTime = i.getValue().getStartingTime();

            Node n = taskGraph.getNodeMap().get(taskName);
            n.setProcessor(processor);
            n.setStart(startTime);
        }

        return best;
    }
}

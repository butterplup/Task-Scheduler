package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.*;
import java.util.stream.Collectors;
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
     * @param threads Number of parallel threads to be run
     *
     * @return The optimal Schedule
     */
    public static PartialSchedule generateOptimalSchedule(Graph taskGraph, int processorCount, int threads) {
        // Stack of schedules to be evaluated
        System.out.println("Setting up threads...");

        ThreadAnalytics ta = ThreadAnalytics.getInstance();

        // Get solution upper bound given a simple list scheduler
        PartialSchedule ubound = TopologicalSort.getSchedule(taskGraph,processorCount);

        // Hand it to ThreadAnalytics
        ta.newSchedule(ubound.getFinishTime(), ubound);
        System.out.printf("Solution Upper Bound: %d%n", ubound.getFinishTime());

        // Get initial PartialSchedules, initialising static vars in the PartialSchedule class
        List<PartialSchedule> initBranches = new PartialSchedule(taskGraph, processorCount).expandSchedule(ubound.getFinishTime());
        if (initBranches == null){
            throw new RuntimeException("Can't find any initial schedules for the task graph!");
        }

        // Create the number of DFSThreads specified by the user
        DFSThread[] threadPool = new DFSThread[threads];
        for (int i = 0; i < threads; i++) {
            threadPool[i] = new DFSThread(taskGraph);
        }

        // Circle the thread pool, pushing schedules until we run out
        int i = 0;
        for (PartialSchedule s : initBranches) {
            threadPool[i].scheduleStack.push(s);
            i += 1;
            i %= threads;
        }

        // Spawn threads with non-empty stacks
        for (DFSThread d : threadPool) {
            if (d.scheduleStack.size() > 0) {
                ta.addThread(d);
            }
        }

        try {
            System.out.println("Processing...");
            ta.waitTillDone();
            System.out.println("Done!");
        } catch (InterruptedException e) {
            throw new RuntimeException("Threads interrupted!");
        }

        PartialSchedule best = ta.getBestSchedule();

        if (best == null || best.getFinishTime() == 0) {
            throw new RuntimeException("No schedules generated!");
        }

        System.out.printf("Thread starts: %d%n", ta.numThreadsSpawned());

        // Annotate nodes in the task graph with the processor it's scheduled on
        for (TaskScheduled t : best.getScheduledTasks()) {
            String taskName = t.getTaskNode().getName();
            int processor = t.getProcessor();
            int startTime = t.getStartingTime();

            Node n = taskGraph.getNodeMap().get(taskName);
            n.setProcessor(processor);
            n.setStart(startTime);
        }

        return best;
    }
}

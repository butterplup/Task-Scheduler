package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
    public static Schedule generateOptimalSchedule(Graph taskGraph, int processorCount, int threads) {
        // Stack of schedules to be evaluated
        System.out.println("Start");
        // Empty schedule
        Scheduler s = new Scheduler(new Schedule(processorCount, taskGraph.getTotalTasksCount()), taskGraph);

        ThreadAnalytics ta = ThreadAnalytics.getInstance(threads);

        s.getTasksCanBeScheduled(taskGraph.getNodes())
                .forEach(
                    n -> {
                        Schedule schedule = new Schedule(processorCount, taskGraph.getTotalTasksCount());
                        schedule.addTask(new TaskScheduled(n, 0, 0));
                        ta.addThread(new Scheduler(schedule, taskGraph));
                    }
        );

        try {
            System.out.println("Waiting...");
            ta.waitTillDone();
            System.out.println("Done!");
        } catch (InterruptedException e) {
            throw new RuntimeException("Threads interrupted!");
        }

        Schedule best = ta.getBestSchedule();

        if (best == null) {
            throw new RuntimeException("No schedules generated!");
        }
        // Inform ThreadAnalytics algorithm is finished with a schedule found to update UI.
        ta.setFinished(true);

        System.out.printf("Thread starts: %d%n", ta.numThreadsSpawned());

        // Annotate nodes in the task graph with the processor its scheduled on
        for (TaskScheduled i : best.getCurrentSchedule()) {
            String taskName = i.getTaskNode().getName();
            int processor = i.getProcessor();
            int startTime = i.getStartingTime();

            Node n = taskGraph.getNodeMap().get(taskName);
            n.setProcessor(processor);
            n.setStart(startTime);
        }

        return best;
    }
}

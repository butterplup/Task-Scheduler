package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
    public static Schedule generateOptimalSchedule(Graph taskGraph, int processorCount, int threads) {
        // Stack of schedules to be evaluated
        System.out.println("Start");
        ThreadAnalytics ta = ThreadAnalytics.getInstance(threads);

        List<Node> initNodes = new Schedule(processorCount, taskGraph.getNodes()).getSchedulable();

        // Empty schedule
        Schedule schedule = new Schedule(processorCount, taskGraph.getNodes());
        Stream<Schedule> initSchedules = initNodes.stream().map(n ->
                new Schedule(schedule, new TaskScheduled(n, 0, 0))
        );

        DFSThread[] threadPool = new DFSThread[threads];
        for (int i = 0; i < threads; i++) {
            threadPool[i] = new DFSThread(taskGraph);
        }

        // Circle the thread pool until we're out of schedules
        int i = 0;
        for (Schedule s : initSchedules.collect(Collectors.toCollection(LinkedList::new))) {
            threadPool[i].scheduleStack.push(s);
            i += 1;
            i %= threads;
        }

        for (DFSThread d : threadPool) {
            // Only spawn threads with a non-empty stack
            if (d.scheduleStack.size() > 0) {
                ta.addThread(d);
            }
        }

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

        System.out.printf("Thread starts: %d%n", ta.numThreadsSpawned());

        // Annotate nodes in the task graph with the processor its scheduled on
        for (TaskScheduled t : best.getCurrentSchedule()) {
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

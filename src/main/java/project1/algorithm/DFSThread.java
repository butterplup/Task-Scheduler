package project1.algorithm;

import project1.graph.Graph;
import project1.graph.Node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class DFSThread extends Thread {
    private final Graph taskGraph;
    public Deque<Schedule> scheduleStack;

    public DFSThread(Graph taskGraph) {
        this.taskGraph = taskGraph;
        scheduleStack = new LinkedList<>();
    }

    /**
     * Given a node in the task graph, perform DFS on it.
     */
    public void run() {
        ThreadAnalytics ta = ThreadAnalytics.getInstance();
        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.pop();

            // If this schedule includes all nodes in the taskGraph
            if (current.getNodesVisited() == taskGraph.getTotalTasksCount()) {
                // Send this to ThreadAnalytics
                ta.newSchedule(current.getFinishTime(), current);
            } else {
                // Get a list of tasks that can be scheduled next
                List<Node> branches = current.getSchedulable();

                // For each branch, add possible schedules to the stack, using global best time
                branches.forEach(branch ->
                        Scheduler.scheduleTaskToProcessor(current, branch, ta.getGlobalBestTime(), scheduleStack)
                );
            }

            synchronized (ta) {
                if (ta.threadNeeded() && scheduleStack.size() > 1) {
                    // Make a new thread from that schedule
                    DFSThread newThread = new DFSThread(taskGraph);

                    // Give an element from the back of the stack to the new thread
                    newThread.scheduleStack.push(scheduleStack.removeLast());

                    // Start the new thread
                    ta.addThread(newThread);
                }
            }
        }
        // Thread has finished, decrease it from count of live threads
        ta.decThreadsAlive();
    }
}

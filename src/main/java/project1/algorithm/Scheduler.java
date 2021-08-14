package project1.algorithm;

import lombok.Getter;
import project1.graph.Edge;
import project1.graph.Graph;
import project1.graph.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A scheduler for each sub-schedule is responsible for scheduling more
 * task nodes to the current sub-schedule.
 */
public class Scheduler extends Thread {
    private final Schedule current;
    @Getter private Schedule output;
    private final Graph taskGraph;

    /**
     * Constructor method for scheduler, creates a scheduler for the Schedule object passed in.
     * @param c A Schedule object to be added with more tasks.
     */
    public Scheduler(Schedule c, Graph taskGraph) {
        this.current = c;
        this.taskGraph = taskGraph;
    }

    /**
     * Given a node in the task graph, perform dfs
     */
    public void run() {
        ThreadAnalytics ta = ThreadAnalytics.getInstance();

        Deque<Schedule> scheduleStack = new LinkedList<>();
        scheduleStack.push(this.current);
        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.pop();

            // If this schedule includes all nodes in the taskGraph
            if (current.getNodesVisited() == taskGraph.getTotalTasksCount()) {
                // Send this to ThreadAnalytics
                ta.newSchedule(current.getFinishTime(), current);
            } else {
                // Otherwise, explore branches
                Scheduler scheduler = new Scheduler(current, taskGraph);

                // Get a list of tasks that can be scheduled next
                Stream<Node> branches = scheduler.getTasksCanBeScheduled(taskGraph.getNodes());

                // For each branch, add possible schedules to the stack, using global best time
                branches.forEach(branch ->
                        scheduler.scheduleTaskToProcessor(branch, ta.getGlobalBestTime(), scheduleStack)
                );
            }

            synchronized (ta) {
                if (ta.threadNeeded() && scheduleStack.size() > 1) {
                    System.out.printf("Thread Split (%d/%d)%n", ta.numThreadsAlive(), ta.getThreadsNeeded());

                    // Take the element from the end
                    Schedule split = scheduleStack.getLast();
                    scheduleStack.removeLast();

                    // Make a new thread from that schedule
                    ta.addThread(new Scheduler(split, this.taskGraph));
                }
            }
        }

        ta.decThreadsAlive();
    }

    /**
     * Generate all possible schedules when task t is scheduled
     * @param t task to be scheduled
     * @param best current best finish time of a complete schedule
     * @param scheduleStack All generated schedules will be added to the current schedule stack
     */
    public void scheduleTaskToProcessor(Node t,int best, Deque<Schedule> scheduleStack) { //current schedule+node t
        boolean foundEmpty = false;

        // Loop through all processors to find all possible schedules, every schedule is a potential solution
        int index = -1;
        for (int free : this.current.getFreeTime()) {
            index++;

            // Break if we encounter two empty processors
            boolean empty = free == 0;
            if (foundEmpty && empty){
                break;
            }

            foundEmpty = empty || foundEmpty;

            Schedule possibility=new Schedule(this.current); //deep copy
            int startTime;
            int communicationCost = 0;
            List<Edge> in = t.getIncomingEdges();

            // Find the earliest starting time on this processor p
            for (Edge e : in) {
                Node pre = e.getStart(); // Get parent node

                List<TaskScheduled> scheduled = this.current.getCurrentSchedule();
                TaskScheduled predecessor = scheduled.get(pre.getId());

                if (predecessor.getProcessor() != index) { //communication cost
                    communicationCost = Math.max(communicationCost, predecessor.getFinishTime() + e.getWeight());
                }
            }

            startTime = Math.max(free, communicationCost);
            TaskScheduled scheduled = new TaskScheduled(t, startTime, index);
            possibility.addTask(scheduled);

            //Only add to Schedule to stack if its finish time<current best "complete" schedule
            if (possibility.getFinishTime() < best) {
                scheduleStack.push(possibility);
            }
        }
    }

    /**
     * This method finds the tasks which satisfies the scheduling constraints and is ready
     * to be scheduled to a processor.
     * @param taskList A list of tasks to be checked if they are ready to be scheduled.
     * @return A Stream of tasks which satisfies the scheduling constraints and can be scheduled.
     */
    public Stream<Node> getTasksCanBeScheduled(List<Node> taskList){
        return taskList.stream().filter(this::checkTaskCanBeScheduled);
    }

    /**
     * Checks if a task satisfies the scheduling constraints and is ready to be scheduled.
     * @param t The task node to check if constraints are satisfied.
     * @return Returns true if the constraints are satisfied, false otherwise
     */
    private boolean checkTaskCanBeScheduled(Node t){
        // Cannot reschedule
        if (checkTaskIsScheduled(t)){
            return false;
        }

        // THIS NODE'S PREDECESSORS MUST HAVE ALREADY BEEN SCHEDULED !!
        List<Edge> incomingEdges = t.getIncomingEdges();
        return incomingEdges.stream().allMatch(e -> checkTaskIsScheduled(e.getStart()));
    }

    /**
     * Checks if a task has already been scheduled to the current schedule.
     * @param t The task node to check
     * @return Returns true if this task hasn't been scheduled yet, false otherwise.
     */
    private boolean checkTaskIsScheduled(Node t){
        return current.getCurrentSchedule().get(t.getId()) != null;
    }
}

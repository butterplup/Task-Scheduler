package project1.algorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project1.graph.Node;

/**
 * This class represents a node/task after it has been scheduled to
 * a processor.
 */
@Getter
@AllArgsConstructor
public class TaskScheduled {
    // Task node that appears in a schedule/sub-schedule.
    final Node taskNode;
    // The time when the task node will start its job.
    final int startingTime;
    // The processor which the task node is assigned to.
    final int processor;

    /**
     * Computes and returns the finishing time of this scheduled task object.
     * @return The finishing time of this already scheduled task node.
     */
    public int getFinishTime(){
        return this.startingTime + taskNode.getWeight();
    }
}

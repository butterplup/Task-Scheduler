package project1.algorithm;

import lombok.Getter;
import project1.graph.Node;

/**
 * This class represents a node/task after it has been scheduled to
 * a processor.
 */
@Getter
public class TaskScheduled {
    Node taskNode;
    int startingTime;
    int processor;

    /**
     * A constructor method that maintains information about a scheduled task for a schedule.
     * @param task task node that appears in a schedule/sub-schedule.
     * @param start the time when the task node will start its job
     * @param processor the processor which the task node is assigned to.
     */
    public TaskScheduled(Node task,int start,int processor){
        this.taskNode=task;
        this.startingTime=start;
        this.processor=processor;
    }

    /**
     * This method computes and returns the finishing time of this scheduled task object.
     * @return The finishing time of this already scheduled task node.
     */
    public int getFinishTime(){
        return this.startingTime+ taskNode.getWeight();
    }

}

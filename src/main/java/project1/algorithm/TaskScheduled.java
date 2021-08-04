package project1.algorithm;

import lombok.Getter;
import project1.graph.Node;

@Getter
public class TaskScheduled {
    Node taskNode;
    int startingTime;
    int processor;

    public TaskScheduled(Node task,int start,int processor){
        this.taskNode=task;
        this.startingTime=start;
        this.processor=processor;
    }

    public int getFinishTime(){
        return this.startingTime+ taskNode.getWeight();
    }

}

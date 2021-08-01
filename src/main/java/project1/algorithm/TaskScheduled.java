package project1.algorithm;

import javafx.concurrent.Task;
import project1.graph.Node;

public class TaskScheduled {
    Node taskNode;
    int startingTime;
    int processor;

    public TaskScheduled(Node task,int start,int processor){
        this.taskNode=task;
        this.startingTime=start;
        this.processor=processor;
    }

    public Node getTaskNode(){
        return this.taskNode;
    }

    public int getProcessor(){
        return this.processor;
    }

    public int getFinishTime(){
        return this.startingTime+ taskNode.getWeight();
    }

    public int getStartingTime() { return this.startingTime; }
}

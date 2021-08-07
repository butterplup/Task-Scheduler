package project1.processor;

import lombok.Getter;

/**
 * This class represents the processors for a schedule, each processor has
 * its id as the processor number, an earliest starting time for the next task
 * to be assigned on this processor, and a boolean variable to indicate if the
 * processor is empty(has no tasks assigned to it).
 */
@Getter
public class Processor {
    private int processorId;
    private int earliestStartTime;
    private boolean empty;

    public Processor(int id){
        this.processorId=id;
        this.earliestStartTime=0;
        this.empty=true;
    }

    /**
     * Constructor method for a deep-copy of processor object
     * @param p processor object to be deep copied
     */
    public Processor(Processor p){
        this.processorId=p.getProcessorId();
        this.earliestStartTime=p.getEarliestStartTime();
        this.empty=p.isEmpty();
    }

    public void setEarliestStartTime(int newStart){
        this.earliestStartTime=newStart;
        this.empty=false;
    }
}

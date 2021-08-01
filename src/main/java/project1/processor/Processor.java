package project1.processor;

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
        this.empty=p.getEmptyValue();
    }

    public void setEarliestStartTime(int newStart){
        this.earliestStartTime=newStart;
        this.empty=false;
    }

    public int getEarliestStartTime(){
        return this.earliestStartTime;
    }

    public int getProcessorId(){
        return this.processorId;
    }

    public boolean getEmptyValue(){ return this.empty; }
}

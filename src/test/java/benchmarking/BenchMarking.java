package benchmarking;

import lombok.AllArgsConstructor;
import project1.algorithm.PartialSchedule;
import project1.algorithm.SequentialDFS;
import project1.graph.Graph;

@AllArgsConstructor
public class BenchMarking {
    private final Graph g;
    private final int n;


    public void benchMarkingDFS(int iterations){
          //warm up
          for (int i=0;i<10;i++){
              PartialSchedule s = SequentialDFS.generateOptimalSchedule(this.g, this.n,1);
          }
          //a basic benchmarking for wall tick times
          long start=System.nanoTime();
          for (int i=0;i<iterations;i++){
              PartialSchedule s=SequentialDFS.generateOptimalSchedule(this.g,this.n,1);
          }
          long end=System.nanoTime();
          System.out.println("For a graph with "+
                  this.g.getNodes().size()+ " nodes and "+this.n+" processors used for scheduling: \n"+
                  "The average execution time is "+(double)(end-start)/iterations/1000000000+" seconds for "
                  + iterations+ " iterations. \n");
    }


}

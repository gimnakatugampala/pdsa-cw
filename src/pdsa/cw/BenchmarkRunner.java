package pdsa.cw;

import java.io.*;
import java.util.*;

public class BenchmarkRunner {
    private static final long SEED=2601L;
    public static void main(String[] args) throws Exception {new BenchmarkRunner().runAll();}

    public void runAll() throws IOException {
        File dir=new File("benchmark-results");if(!dir.exists()&&!dir.mkdirs())throw new IOException("Could not create benchmark-results directory.");
        benchmarkRoutes(new File(dir,"task1_route.csv"));
        benchmarkAllocation(new File(dir,"task2_resource_allocation.csv"));
        benchmarkNetwork(new File(dir,"task3_network.csv"));
        benchmarkDecision(new File(dir,"task4_decision.csv"));
        benchmarkTsp(new File(dir,"task5_optimization.csv"));
        System.out.println("\nBenchmark CSV files written to: "+dir.getAbsolutePath());
    }
    private void benchmarkRoutes(File f)throws IOException{try(PrintWriter out=new PrintWriter(f)){out.println("nodes,edges,algorithm,avgTimeMicros,heapDeltaBytes,distance");int[] sizes={25,50,100,250,500,1000};for(int n:sizes){Graph g=GraphGenerator.connectedGeographicGraph(n,n*3,SEED+n);int s=0,t=n-1;DijkstraAlgorithm dj=new DijkstraAlgorithm();BellmanFordAlgorithm bf=new BellmanFordAlgorithm();AStarAlgorithm as=new AStarAlgorithm();PathResult d=warmRoute(dj,g,s,t);PathResult b=warmRoute(bf,g,s,t);PathResult a=warmRoute(as,g,s,t);if(d.distance!=b.distance||d.distance!=a.distance)throw new IllegalStateException("Route algorithms disagree at n="+n);write(out,n,g.edgeCount(),"Dijkstra",()->dj.findPath(g,s,t).distance);write(out,n,g.edgeCount(),"Bellman-Ford",()->bf.findPath(g,s,t).distance);write(out,n,g.edgeCount(),"A* (Haversine)",()->as.findPath(g,s,t).distance);}}}
    private PathResult warmRoute(Object algorithm,Graph g,int s,int t){for(int i=0;i<3;i++){if(algorithm instanceof DijkstraAlgorithm)((DijkstraAlgorithm)algorithm).findPath(g,s,t);else if(algorithm instanceof BellmanFordAlgorithm)((BellmanFordAlgorithm)algorithm).findPath(g,s,t);else ((AStarAlgorithm)algorithm).findPath(g,s,t);}if(algorithm instanceof DijkstraAlgorithm)return ((DijkstraAlgorithm)algorithm).findPath(g,s,t);if(algorithm instanceof BellmanFordAlgorithm)return ((BellmanFordAlgorithm)algorithm).findPath(g,s,t);return ((AStarAlgorithm)algorithm).findPath(g,s,t);}

    private void benchmarkAllocation(File f)throws IOException{
        try(PrintWriter out=new PrintWriter(f)){
            out.println("items,capacity,algorithm,avgTimeMicros,heapDeltaBytes,value,weight");
            int[] ns={25,50,100,200,400,800}; ResourceAllocation ra=new ResourceAllocation();
            for(int n:ns){
                List<CargoItem> x=GraphGenerator.randomCargo(n,SEED+n); int cap=n*10;
                for(int i=0;i<3;i++){ra.dynamicProgramming(x,cap);ra.greedy(x,cap);ra.branchAndBound(x,cap);}
                writeAllocation(out,n,cap,"Dynamic Programming",()->ra.dynamicProgramming(x,cap));
                writeAllocation(out,n,cap,"Greedy",()->ra.greedy(x,cap));
                writeAllocation(out,n,cap,"Branch and Bound",()->ra.branchAndBound(x,cap));
            }
        }
    }
    private void writeAllocation(PrintWriter out,int n,int cap,String name,Callable<ResourceAllocation.AllocationResult> call)throws IOException{
        long total=0,best=0; ResourceAllocation.AllocationResult r=null;
        for(int i=0;i<10;i++){gc();long b=used(),t=System.nanoTime();try{r=call.call();}catch(Exception ex){throw new IOException(ex);}total+=System.nanoTime()-t;best=Math.max(best,Math.max(0,used()-b));}
        out.printf(Locale.US,"%d,%d,%s,%.3f,%d,%d,%d%n",n,cap,name,(total/10)/1000.0,best,r.value,r.totalWeight());
    }
    private void benchmarkNetwork(File f)throws IOException{try(PrintWriter out=new PrintWriter(f)){out.println("nodes,edges,algorithm,avgTimeMicros,heapDeltaBytes,totalWeight");int[]ns={25,50,100,250,500,1000};NetworkAnalysis na=new NetworkAnalysis();for(int n:ns){Graph g=GraphGenerator.connectedGeographicGraph(n,n*4,SEED+100+n);for(int i=0;i<3;i++){na.prim(g);na.kruskal(g);na.boruvka(g);}List<NetworkAnalysis.MSTResult> msts=Arrays.asList(na.prim(g),na.kruskal(g),na.boruvka(g));if(!(msts.get(0).totalWeight==msts.get(1).totalWeight&&msts.get(1).totalWeight==msts.get(2).totalWeight))throw new IllegalStateException("MST algorithms disagree");writeMst(out,n,g.edgeCount(),"Prim",()->na.prim(g));writeMst(out,n,g.edgeCount(),"Kruskal",()->na.kruskal(g));writeMst(out,n,g.edgeCount(),"Boruvka",()->na.boruvka(g));}}}
    private void writeMst(PrintWriter out,int n,int e,String name,Callable<NetworkAnalysis.MSTResult>call)throws IOException{long best=0,total=0;NetworkAnalysis.MSTResult r=null;for(int i=0;i<10;i++){gc();long before=used();long t=System.nanoTime();try{r=call.call();}catch(Exception ex){throw new IOException(ex);}total+=System.nanoTime()-t;best=Math.max(best,Math.max(0,used()-before));}out.printf(Locale.US,"%d,%d,%s,%.3f,%d,%d%n",n,e,name,(total/10)/1000.0,best,r.totalWeight);}

    private void benchmarkDecision(File f)throws IOException{try(PrintWriter out=new PrintWriter(f)){out.println("suppliers,algorithm,avgTimeMicros,heapDeltaBytes,bestSupplier,bestScore");int[]ns={25,50,100,250,500,1000,5000};DecisionModule dm=new DecisionModule();double[]w={0.40,0.40,0.20};for(int n:ns){List<Supplier>s=GraphGenerator.randomSuppliers(n,SEED+200+n);for(int i=0;i<3;i++){dm.weightedSumModel(s,w);dm.topsis(s,w);dm.weightedProductModel(s,w);}writeDecision(out,n,"WSM",()->dm.weightedSumModel(s,w));writeDecision(out,n,"TOPSIS",()->dm.topsis(s,w));writeDecision(out,n,"Weighted Product Model",()->dm.weightedProductModel(s,w));}}}
    private void writeDecision(PrintWriter out,int n,String name,Callable<List<DecisionModule.ScoredSupplier>>call)throws IOException{long total=0,best=0;List<DecisionModule.ScoredSupplier>r=null;for(int i=0;i<10;i++){gc();long b=used(),t=System.nanoTime();try{r=call.call();}catch(Exception ex){throw new IOException(ex);}total+=System.nanoTime()-t;best=Math.max(best,Math.max(0,used()-b));}out.printf(Locale.US,"%d,%s,%.3f,%d,%s,%.6f%n",n,name,(total/10)/1000.0,best,r.get(0).supplier.name,r.get(0).score);}

    private void benchmarkTsp(File f)throws IOException{try(PrintWriter out=new PrintWriter(f)){out.println("cities,algorithm,avgTimeMicros,heapDeltaBytes,tourCost");int[]ns={5,6,7,8,9,10,11};OptimizationModule om=new OptimizationModule();for(int n:ns){List<List<Integer>>d=GraphGenerator.randomTspMatrix(n,SEED+300+n);OptimizationModule.TspResult exact=om.exact(d);OptimizationModule.TspResult nn=om.nearestNeighbor(d);OptimizationModule.TspResult opt=om.twoOpt(d);if(nn.tour.isEmpty()||opt.tour.isEmpty())throw new IllegalStateException("Invalid tour");writeTsp(out,n,"Exact Enumeration",()->om.exact(d));writeTsp(out,n,"Nearest Neighbor",()->om.nearestNeighbor(d));writeTsp(out,n,"2-opt",()->om.twoOpt(d));System.out.printf(Locale.US,"TSP n=%d exact=%d NN=%d 2-opt=%d%n",n,exact.cost,nn.cost,opt.cost);}}}
    private void writeTsp(PrintWriter out,int n,String name,Callable<OptimizationModule.TspResult>call)throws IOException{long total=0,best=0;OptimizationModule.TspResult r=null;int repeats=name.startsWith("Exact")?3:10;for(int i=0;i<repeats;i++){gc();long b=used(),t=System.nanoTime();try{r=call.call();}catch(Exception ex){throw new IOException(ex);}total+=System.nanoTime()-t;best=Math.max(best,Math.max(0,used()-b));}out.printf(Locale.US,"%d,%s,%.3f,%d,%d%n",n,name,(total/repeats)/1000.0,best,r.cost);}

    @FunctionalInterface private interface Callable<T>{T call()throws Exception;}
    private void write(PrintWriter out,int n,int e,String name,Callable<Integer>call)throws IOException{long total=0,best=0;int value=0;for(int i=0;i<10;i++){gc();long b=used(),t=System.nanoTime();try{value=call.call();}catch(Exception ex){throw new IOException(ex);}total+=System.nanoTime()-t;best=Math.max(best,Math.max(0,used()-b));}out.printf(Locale.US,"%d,%d,%s,%.3f,%d,%d%n",n,e,name,(total/10)/1000.0,best,value);}
    private static long used(){Runtime r=Runtime.getRuntime();return r.totalMemory()-r.freeMemory();}
    private static void gc(){System.gc();try{Thread.sleep(5);}catch(InterruptedException e){Thread.currentThread().interrupt();}}

}

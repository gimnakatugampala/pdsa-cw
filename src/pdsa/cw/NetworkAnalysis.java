package pdsa.cw;

import java.util.*;

public class NetworkAnalysis {
    public MSTResult prim(Graph graph){
        if(graph==null)throw new IllegalArgumentException("Graph cannot be null.");
        if(graph.nodes==0)return new MSTResult(0,new ArrayList<>(),"Prim");
        boolean[] used=new boolean[graph.nodes]; PriorityQueue<Edge> pq=new PriorityQueue<>(Comparator.comparingInt(e->e.weight));
        List<Edge> chosen=new ArrayList<>(); int total=0; used[0]=true; pq.addAll(graph.adjList.get(0));
        while(!pq.isEmpty()&&chosen.size()<graph.nodes-1){Edge e=pq.poll(); if(used[e.targetNode])continue; used[e.targetNode]=true; chosen.add(e); total+=e.weight; pq.addAll(graph.adjList.get(e.targetNode));}
        if(chosen.size()!=graph.nodes-1)throw new IllegalStateException("Graph is disconnected; no spanning tree exists.");
        return new MSTResult(total,chosen,"Prim");
    }
    public MSTResult kruskal(Graph graph){
        if(graph==null)throw new IllegalArgumentException("Graph cannot be null.");
        List<Edge> edges=graph.uniqueEdges(); edges.sort(Comparator.comparingInt(e->e.weight));
        DisjointSet ds=new DisjointSet(graph.nodes); List<Edge> chosen=new ArrayList<>(); int total=0;
        for(Edge e:edges)if(ds.union(e.sourceNode,e.targetNode)){chosen.add(e);total+=e.weight;if(chosen.size()==graph.nodes-1)break;}
        if(chosen.size()!=graph.nodes-1)throw new IllegalStateException("Graph is disconnected; no spanning tree exists.");
        return new MSTResult(total,chosen,"Kruskal");
    }
    public MSTResult boruvka(Graph graph){
        if(graph==null)throw new IllegalArgumentException("Graph cannot be null.");
        DisjointSet ds=new DisjointSet(graph.nodes); int components=graph.nodes,total=0; List<Edge> chosen=new ArrayList<>();
        while(components>1){Edge[] cheapest=new Edge[graph.nodes];
            for(Edge e:graph.uniqueEdges()){int a=ds.find(e.sourceNode),b=ds.find(e.targetNode);if(a==b)continue;if(cheapest[a]==null||e.weight<cheapest[a].weight)cheapest[a]=e;if(cheapest[b]==null||e.weight<cheapest[b].weight)cheapest[b]=e;}
            boolean merged=false; for(Edge e:cheapest)if(e!=null&&ds.union(e.sourceNode,e.targetNode)){chosen.add(e);total+=e.weight;components--;merged=true;} if(!merged)throw new IllegalStateException("Graph is disconnected; no spanning tree exists.");
        }
        return new MSTResult(total,chosen,"Boruvka");
    }
    public static final class MSTResult {public final int totalWeight;public final List<Edge>edges;public final String algorithm;MSTResult(int w,List<Edge>e,String a){totalWeight=w;edges=Collections.unmodifiableList(e);algorithm=a;} public void print(){System.out.println("\n--- "+algorithm+" MST ---\nTotal weight: "+totalWeight);}}
}

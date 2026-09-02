package pdsa.cw;

import java.util.*;

public class Graph{
    int nodes;
    List<List<Edge>> adjList;

    public Graph(int nodes){
        this.nodes = nodes;
        adjList = new ArrayList<>();

        for(int i=0; i<nodes; i++){
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int target, int weight){
        adjList.get(source).add(new Edge(target, weight));
        adjList.get(target).add(new Edge(source, weight));
    }
}
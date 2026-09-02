package pdsa.cw;

import java.util.*;

public class NetworkAnalysis {

    // --- PRIM'S ALGORITHM ---
    public void analyzeWithPrims(Graph graph, String[] cities) {
        int nodesCount = graph.nodes;
        boolean[] inMST = new boolean[nodesCount];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        
        int totalCost = 0;
        System.out.println("Prim's MST Backbone:");
        
        // Start from node 0 (targetNode, parentNode, weight)
        pq.add(new int[]{0, -1, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int parent = current[1];
            int weight = current[2];

            if (inMST[u]) continue;
            
            inMST[u] = true;
            totalCost += weight;
            
            if (parent != -1) {
                System.out.println(cities[parent] + " - " + cities[u] + " : " + weight + " km");
            }

            for (Edge edge : graph.adjList.get(u)) {
                if (!inMST[edge.targetNode]) {
                    pq.add(new int[]{edge.targetNode, u, edge.weight});
                }
            }
        }
        System.out.println("Total Prim's Network Cost: " + totalCost + " km\n");
    }

    // --- KRUSKAL'S ALGORITHM ---
    public void analyzeWithKruskals(Graph graph, String[] cities) {
        List<int[]> allEdges = new ArrayList<>();
        
        // Extract all unique edges from the undirected graph
        for (int u = 0; u < graph.nodes; u++) {
            for (Edge edge : graph.adjList.get(u)) {
                if (u < edge.targetNode) { 
                    allEdges.add(new int[]{u, edge.targetNode, edge.weight});
                }
            }
        }

        // Sort edges by weight
        allEdges.sort(Comparator.comparingInt(a -> a[2]));

        DisjointSet ds = new DisjointSet(graph.nodes);
        int totalCost = 0;
        System.out.println("Kruskal's MST Backbone:");

        for (int[] edge : allEdges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];

            if (ds.find(u) != ds.find(v)) {
                ds.union(u, v);
                totalCost += weight;
                System.out.println(cities[u] + " - " + cities[v] + " : " + weight + " km");
            }
        }
        System.out.println("Total Kruskal's Network Cost: " + totalCost + " km\n");
        
    }
}
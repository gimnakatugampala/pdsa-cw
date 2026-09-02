package pdsa.cw;

import java.util.Arrays;

public class BellmanFordAlgorithm {
    
    public void findShortestPath(Graph graph, int startNode, String[] cityNames) {
        int nodesCount = graph.nodes;
        int[] distances = new int[nodesCount];
        int[] previousNode = new int[nodesCount];

        // Step 1: Initialize distances
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previousNode, -1);
        distances[startNode] = 0;

        // Step 2: Relax all edges |V| - 1 times
        for (int i = 1; i < nodesCount; i++) {
            for (int u = 0; u < nodesCount; u++) {
                if (distances[u] == Integer.MAX_VALUE) continue;
                
                for (Edge edge : graph.adjList.get(u)) {
                    int v = edge.targetNode;
                    int weight = edge.weight;
                    
                    if (distances[u] + weight < distances[v]) {
                        distances[v] = distances[u] + weight;
                        previousNode[v] = u;
                    }
                }
            }
        }

        // Step 3: Check for negative-weight cycles
        for (int u = 0; u < nodesCount; u++) {
            if (distances[u] == Integer.MAX_VALUE) continue;
            for (Edge edge : graph.adjList.get(u)) {
                int v = edge.targetNode;
                int weight = edge.weight;
                if (distances[u] + weight < distances[v]) {
                    System.out.println("Graph contains a negative-weight cycle!");
                    return;
                }
            }
        }

        printRoutes(startNode, distances, previousNode, cityNames);
    }

    private void printRoutes(int start, int[] dist, int[] prev, String[] cities) {
        System.out.println("\n--- Bellman-Ford Optimal Routes from " + cities[start] + " ---");
        for (int i = 0; i < dist.length; i++) {
            if (i == start) continue;
            System.out.print("To " + cities[i] + " (" + dist[i] + " km): ");
            printPath(i, prev, cities);
            System.out.println();
        }
    }

    private void printPath(int current, int[] prev, String[] cities) {
        if (current == -1) return;
        printPath(prev[current], prev, cities);
        System.out.print(cities[current] + (prev[current] != -1 ? " <- " : " "));
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsa.cw;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author user
 */
class dijkstra {
        public void findShortestPath(Graph graph, int startNode, String[] cityNames) {
        int nodesCount = graph.nodes;
        int[] distances = new int[nodesCount];
        int[] previousNode = new int[nodesCount]; // Tracks the path

        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previousNode, -1);
        distances[startNode] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[] { startNode, 0 });

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentNode = current[0];
            int currentDist = current[1];

            if (currentDist > distances[currentNode]) continue;

            for (Edge edge : graph.adjList.get(currentNode)) {
                int newDist = distances[currentNode] + edge.weight;
                if (newDist < distances[edge.targetNode]) {
                    distances[edge.targetNode] = newDist;
                    previousNode[edge.targetNode] = currentNode; // Record the route
                    pq.add(new int[] { edge.targetNode, newDist });
                }
            }
        }
        printRoutes(startNode, distances, previousNode, cityNames);
    }

    private void printRoutes(int start, int[] dist, int[] prev, String[] cities) {
        System.out.println("\n--- Optimal Routes from " + cities[start] + " ---");
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

package pdsa.cw;

import java.util.Random;

public class GraphGenerator {
    
    public static Graph generateRandomConnectedGraph(int nodes, int edgesPerNode) {
        Graph graph = new Graph(nodes);
        Random rand = new Random(42); // Fixed seed for reproducible benchmarks

        // Guarantee graph connectivity by creating a spanning backbone (0-1-2-...-N)
        for (int i = 0; i < nodes - 1; i++) {
            int weight = rand.nextInt(100) + 5; // weights between 5 and 104 km
            graph.addEdge(i, i + 1, weight);
        }

        // Add remaining random edges to increase graph density
        int additionalEdges = (nodes * edgesPerNode) - (nodes - 1);
        for (int i = 0; i < additionalEdges; i++) {
            int u = rand.nextInt(nodes);
            int v = rand.nextInt(nodes);
            if (u != v) {
                int weight = rand.nextInt(100) + 5;
                graph.addEdge(u, v, weight);
            }
        }

        return graph;
    }
}
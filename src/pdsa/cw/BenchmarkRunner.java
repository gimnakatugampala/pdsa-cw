package pdsa.cw;

public class BenchmarkRunner {

    public static void main(String[] args) {
        int[] testSizes = {10, 50, 100, 250, 500, 1000};
        int edgesPerNode = 3; // Average degree

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm();
        Runtime runtime = Runtime.getRuntime();

        System.out.println("=========================================================================================");
        System.out.printf("%-10s %-12s %-18s %-20s %-18s%n", 
                "Nodes (V)", "Edges (E)", "Dijkstra (ms)", "Bellman-Ford (ms)", "Dijkstra Mem (KB)");
        System.out.println("=========================================================================================");

        for (int nodes : testSizes) {
            Graph graph = GraphGenerator.generateRandomConnectedGraph(nodes, edgesPerNode);
            int totalEdges = 0;
            for (int i = 0; i < nodes; i++) {
                totalEdges += graph.adjList.get(i).size();
            }

            // Benchmark Dijkstra
            runtime.gc();
            long memBeforeD = runtime.totalMemory() - runtime.freeMemory();
            long startD = System.nanoTime();
            // Call an overloaded method or pass null for cityNames to suppress console prints during benchmark
            executeDijkstraBenchmark(graph, 0);
            long endD = System.nanoTime();
            long memAfterD = runtime.totalMemory() - runtime.freeMemory();

            double timeDijkstra = (endD - startD) / 1_000_000.0;
            double memDijkstra = Math.max(0, (memAfterD - memBeforeD) / 1024.0);

            // Benchmark Bellman-Ford (omit Bellman-Ford at 1000 if it takes too long due to O(V*E))
            double timeBF = 0.0;
            if (nodes <= 500) {
                runtime.gc();
                long startBF = System.nanoTime();
                executeBellmanFordBenchmark(graph, 0);
                long endBF = System.nanoTime();
                timeBF = (endBF - startBF) / 1_000_000.0;
            }

            System.out.printf("%-10d %-12d %-18.4f %-20s %-18.2f%n", 
                    nodes, 
                    totalEdges / 2, 
                    timeDijkstra, 
                    (nodes > 500 ? "Skipped (Too Slow)" : String.format("%.4f", timeBF)), 
                    memDijkstra);
        }
        System.out.println("=========================================================================================");
    }

    // Benchmark helper that suppresses console outputs
    private static void executeDijkstraBenchmark(Graph graph, int start) {
        int[] dist = new int[graph.nodes];
        java.util.Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        java.util.PriorityQueue<int[]> pq = new java.util.PriorityQueue<>(java.util.Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];
            if (d > dist[u]) continue;
            for (Edge e : graph.adjList.get(u)) {
                if (dist[u] + e.weight < dist[e.targetNode]) {
                    dist[e.targetNode] = dist[u] + e.weight;
                    pq.add(new int[]{e.targetNode, dist[e.targetNode]});
                }
            }
        }
    }

    private static void executeBellmanFordBenchmark(Graph graph, int start) {
        int[] dist = new int[graph.nodes];
        java.util.Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        for (int i = 1; i < graph.nodes; i++) {
            for (int u = 0; u < graph.nodes; u++) {
                if (dist[u] == Integer.MAX_VALUE) continue;
                for (Edge e : graph.adjList.get(u)) {
                    if (dist[u] + e.weight < dist[e.targetNode]) {
                        dist[e.targetNode] = dist[u] + e.weight;
                    }
                }
            }
        }
    }
}
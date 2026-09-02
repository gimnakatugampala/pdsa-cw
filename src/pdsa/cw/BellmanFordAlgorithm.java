package pdsa.cw;

import java.util.*;

public class BellmanFordAlgorithm {
    public PathResult findPath(Graph graph, int start, int goal) {
        if (graph == null) throw new IllegalArgumentException("Graph cannot be null.");
        int n = graph.nodes;
        if (start < 0 || start >= n || goal < 0 || goal >= n) throw new IndexOutOfBoundsException("Invalid node.");
        int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
        int[] prev = new int[n]; Arrays.fill(prev, -1); dist[start] = 0;
        List<Edge> edges = graph.uniqueEdges();
        for (int i = 1; i < n; i++) {
            boolean changed = false;
            for (Edge e : edges) {
                if (dist[e.sourceNode] != Integer.MAX_VALUE && dist[e.sourceNode] + e.weight < dist[e.targetNode]) {
                    dist[e.targetNode] = dist[e.sourceNode] + e.weight; prev[e.targetNode] = e.sourceNode; changed = true;
                }
                if (dist[e.targetNode] != Integer.MAX_VALUE && dist[e.targetNode] + e.weight < dist[e.sourceNode]) {
                    dist[e.sourceNode] = dist[e.targetNode] + e.weight; prev[e.sourceNode] = e.targetNode; changed = true;
                }
            }
            if (!changed) break;
        }
        // Graph enforces non-negative weights, but retain the standard negative-cycle check for algorithm completeness.
        for (Edge e : edges) {
            if (dist[e.sourceNode] != Integer.MAX_VALUE && dist[e.sourceNode] + e.weight < dist[e.targetNode])
                throw new IllegalStateException("Negative-weight cycle detected.");
        }
        return PathResult.from(goal, dist, prev, start);
    }
    public void findShortestPath(Graph graph, int start, int goal, String[] names) { findPath(graph,start,goal).print("Bellman-Ford", names); }
}

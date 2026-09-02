package pdsa.cw;

import java.util.*;

public class DijkstraAlgorithm {
    public PathResult findPath(Graph graph, int start, int goal) {
        validate(graph, start, goal);
        int n = graph.nodes;
        int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
        int[] prev = new int[n]; Arrays.fill(prev, -1);
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.distance));
        dist[start] = 0; pq.add(new Node(start, 0));
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.distance != dist[cur.node]) continue;
            if (cur.node == goal) break;
            for (Edge e : graph.adjList.get(cur.node)) {
                long nd = (long)dist[cur.node] + e.weight;
                if (nd < dist[e.targetNode]) {
                    dist[e.targetNode] = (int)nd; prev[e.targetNode] = cur.node;
                    pq.add(new Node(e.targetNode, dist[e.targetNode]));
                }
            }
        }
        return PathResult.from(goal, dist, prev, start);
    }
    public void findShortestPath(Graph graph, int start, int goal, String[] names) {
        PathResult r = findPath(graph, start, goal); r.print("Dijkstra", names);
    }
    private static void validate(Graph g, int s, int t) {
        if (g == null) throw new IllegalArgumentException("Graph cannot be null.");
        if (s < 0 || s >= g.nodes || t < 0 || t >= g.nodes) throw new IndexOutOfBoundsException("Invalid node.");
    }
    private static final class Node { final int node, distance; Node(int n, int d){node=n;distance=d;} }
}

package pdsa.cw;

import java.util.*;

public class AStarAlgorithm {
    public PathResult findPath(Graph graph, int start, int goal) {
        if (graph == null) throw new IllegalArgumentException("Graph cannot be null.");
        if (start < 0 || start >= graph.nodes || goal < 0 || goal >= graph.nodes) throw new IndexOutOfBoundsException("Invalid node.");
        int n = graph.nodes;
        int[] g = new int[n]; int[] prev = new int[n]; Arrays.fill(g, Integer.MAX_VALUE); Arrays.fill(prev, -1);
        PriorityQueue<NodeRecord> open = new PriorityQueue<>(Comparator.comparingDouble(x -> x.f));
        g[start] = 0; open.add(new NodeRecord(start, heuristic(graph,start,goal)));
        boolean[] closed = new boolean[n];
        while (!open.isEmpty()) {
            NodeRecord r = open.poll(); int u=r.node;
            if (closed[u]) continue;
            if (u==goal) break;
            closed[u]=true;
            for (Edge e : graph.adjList.get(u)) {
                int v=e.targetNode;
                int tentative=g[u]+e.weight;
                if (tentative<g[v]) { g[v]=tentative; prev[v]=u; open.add(new NodeRecord(v,tentative+heuristic(graph,v,goal))); }
            }
        }
        return PathResult.from(goal,g,prev,start);
    }
    public void findShortestPath(Graph graph, int start, int goal, String[] names, double[] ignoredHeuristic) { findPath(graph,start,goal).print("A* (Haversine)", names); }

    // Scale Haversine to match the graph's edge-weight unit (km). It is admissible when edge weights are physical road distances.
    public static double heuristic(Graph graph, int from, int goal) {
        final double R = 6371.0;
        double lat1=Math.toRadians(graph.getLatitude(from)), lat2=Math.toRadians(graph.getLatitude(goal));
        double dLat=lat2-lat1, dLon=Math.toRadians(graph.getLongitude(goal)-graph.getLongitude(from));
        double a=Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(lat1)*Math.cos(lat2)*Math.sin(dLon/2)*Math.sin(dLon/2);
        return R*2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    }
    private static final class NodeRecord { final int node; final double f; NodeRecord(int n,double f){node=n;this.f=f;} }
}

package pdsa.cw;

import java.util.*;

public class Graph {
    public final int nodes;
    public final List<List<Edge>> adjList;
    private final double[] latitude;
    private final double[] longitude;

    public Graph(int nodes) {
        if (nodes <= 0) throw new IllegalArgumentException("Graph must contain at least one node.");
        this.nodes = nodes;
        this.adjList = new ArrayList<>(nodes);
        this.latitude = new double[nodes];
        this.longitude = new double[nodes];
        for (int i = 0; i < nodes; i++) adjList.add(new ArrayList<>());
    }

    public void addEdge(int source, int target, int weight) {
        validateNode(source); validateNode(target);
        if (source == target) throw new IllegalArgumentException("Self-loops are not allowed.");
        if (weight < 0) throw new IllegalArgumentException("Negative edge weights are not supported.");
        adjList.get(source).add(new Edge(source, target, weight));
        adjList.get(target).add(new Edge(target, source, weight));
    }

    public void setCoordinate(int node, double lat, double lon) {
        validateNode(node);
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        if (lon < -180 || lon > 180) throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        latitude[node] = lat; longitude[node] = lon;
    }
    public double getLatitude(int node) { validateNode(node); return latitude[node]; }
    public double getLongitude(int node) { validateNode(node); return longitude[node]; }

    public int edgeCount() {
        long total = 0;
        for (List<Edge> list : adjList) total += list.size();
        return (int)(total / 2);
    }

    public List<Edge> uniqueEdges() {
        List<Edge> result = new ArrayList<>();
        for (int u = 0; u < nodes; u++) {
            for (Edge e : adjList.get(u)) if (u < e.targetNode) result.add(e);
        }
        return result;
    }

    private void validateNode(int node) {
        if (node < 0 || node >= nodes) throw new IndexOutOfBoundsException("Invalid node: " + node);
    }
}

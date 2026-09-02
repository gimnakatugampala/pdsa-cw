package pdsa.cw;

import java.util.*;

public class Graph {

    int nodes;
    List<List<Edge>> adjList;

    // Coordinate information used by A*
    private final double[] latitude;
    private final double[] longitude;

    public Graph(int nodes) {

        if (nodes <= 0) {
            throw new IllegalArgumentException(
                    "Graph must contain at least one node."
            );
        }

        this.nodes = nodes;

        adjList = new ArrayList<>();

        latitude = new double[nodes];
        longitude = new double[nodes];

        for (int i = 0; i < nodes; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    /**
     * Adds an undirected edge between two nodes.
     */
    public void addEdge(int source, int target, int weight) {

        validateNode(source);
        validateNode(target);

        if (source == target) {
            throw new IllegalArgumentException(
                    "Self-loops are not allowed."
            );
        }

        if (weight < 0) {
            throw new IllegalArgumentException(
                    "Negative edge weights are not supported."
            );
        }

        adjList.get(source).add(
                new Edge(target, weight)
        );

        adjList.get(target).add(
                new Edge(source, weight)
        );
    }

    /**
     * Stores the geographical coordinates of a node.
     *
     * @param node node index
     * @param lat latitude
     * @param lon longitude
     */
    public void setCoordinate(
            int node,
            double lat,
            double lon) {

        validateNode(node);

        if (lat < -90.0 || lat > 90.0) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90 degrees."
            );
        }

        if (lon < -180.0 || lon > 180.0) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180 degrees."
            );
        }

        latitude[node] = lat;
        longitude[node] = lon;
    }

    /**
     * Returns the latitude of a node.
     */
    public double getLatitude(int node) {

        validateNode(node);

        return latitude[node];
    }

    /**
     * Returns the longitude of a node.
     */
    public double getLongitude(int node) {

        validateNode(node);

        return longitude[node];
    }

    /**
     * Validates that a node index exists in the graph.
     */
    private void validateNode(int node) {

        if (node < 0 || node >= nodes) {
            throw new IndexOutOfBoundsException(
                    "Node index must be between 0 and "
                    + (nodes - 1) + "."
            );
        }
    }
}
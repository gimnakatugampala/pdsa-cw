package pdsa.cw;

import java.util.Random;

public class GraphGenerator {

    /*
     * Fixed seed makes benchmark results reproducible.
     */
    private static final long RANDOM_SEED = 42L;

    /*
     * Starting coordinate used for generated graphs.
     * The generated coordinates represent an abstract
     * geographical area.
     */
    private static final double BASE_LATITUDE = 6.90;
    private static final double BASE_LONGITUDE = 79.80;

    /**
     * Generates a connected weighted graph with geographical
     * coordinates suitable for A*.
     *
     * @param nodes number of vertices
     * @param edgesPerNode approximate number of edges per vertex
     * @return generated graph
     */
    public static Graph generateRandomConnectedGraph(
            int nodes,
            int edgesPerNode) {

        if (nodes < 2) {
            throw new IllegalArgumentException(
                    "The graph must contain at least 2 nodes."
            );
        }

        if (edgesPerNode < 1) {
            throw new IllegalArgumentException(
                    "Edges per node must be at least 1."
            );
        }

        Graph graph = new Graph(nodes);

        Random random = new Random(
                RANDOM_SEED + nodes
        );

        /*
         * -----------------------------------------------------
         * STEP 1: Generate geographical coordinates
         * -----------------------------------------------------
         */
        for (int i = 0; i < nodes; i++) {

            /*
             * Spread nodes over an artificial geographical area.
             */
            double latitude =
                    BASE_LATITUDE
                    + random.nextDouble() * 3.0;

            double longitude =
                    BASE_LONGITUDE
                    + random.nextDouble() * 3.0;

            graph.setCoordinate(
                    i,
                    latitude,
                    longitude
            );
        }

        /*
         * -----------------------------------------------------
         * STEP 2: Create a connected backbone
         * -----------------------------------------------------
         *
         * 0 -> 1 -> 2 -> 3 -> ... -> N
         *
         * This guarantees that every node is reachable.
         */
        for (int i = 0; i < nodes - 1; i++) {

            int weight = calculateEdgeWeight(
                    graph,
                    i,
                    i + 1,
                    random
            );

            graph.addEdge(
                    i,
                    i + 1,
                    weight
            );
        }

        /*
         * -----------------------------------------------------
         * STEP 3: Add additional random edges
         * -----------------------------------------------------
         */
        int targetEdges =
                nodes * edgesPerNode;

        int currentEdges = nodes - 1;

        int attempts = 0;

        int maxAttempts =
                Math.max(targetEdges * 10, 100);

        while (currentEdges < targetEdges
                && attempts < maxAttempts) {

            attempts++;

            int source =
                    random.nextInt(nodes);

            int target =
                    random.nextInt(nodes);

            if (source == target) {
                continue;
            }

            /*
             * Avoid duplicate undirected edges.
             */
            if (edgeAlreadyExists(
                    graph,
                    source,
                    target)) {

                continue;
            }

            int weight = calculateEdgeWeight(
                    graph,
                    source,
                    target,
                    random
            );

            graph.addEdge(
                    source,
                    target,
                    weight
            );

            currentEdges++;
        }

        return graph;
    }

    /**
     * Calculates an edge weight using the geographical
     * distance between two nodes.
     *
     * The weight is deliberately >= the straight-line
     * geographical distance. This keeps the A* heuristic
     * admissible.
     */
    private static int calculateEdgeWeight(
            Graph graph,
            int source,
            int target,
            Random random) {

        double directDistance =
                haversineDistance(
                        graph.getLatitude(source),
                        graph.getLongitude(source),
                        graph.getLatitude(target),
                        graph.getLongitude(target)
                );

        /*
         * Add a realistic detour factor between 1.10 and 1.50.
         */
        double detourFactor =
                1.10 + (random.nextDouble() * 0.40);

        double routeDistance =
                directDistance * detourFactor;

        /*
         * Minimum weight of 1 km.
         */
        return Math.max(
                1,
                (int) Math.ceil(routeDistance)
        );
    }

    /**
     * Checks whether an edge already exists.
     */
    private static boolean edgeAlreadyExists(
            Graph graph,
            int source,
            int target) {

        for (Edge edge :
                graph.adjList.get(source)) {

            if (edge.targetNode == target) {
                return true;
            }
        }

        return false;
    }

    /**
     * Haversine distance between two geographical
     * coordinates in kilometres.
     */
    private static double haversineDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double EARTH_RADIUS_KM = 6371.0;

        double latitude1 =
                Math.toRadians(lat1);

        double latitude2 =
                Math.toRadians(lat2);

        double deltaLatitude =
                Math.toRadians(lat2 - lat1);

        double deltaLongitude =
                Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(deltaLatitude / 2)
                        * Math.sin(deltaLatitude / 2)
                + Math.cos(latitude1)
                        * Math.cos(latitude2)
                        * Math.sin(deltaLongitude / 2)
                        * Math.sin(deltaLongitude / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return EARTH_RADIUS_KM * c;
    }
}
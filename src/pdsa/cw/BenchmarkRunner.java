package pdsa.cw;

import java.util.Arrays;

public class BenchmarkRunner {

    /*
     * These sizes provide a meaningful scalability comparison
     * while still allowing all three algorithms to be measured.
     */
    private static final int[] TEST_SIZES = {
            10, 50, 100, 250, 500
    };

    /*
     * Approximate average number of edges per vertex.
     */
    private static final int EDGES_PER_NODE = 3;

    /*
     * Number of warm-up executions before measurement.
     * This reduces the effect of JVM startup/JIT compilation.
     */
    private static final int WARMUP_RUNS = 5;

    /*
     * Number of measured executions.
     */
    private static final int MEASUREMENT_RUNS = 20;

    public static void main(String[] args) {

        System.out.println(
                "============================================================================="
        );
        System.out.println(
                "             TASK 1 - ROUTE OPTIMIZATION BENCHMARK"
        );
        System.out.println(
                "          Dijkstra vs Bellman-Ford vs A* Algorithm"
        );
        System.out.println(
                "============================================================================="
        );

        System.out.printf(
                "%-10s %-10s %-18s %-20s %-15s %-15s%n",
                "Nodes",
                "Edges",
                "Dijkstra(ms)",
                "Bellman-Ford(ms)",
                "A*(ms)",
                "Correct?"
        );

        System.out.println(
                "-----------------------------------------------------------------------------"
        );

        for (int nodes : TEST_SIZES) {

            Graph graph =
                    GraphGenerator.generateRandomConnectedGraph(
                            nodes,
                            EDGES_PER_NODE
                    );

            int edgeCount =
                    countUndirectedEdges(graph);

            int startNode = 0;
            int goalNode = nodes - 1;

            /*
             * ---------------------------------------------------------
             * WARM-UP
             * ---------------------------------------------------------
             *
             * Warm-up prevents JVM startup effects from dominating
             * the measured execution times.
             */
            for (int i = 0; i < WARMUP_RUNS; i++) {

                dijkstraDistance(
                        graph,
                        startNode,
                        goalNode
                );

                bellmanFordDistance(
                        graph,
                        startNode,
                        goalNode
                );

                aStarDistance(
                        graph,
                        startNode,
                        goalNode
                );
            }

            /*
             * ---------------------------------------------------------
             * DIJKSTRA
             * ---------------------------------------------------------
             */
            long dijkstraTotalTime = 0;

            long dijkstraMemoryBefore =
                    usedMemory();

            int dijkstraDistance = 0;

            for (int i = 0; i < MEASUREMENT_RUNS; i++) {

                long startTime =
                        System.nanoTime();

                dijkstraDistance =
                        dijkstraDistance(
                                graph,
                                startNode,
                                goalNode
                        );

                long endTime =
                        System.nanoTime();

                dijkstraTotalTime +=
                        (endTime - startTime);
            }

            long dijkstraMemoryAfter =
                    usedMemory();

            double dijkstraAverageTime =
                    dijkstraTotalTime
                    / (double) MEASUREMENT_RUNS
                    / 1_000_000.0;

            long dijkstraMemory =
                    Math.max(
                            0,
                            dijkstraMemoryAfter
                            - dijkstraMemoryBefore
                    );

            /*
             * ---------------------------------------------------------
             * BELLMAN-FORD
             * ---------------------------------------------------------
             */
            long bellmanFordTotalTime = 0;

            long bellmanFordMemoryBefore =
                    usedMemory();

            int bellmanFordDistance = 0;

            for (int i = 0; i < MEASUREMENT_RUNS; i++) {

                long startTime =
                        System.nanoTime();

                bellmanFordDistance =
                        bellmanFordDistance(
                                graph,
                                startNode,
                                goalNode
                        );

                long endTime =
                        System.nanoTime();

                bellmanFordTotalTime +=
                        (endTime - startTime);
            }

            long bellmanFordMemoryAfter =
                    usedMemory();

            double bellmanFordAverageTime =
                    bellmanFordTotalTime
                    / (double) MEASUREMENT_RUNS
                    / 1_000_000.0;

            long bellmanFordMemory =
                    Math.max(
                            0,
                            bellmanFordMemoryAfter
                            - bellmanFordMemoryBefore
                    );

            /*
             * ---------------------------------------------------------
             * A*
             * ---------------------------------------------------------
             */
            long aStarTotalTime = 0;

            long aStarMemoryBefore =
                    usedMemory();

            int aStarDistance = 0;

            for (int i = 0; i < MEASUREMENT_RUNS; i++) {

                long startTime =
                        System.nanoTime();

                aStarDistance =
                        aStarDistance(
                                graph,
                                startNode,
                                goalNode
                        );

                long endTime =
                        System.nanoTime();

                aStarTotalTime +=
                        (endTime - startTime);
            }

            long aStarMemoryAfter =
                    usedMemory();

            double aStarAverageTime =
                    aStarTotalTime
                    / (double) MEASUREMENT_RUNS
                    / 1_000_000.0;

            long aStarMemory =
                    Math.max(
                            0,
                            aStarMemoryAfter
                            - aStarMemoryBefore
                    );

            /*
             * ---------------------------------------------------------
             * CORRECTNESS CHECK
             * ---------------------------------------------------------
             *
             * All three algorithms should return the same optimal
             * distance on this graph because all edge weights are
             * non-negative.
             */
            boolean correct =
                    dijkstraDistance == bellmanFordDistance
                    && dijkstraDistance == aStarDistance;

            System.out.printf(
                    "%-10d %-10d %-18.6f %-20.6f %-15.6f %-15s%n",
                    nodes,
                    edgeCount,
                    dijkstraAverageTime,
                    bellmanFordAverageTime,
                    aStarAverageTime,
                    correct ? "PASS" : "FAIL"
            );

            /*
             * Memory results are printed separately to prevent
             * the main table becoming too wide.
             */
            System.out.printf(
                    "   Memory: Dijkstra=%d KB | Bellman-Ford=%d KB | A*=%d KB%n",
                    dijkstraMemory / 1024,
                    bellmanFordMemory / 1024,
                    aStarMemory / 1024
            );

            System.out.printf(
                    "   Shortest distance: %d km%n",
                    dijkstraDistance
            );

            System.out.println(
                    "-----------------------------------------------------------------------------"
            );
        }

        System.out.println(
                "Benchmark completed successfully."
        );

        System.out.println(
                "============================================================================="
        );
    }

    /**
     * Dijkstra benchmark implementation.
     *
     * Returns the shortest distance from startNode to goalNode.
     */
    private static int dijkstraDistance(
            Graph graph,
            int startNode,
            int goalNode) {

        int[] distances =
                new int[graph.nodes];

        Arrays.fill(
                distances,
                Integer.MAX_VALUE
        );

        distances[startNode] = 0;

        java.util.PriorityQueue<int[]> priorityQueue =
                new java.util.PriorityQueue<>(
                        java.util.Comparator.comparingInt(
                                value -> value[1]
                        )
                );

        priorityQueue.add(
                new int[] {
                        startNode,
                        0
                }
        );

        while (!priorityQueue.isEmpty()) {

            int[] current =
                    priorityQueue.poll();

            int currentNode =
                    current[0];

            int currentDistance =
                    current[1];

            if (currentDistance
                    > distances[currentNode]) {

                continue;
            }

            if (currentNode == goalNode) {
                return distances[goalNode];
            }

            for (Edge edge :
                    graph.adjList.get(currentNode)) {

                int newDistance =
                        distances[currentNode]
                        + edge.weight;

                if (newDistance
                        < distances[edge.targetNode]) {

                    distances[edge.targetNode] =
                            newDistance;

                    priorityQueue.add(
                            new int[] {
                                    edge.targetNode,
                                    newDistance
                            }
                    );
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Bellman-Ford benchmark implementation.
     */
    private static int bellmanFordDistance(
            Graph graph,
            int startNode,
            int goalNode) {

        int[] distances =
                new int[graph.nodes];

        Arrays.fill(
                distances,
                Integer.MAX_VALUE
        );

        distances[startNode] = 0;

        /*
         * Relax all edges V-1 times.
         */
        for (int i = 1;
             i < graph.nodes;
             i++) {

            boolean changed = false;

            for (int u = 0;
                 u < graph.nodes;
                 u++) {

                if (distances[u]
                        == Integer.MAX_VALUE) {

                    continue;
                }

                for (Edge edge :
                        graph.adjList.get(u)) {

                    int newDistance =
                            distances[u]
                            + edge.weight;

                    if (newDistance
                            < distances[edge.targetNode]) {

                        distances[edge.targetNode] =
                                newDistance;

                        changed = true;
                    }
                }
            }

            /*
             * Early termination:
             * if no distance changed, the solution has converged.
             */
            if (!changed) {
                break;
            }
        }

        return distances[goalNode];
    }

    /**
     * A* benchmark implementation.
     *
     * The heuristic is the Haversine geographical distance
     * from the current node to the goal node.
     */
    private static int aStarDistance(
            Graph graph,
            int startNode,
            int goalNode) {

        int n = graph.nodes;

        int[] gScore =
                new int[n];

        Arrays.fill(
                gScore,
                Integer.MAX_VALUE
        );

        gScore[startNode] = 0;

        java.util.PriorityQueue<NodeRecord> openSet =
                new java.util.PriorityQueue<>(
                        java.util.Comparator.comparingDouble(
                                record -> record.fScore
                        )
                );

        openSet.add(
                new NodeRecord(
                        startNode,
                        heuristic(
                                graph,
                                startNode,
                                goalNode
                        )
                )
        );

        boolean[] closed =
                new boolean[n];

        while (!openSet.isEmpty()) {

            NodeRecord currentRecord =
                    openSet.poll();

            int current =
                    currentRecord.node;

            if (closed[current]) {
                continue;
            }

            if (current == goalNode) {
                return gScore[goalNode];
            }

            closed[current] = true;

            for (Edge edge :
                    graph.adjList.get(current)) {

                if (closed[edge.targetNode]) {
                    continue;
                }

                int tentativeG =
                        gScore[current]
                        + edge.weight;

                if (tentativeG
                        < gScore[edge.targetNode]) {

                    gScore[edge.targetNode] =
                            tentativeG;

                    double fScore =
                            tentativeG
                            + heuristic(
                                    graph,
                                    edge.targetNode,
                                    goalNode
                            );

                    openSet.add(
                            new NodeRecord(
                                    edge.targetNode,
                                    fScore
                            )
                    );
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    /**
     * A* node record containing the node and f(n) value.
     */
    private static class NodeRecord {

        int node;
        double fScore;

        NodeRecord(
                int node,
                double fScore) {

            this.node = node;
            this.fScore = fScore;
        }
    }

    /**
     * Haversine heuristic.
     *
     * Estimates the straight-line geographical distance
     * from node to goal in kilometres.
     */
    private static double heuristic(
            Graph graph,
            int node,
            int goal) {

        return haversineDistance(
                graph.getLatitude(node),
                graph.getLongitude(node),
                graph.getLatitude(goal),
                graph.getLongitude(goal)
        );
    }

    /**
     * Haversine distance calculation.
     */
    private static double haversineDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double EARTH_RADIUS_KM =
                6371.0;

        double latitude1 =
                Math.toRadians(lat1);

        double latitude2 =
                Math.toRadians(lat2);

        double deltaLatitude =
                Math.toRadians(
                        lat2 - lat1
                );

        double deltaLongitude =
                Math.toRadians(
                        lon2 - lon1
                );

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

    /**
     * Counts undirected edges.
     *
     * Graph.addEdge() stores every edge twice,
     * therefore divide the adjacency count by 2.
     */
    private static int countUndirectedEdges(
            Graph graph) {

        int total = 0;

        for (int i = 0;
             i < graph.nodes;
             i++) {

            total +=
                    graph.adjList
                            .get(i)
                            .size();
        }

        return total / 2;
    }

    /**
     * Returns approximate JVM used memory.
     *
     * Memory measurements in a managed JVM are approximate
     * because garbage collection and JVM memory allocation
     * are handled automatically.
     */
    private static long usedMemory() {

        Runtime runtime =
                Runtime.getRuntime();

        return runtime.totalMemory()
                - runtime.freeMemory();
    }
}
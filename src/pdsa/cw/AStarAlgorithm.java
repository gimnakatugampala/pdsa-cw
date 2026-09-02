package pdsa.cw;

import java.util.*;

/**
 * A* shortest-path algorithm.
 *
 * A* uses:
 *     f(n) = g(n) + h(n)
 *
 * where:
 *     g(n) = cost from the start node to node n
 *     h(n) = estimated cost from node n to the goal
 *
 * The heuristic is calculated using the Haversine distance
 * between geographical coordinates stored in the Graph.
 */
public class AStarAlgorithm {

    /**
     * Calculates and displays the optimal route from startNode
     * to goalNode.
     */
    public void findShortestPath(
            Graph graph,
            int startNode,
            int goalNode,
            String[] cityNames,
            double[] heuristic) {

        validateInput(
                graph,
                startNode,
                goalNode,
                cityNames,
                heuristic
        );

        int numberOfNodes = graph.nodes;

        int[] gScore =
                new int[numberOfNodes];

        int[] previousNode =
                new int[numberOfNodes];

        Arrays.fill(
                gScore,
                Integer.MAX_VALUE
        );

        Arrays.fill(
                previousNode,
                -1
        );

        /*
         * Cost of reaching start node is 0.
         */
        gScore[startNode] = 0;

        /*
         * Priority queue stores nodes according to
         * their f(n) = g(n) + h(n).
         */
        PriorityQueue<NodeRecord> openSet =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                record -> record.fScore
                        )
                );

        openSet.add(
                new NodeRecord(
                        startNode,
                        heuristic[startNode]
                )
        );

        boolean[] closed =
                new boolean[numberOfNodes];

        /*
         * ---------------------------------------------------------
         * MAIN A* SEARCH
         * ---------------------------------------------------------
         */
        while (!openSet.isEmpty()) {

            NodeRecord currentRecord =
                    openSet.poll();

            int current =
                    currentRecord.node;

            /*
             * Ignore nodes already processed.
             */
            if (closed[current]) {
                continue;
            }

            /*
             * Goal reached.
             */
            if (current == goalNode) {

                printPath(
                        startNode,
                        goalNode,
                        gScore,
                        previousNode,
                        cityNames
                );

                return;
            }

            closed[current] = true;

            /*
             * Examine neighbouring nodes.
             */
            for (Edge edge :
                    graph.adjList.get(current)) {

                if (edge.weight < 0) {
                    throw new IllegalArgumentException(
                            "A* requires non-negative edge weights."
                    );
                }

                int neighbour =
                        edge.targetNode;

                if (closed[neighbour]) {
                    continue;
                }

                /*
                 * g(current) + edge cost
                 */
                int tentativeGScore =
                        gScore[current]
                        + edge.weight;

                /*
                 * If this is a better route to the neighbour,
                 * update it.
                 */
                if (tentativeGScore
                        < gScore[neighbour]) {

                    gScore[neighbour] =
                            tentativeGScore;

                    previousNode[neighbour] =
                            current;

                    double fScore =
                            tentativeGScore
                            + heuristic[neighbour];

                    openSet.add(
                            new NodeRecord(
                                    neighbour,
                                    fScore
                            )
                    );
                }
            }
        }

        /*
         * No route was found.
         */
        System.out.println(
                "\n--- A* Route Result ---"
        );

        System.out.println(
                "No route found from "
                + cityNames[startNode]
                + " to "
                + cityNames[goalNode]
                + "."
        );
    }

    /**
     * Prints the reconstructed shortest route.
     */
    private void printPath(
            int startNode,
            int goalNode,
            int[] gScore,
            int[] previousNode,
            String[] cityNames) {

        List<Integer> path =
                new ArrayList<>();

        int current =
                goalNode;

        /*
         * Trace backwards from the destination.
         */
        while (current != -1) {

            path.add(current);

            if (current == startNode) {
                break;
            }

            current =
                    previousNode[current];
        }

        /*
         * If the start node was not reached,
         * the path is invalid.
         */
        if (path.get(path.size() - 1)
                != startNode) {

            System.out.println(
                    "\n--- A* Route Result ---"
            );

            System.out.println(
                    "No valid route was found."
            );

            return;
        }

        /*
         * Reverse so that the route is displayed
         * from start -> destination.
         */
        Collections.reverse(path);

        System.out.println(
                "\n--- A* Optimal Route ---"
        );

        System.out.println(
                "From: "
                + cityNames[startNode]
        );

        System.out.println(
                "To: "
                + cityNames[goalNode]
        );

        System.out.println(
                "Distance: "
                + gScore[goalNode]
                + " km"
        );

        System.out.print(
                "Route: "
        );

        for (int i = 0;
             i < path.size();
             i++) {

            System.out.print(
                    cityNames[path.get(i)]
            );

            if (i < path.size() - 1) {
                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();
    }

    /**
     * Validates the input supplied to A*.
     */
    private void validateInput(
            Graph graph,
            int startNode,
            int goalNode,
            String[] cityNames,
            double[] heuristic) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null."
            );
        }

        if (startNode < 0
                || startNode >= graph.nodes) {

            throw new IndexOutOfBoundsException(
                    "Invalid start node."
            );
        }

        if (goalNode < 0
                || goalNode >= graph.nodes) {

            throw new IndexOutOfBoundsException(
                    "Invalid goal node."
            );
        }

        if (cityNames == null
                || cityNames.length != graph.nodes) {

            throw new IllegalArgumentException(
                    "City names must contain one value per graph node."
            );
        }

        if (heuristic == null
                || heuristic.length != graph.nodes) {

            throw new IllegalArgumentException(
                    "Heuristic array must contain one value per graph node."
            );
        }

        for (double value : heuristic) {

            if (value < 0) {

                throw new IllegalArgumentException(
                        "A* heuristic values cannot be negative."
                );
            }
        }
    }

    /**
     * Record used by the A* priority queue.
     */
    private static class NodeRecord {

        final int node;
        final double fScore;

        NodeRecord(
                int node,
                double fScore) {

            this.node = node;
            this.fScore = fScore;
        }
    }
}
package pdsa.cw;

import java.util.Scanner;

public class PDSACW {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("    INTELLIGENT DECISION SUPPORT SYSTEM (IDSS)   ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\nMain Menu - Select a Module to Execute:");
            System.out.println("1. Task 1: Route Optimization (Dijkstra vs Bellman-Ford vs A*)");
            System.out.println("2. Task 2: Resource Allocation (Knapsack DP vs Greedy)");
            System.out.println("3. Task 3: Network Analysis (MST Prim vs Kruskal)");
            System.out.println("4. Task 4: Intelligent Decision (WSM vs TOPSIS)");
            System.out.println("5. Task 5: Optimization Module (TSP Exact vs Heuristic)");
            System.out.println("0. Exit System");
            System.out.print("\nEnter your choice (0-5): ");

            int choice = scanner.nextInt();
            System.out.println("\n-------------------------------------------------");

            switch (choice) {
                case 1:
                    runTask1();
                    break;
                case 2:
                    runTask2();
                    break;
                case 3:
                    runTask3();
                    break;
                case 4:
                    runTask4();
                    break;
                case 5:
                    runTask5();
                    break;
                case 0:
                    System.out.println("Exiting System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 0 and 5.");
            }
            System.out.println("-------------------------------------------------");
        }
        scanner.close();
    }

    // ==========================================
    // MODULE EXECUTION METHODS
    // ==========================================

    private static void runTask1() {

    int numberOfCities = 5;

    Graph routeGraph = new Graph(numberOfCities);

    String[] cities = {
        "Colombo",
        "Kurunegala",
        "Kandy",
        "Dambulla",
        "Anuradhapura"
    };

    routeGraph.addEdge(0, 1, 95);
    routeGraph.addEdge(0, 2, 115);
    routeGraph.addEdge(1, 3, 55);
    routeGraph.addEdge(2, 3, 72);
    routeGraph.addEdge(3, 4, 65);
    routeGraph.addEdge(1, 4, 110);

    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
    BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm();
    AStarAlgorithm aStar = new AStarAlgorithm();

    System.out.println("========== ROUTE OPTIMIZATION ==========");

    // -----------------------------
    // DIJKSTRA
    // -----------------------------
    long startDijkstra = System.nanoTime();

    dijkstra.findShortestPath(
            routeGraph,
            0,
            cities
    );

    long endDijkstra = System.nanoTime();

    System.out.println(
            "Dijkstra Time: "
            + (endDijkstra - startDijkstra) / 1_000_000.0
            + " ms\n"
    );

    // -----------------------------
    // BELLMAN-FORD
    // -----------------------------
    long startBellmanFord = System.nanoTime();

    bellmanFord.findShortestPath(
            routeGraph,
            0,
            cities
    );

    long endBellmanFord = System.nanoTime();

    System.out.println(
            "Bellman-Ford Time: "
            + (endBellmanFord - startBellmanFord) / 1_000_000.0
            + " ms\n"
    );

    // -----------------------------
    // A*
    // -----------------------------
    /*
     * Heuristic values must be admissible.
     * For this small demonstration graph,
     * zero heuristic makes A* behave like
     * Dijkstra while still demonstrating
     * the A* algorithm structure.
     */
    double[] heuristic = {
        0,
        0,
        0,
        0,
        0
    };

    long startAStar = System.nanoTime();

    aStar.findShortestPath(
            routeGraph,
            0,
            4,
            cities,
            heuristic
    );

    long endAStar = System.nanoTime();

    System.out.println(
            "A* Time: "
            + (endAStar - startAStar) / 1_000_000.0
            + " ms\n"
    );

    System.out.println("========================================");
}

    private static void runTask2() {
        System.out.println("--- Task 2: Intelligent Resource Allocation ---");
    
        CargoItem[] cargo = {
            new CargoItem("BoxA", 10, 60),
            new CargoItem("BoxB", 20, 100),
            new CargoItem("BoxC", 30, 120),
            new CargoItem("BoxD", 15, 75)
        };

        int truckCapacity = 50;
        ResourceAllocation allocator = new ResourceAllocation();

        System.out.println("Evaluating DP (Exact)...");
        allocator.allocateExactDP(cargo, truckCapacity);

        System.out.println("Evaluating Greedy (Heuristic)...");
        allocator.allocateHeuristicGreedy(cargo.clone(), truckCapacity);
    }

    private static void runTask3() {
        System.out.println("--- Task 3: Network Analysis (MST) ---");
        
        int numberOfCities = 5;
        Graph routeGraph = new Graph(numberOfCities);
        String[] cities = {"Colombo", "Kurunegala", "Kandy", "Dambulla", "Anuradhapura"};
        routeGraph.addEdge(0, 1, 95);  
        routeGraph.addEdge(0, 2, 115); 
        routeGraph.addEdge(1, 3, 55);  
        routeGraph.addEdge(2, 3, 72);  
        routeGraph.addEdge(3, 4, 65);  
        routeGraph.addEdge(1, 4, 110); 

        NetworkAnalysis networkAnalyzer = new NetworkAnalysis();
        
        long startPrim = System.nanoTime();
        networkAnalyzer.analyzeWithPrims(routeGraph, cities);
        long endPrim = System.nanoTime();
        System.out.println("Prim's Execution Time: " + (endPrim - startPrim) / 1_000_000.0 + " ms\n");

        long startKruskal = System.nanoTime();
        networkAnalyzer.analyzeWithKruskals(routeGraph, cities);
        long endKruskal = System.nanoTime();
        System.out.println("Kruskal's Execution Time: " + (endKruskal - startKruskal) / 1_000_000.0 + " ms\n");
    }

    private static void runTask4() {
        System.out.println("--- Task 4: Intelligent Decision Module ---");
        Supplier[] suppliers = {
            new Supplier("Supplier A (Local)", 5000, 85, 2),
            new Supplier("Supplier B (Premium)", 8000, 98, 1),
            new Supplier("Supplier C (Budget)", 3500, 70, 5),
            new Supplier("Supplier D (Standard)", 5500, 88, 3)
        };
        
        double[] criteriaWeights = {0.4, 0.4, 0.2};
        DecisionModule decisionModule = new DecisionModule();
        
        long startWSM = System.nanoTime();
        decisionModule.rankWithWSM(suppliers.clone(), criteriaWeights);
        long endWSM = System.nanoTime();
        System.out.println("WSM Execution Time: " + (endWSM - startWSM) / 1_000_000.0 + " ms\n");

        long startTOPSIS = System.nanoTime();
        decisionModule.rankWithTOPSIS(suppliers.clone(), criteriaWeights);
        long endTOPSIS = System.nanoTime();
        System.out.println("TOPSIS Execution Time: " + (endTOPSIS - startTOPSIS) / 1_000_000.0 + " ms\n");
    }

    private static void runTask5() {
        System.out.println("--- Task 5: Optimization Module (TSP) ---");
        int[][] tspGraph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        
        OptimizationModule optModule = new OptimizationModule();
        
        long startTSPExact = System.nanoTime();
        optModule.solveExactTSP(tspGraph);
        long endTSPExact = System.nanoTime();
        System.out.println("Exact TSP Execution Time: " + (endTSPExact - startTSPExact) / 1_000_000.0 + " ms\n");

        long startTSPHeuristic = System.nanoTime();
        optModule.solveHeuristicTSP(tspGraph);
        long endTSPHeuristic = System.nanoTime();
        System.out.println("Heuristic TSP Execution Time: " + (endTSPHeuristic - startTSPHeuristic) / 1_000_000.0 + " ms\n");
    }
}
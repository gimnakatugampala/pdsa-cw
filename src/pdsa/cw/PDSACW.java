package pdsa.cw;

import java.util.*;

public class PDSACW {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        if (args.length > 0) {
            String mode = args[0].toLowerCase(Locale.ROOT);

            if (mode.equals("benchmark")) {
                new BenchmarkRunner().runAll();
                return;
            }

            if (mode.equals("demo")) {
                runAllDemonstrations();
                return;
            }

            if (mode.equals("all")) {
                runAllDemonstrations();
                System.out.println();
                new BenchmarkRunner().runAll();
                return;
            }
        }

        runMenu();
    }

    // ============================================================
    // MAIN MENU
    // ============================================================

    private static void runMenu() {

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        task1Menu();
                        break;

                    case 2:
                        task2Menu();
                        break;

                    case 3:
                        task3Menu();
                        break;

                    case 4:
                        task4Menu();
                        break;

                    case 5:
                        task5Menu();
                        break;

                    case 6:
                        runAllDemonstrations();
                        pause();
                        break;

                    case 7:
                        System.out.println();
                        System.out.println("Running benchmark suite...");
                        System.out.println();

                        new BenchmarkRunner().runAll();

                        pause();
                        break;

                    case 0:
                        running = false;
                        System.out.println();
                        System.out.println("Exiting PDSA Intelligent Decision Support System...");
                        break;

                    default:
                        System.out.println();
                        System.out.println("Invalid option. Please select a valid menu item.");
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                System.out.println();

                pause();
            }
        }
    }

    private static void printMainMenu() {

        clearSection();

        System.out.println("============================================================");
        System.out.println("        PDSA INTELLIGENT DECISION SUPPORT SYSTEM");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("1. Intelligent Route Optimization");
        System.out.println("2. Intelligent Resource Allocation");
        System.out.println("3. Network Analysis");
        System.out.println("4. Intelligent Decision");
        System.out.println("5. Optimization");
        System.out.println();
        System.out.println("6. Run All Task Demonstrations");
        System.out.println("7. Run Performance Benchmarking");
        System.out.println();
        System.out.println("0. Exit");
        System.out.println();
    }

    // ============================================================
    // TASK 1 MENU
    // ============================================================

    private static void task1Menu() {

        boolean back = false;

        while (!back) {

            clearSection();

            System.out.println("============================================================");
            System.out.println("TASK 1 - INTELLIGENT ROUTE OPTIMIZATION");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Run Dijkstra");
            System.out.println("2. Run Bellman-Ford");
            System.out.println("3. Run A*");
            System.out.println("4. Compare All Algorithms");
            System.out.println("5. Display Route Information");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        runDijkstra();
                        pause();
                        break;

                    case 2:
                        runBellmanFord();
                        pause();
                        break;

                    case 3:
                        runAStar();
                        pause();
                        break;

                    case 4:
                        runTask1Comparison();
                        pause();
                        break;

                    case 5:
                        showTask1Data();
                        pause();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        pause();
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                pause();
            }
        }
    }

    private static Graph createTask1Graph() {

        return GraphGenerator.connectedGeographicGraph(
                6,
                5,
                101
        );
    }

    private static String[] getTask1Names() {

        return new String[]{
            "Colombo",
            "Kelaniya",
            "Kadawatha",
            "Gampaha",
            "Nittambuwa",
            "Kegalle"
        };
    }

    private static void runDijkstra() {

        Graph graph = createTask1Graph();
        String[] names = getTask1Names();

        DijkstraAlgorithm algorithm = new DijkstraAlgorithm();

        PathResult result =
                algorithm.findPath(graph, 0, names.length - 1);

        result.print(
                "Task 1 - Dijkstra",
                names
        );
    }

    private static void runBellmanFord() {

        Graph graph = createTask1Graph();
        String[] names = getTask1Names();

        BellmanFordAlgorithm algorithm =
                new BellmanFordAlgorithm();

        PathResult result =
                algorithm.findPath(graph, 0, names.length - 1);

        result.print(
                "Task 1 - Bellman-Ford",
                names
        );
    }

    private static void runAStar() {

        Graph graph = createTask1Graph();
        String[] names = getTask1Names();

        AStarAlgorithm algorithm =
                new AStarAlgorithm();

        PathResult result =
                algorithm.findPath(graph, 0, names.length - 1);

        result.print(
                "Task 1 - A* with Haversine heuristic",
                names
        );
    }

    private static void runTask1Comparison() {

        Graph graph = createTask1Graph();
        String[] names = getTask1Names();

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        BellmanFordAlgorithm bellmanFord =
                new BellmanFordAlgorithm();

        AStarAlgorithm aStar =
                new AStarAlgorithm();

        PathResult r1 =
                dijkstra.findPath(graph, 0, names.length - 1);

        PathResult r2 =
                bellmanFord.findPath(graph, 0, names.length - 1);

        PathResult r3 =
                aStar.findPath(graph, 0, names.length - 1);

        r1.print("Dijkstra", names);
        r2.print("Bellman-Ford", names);
        r3.print("A* with Haversine heuristic", names);

        System.out.println();
        System.out.println("============================================================");
        System.out.println("ALGORITHM COMPARISON");
        System.out.println("============================================================");
        System.out.println("Dijkstra distance       : " + r1.distance + " km");
        System.out.println("Bellman-Ford distance   : " + r2.distance + " km");
        System.out.println("A* distance             : " + r3.distance + " km");

        if (r1.distance == r2.distance &&
            r1.distance == r3.distance) {

            System.out.println();
            System.out.println("Correctness Check: PASS");
            System.out.println("All three algorithms produced the same shortest distance.");

        } else {

            System.out.println();
            System.out.println("Correctness Check: FAIL");
            System.out.println("Algorithms produced different results.");
        }
    }

    private static void showTask1Data() {

        System.out.println();
        System.out.println("TASK 1 TEST NETWORK");
        System.out.println("-------------------");

        String[] names = getTask1Names();

        for (int i = 0; i < names.length; i++) {

            System.out.println(
                    i + " -> " + names[i]
            );
        }

        System.out.println();
        System.out.println("Start Node : Colombo");
        System.out.println("Goal Node  : Kegalle");
    }

    // ============================================================
    // TASK 2 MENU
    // ============================================================

    private static void task2Menu() {

        boolean back = false;

        while (!back) {

            clearSection();

            System.out.println("============================================================");
            System.out.println("TASK 2 - INTELLIGENT RESOURCE ALLOCATION");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Dynamic Programming");
            System.out.println("2. Greedy");
            System.out.println("3. Branch and Bound");
            System.out.println("4. Compare All Algorithms");
            System.out.println("5. Display Available Resources");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        runDynamicProgramming();
                        pause();
                        break;

                    case 2:
                        runGreedy();
                        pause();
                        break;

                    case 3:
                        runBranchAndBound();
                        pause();
                        break;

                    case 4:
                        runTask2Comparison();
                        pause();
                        break;

                    case 5:
                        showTask2Data();
                        pause();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        pause();
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                pause();
            }
        }
    }

    private static List<CargoItem> createTask2Items() {

        return Arrays.asList(

                new CargoItem(
                        "Medical Supplies",
                        10,
                        60
                ),

                new CargoItem(
                        "Food",
                        20,
                        100
                ),

                new CargoItem(
                        "Water",
                        15,
                        70
                ),

                new CargoItem(
                        "Equipment",
                        25,
                        120
                ),

                new CargoItem(
                        "Shelter",
                        18,
                        95
                ),

                new CargoItem(
                        "Power Units",
                        12,
                        75
                )
        );
    }

    private static int getTask2Capacity() {
        return 50;
    }

    private static void runDynamicProgramming() {

        ResourceAllocation algorithm =
                new ResourceAllocation();

        ResourceAllocation.AllocationResult result =
                algorithm.dynamicProgramming(
                        createTask2Items(),
                        getTask2Capacity()
                );

        result.print();
    }

    private static void runGreedy() {

        ResourceAllocation algorithm =
                new ResourceAllocation();

        ResourceAllocation.AllocationResult result =
                algorithm.greedy(
                        createTask2Items(),
                        getTask2Capacity()
                );

        result.print();
    }

    private static void runBranchAndBound() {

        ResourceAllocation algorithm =
                new ResourceAllocation();

        ResourceAllocation.AllocationResult result =
                algorithm.branchAndBound(
                        createTask2Items(),
                        getTask2Capacity()
                );

        result.print();
    }

    private static void runTask2Comparison() {

        ResourceAllocation algorithm =
                new ResourceAllocation();

        int capacity = getTask2Capacity();

        ResourceAllocation.AllocationResult dp =
                algorithm.dynamicProgramming(
                        createTask2Items(),
                        capacity
                );

        ResourceAllocation.AllocationResult greedy =
                algorithm.greedy(
                        createTask2Items(),
                        capacity
                );

        ResourceAllocation.AllocationResult bnb =
                algorithm.branchAndBound(
                        createTask2Items(),
                        capacity
                );

        dp.print();
        greedy.print();
        bnb.print();

        System.out.println();
        System.out.println("============================================================");
        System.out.println("RESOURCE ALLOCATION COMPARISON");
        System.out.println("============================================================");

        System.out.println(
                "Dynamic Programming : " + dp.value
        );

        System.out.println(
                "Greedy              : " + greedy.value
        );

        System.out.println(
                "Branch and Bound    : " + bnb.value
        );

        System.out.println();

        if (dp.value == bnb.value) {

            System.out.println(
                    "Correctness Check: PASS"
            );

            System.out.println(
                    "Branch and Bound agrees with the DP optimum."
            );

        } else {

            System.out.println(
                    "Correctness Check: FAIL"
            );
        }
    }

    private static void showTask2Data() {

        List<CargoItem> items =
                createTask2Items();

        System.out.println();
        System.out.println("AVAILABLE RESOURCES");
        System.out.println("-------------------");
        System.out.println("Capacity: " + getTask2Capacity());
        System.out.println();

        for (CargoItem item : items) {
            System.out.println(item);
        }
    }

    // ============================================================
    // TASK 3 MENU
    // ============================================================

    private static void task3Menu() {

        boolean back = false;

        while (!back) {

            clearSection();

            System.out.println("============================================================");
            System.out.println("TASK 3 - NETWORK ANALYSIS");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Prim");
            System.out.println("2. Kruskal");
            System.out.println("3. Boruvka");
            System.out.println("4. Compare All Algorithms");
            System.out.println("5. Display Network Information");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        runPrim();
                        pause();
                        break;

                    case 2:
                        runKruskal();
                        pause();
                        break;

                    case 3:
                        runBoruvka();
                        pause();
                        break;

                    case 4:
                        runTask3Comparison();
                        pause();
                        break;

                    case 5:
                        showTask3Data();
                        pause();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        pause();
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                pause();
            }
        }
    }

    private static Graph createTask3Graph() {

        return GraphGenerator.connectedGeographicGraph(
                7,
                8,
                303
        );
    }

    private static void runPrim() {

        NetworkAnalysis algorithm =
                new NetworkAnalysis();

        NetworkAnalysis.MSTResult result =
                algorithm.prim(
                        createTask3Graph()
                );

        result.print();
    }

    private static void runKruskal() {

        NetworkAnalysis algorithm =
                new NetworkAnalysis();

        NetworkAnalysis.MSTResult result =
                algorithm.kruskal(
                        createTask3Graph()
                );

        result.print();
    }

    private static void runBoruvka() {

        NetworkAnalysis algorithm =
                new NetworkAnalysis();

        NetworkAnalysis.MSTResult result =
                algorithm.boruvka(
                        createTask3Graph()
                );

        result.print();
    }

    private static void runTask3Comparison() {

        NetworkAnalysis algorithm =
                new NetworkAnalysis();

        Graph graph = createTask3Graph();

        NetworkAnalysis.MSTResult prim =
                algorithm.prim(graph);

        NetworkAnalysis.MSTResult kruskal =
                algorithm.kruskal(graph);

        NetworkAnalysis.MSTResult boruvka =
                algorithm.boruvka(graph);

        prim.print();
        kruskal.print();
        boruvka.print();

        System.out.println();
        System.out.println("============================================================");
        System.out.println("MST ALGORITHM COMPARISON");
        System.out.println("============================================================");

        System.out.println(
                "Prim     : " + prim.totalWeight
        );

        System.out.println(
                "Kruskal  : " + kruskal.totalWeight
        );

        System.out.println(
                "Boruvka  : " + boruvka.totalWeight
        );

        System.out.println();

        if (prim.totalWeight == kruskal.totalWeight &&
            prim.totalWeight == boruvka.totalWeight) {

            System.out.println(
                    "Correctness Check: PASS"
            );

            System.out.println(
                    "All MST algorithms produced the same total weight."
            );

        } else {

            System.out.println(
                    "Correctness Check: FAIL"
            );
        }
    }

    private static void showTask3Data() {

        Graph graph = createTask3Graph();

        System.out.println();
        System.out.println("NETWORK INFORMATION");
        System.out.println("-------------------");
        System.out.println("Nodes: " + graph.nodes);
        System.out.println("Network generated using fixed seed: 303");
        System.out.println("Graph is connected.");
    }

    // ============================================================
    // TASK 4 MENU
    // ============================================================

    private static void task4Menu() {

        boolean back = false;

        while (!back) {

            clearSection();

            System.out.println("============================================================");
            System.out.println("TASK 4 - INTELLIGENT DECISION");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Weighted Sum Model (WSM)");
            System.out.println("2. TOPSIS");
            System.out.println("3. Weighted Product Model (WPM)");
            System.out.println("4. Compare All Rankings");
            System.out.println("5. Display Supplier Data");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        runWSM();
                        pause();
                        break;

                    case 2:
                        runTOPSIS();
                        pause();
                        break;

                    case 3:
                        runWPM();
                        pause();
                        break;

                    case 4:
                        runTask4Comparison();
                        pause();
                        break;

                    case 5:
                        showTask4Data();
                        pause();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        pause();
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                pause();
            }
        }
    }

    private static List<Supplier> createTask4Suppliers() {

        return Arrays.asList(

                new Supplier(
                        "Supplier A",
                        500,
                        0.95,
                        5
                ),

                new Supplier(
                        "Supplier B",
                        420,
                        0.90,
                        8
                ),

                new Supplier(
                        "Supplier C",
                        650,
                        0.98,
                        4
                ),

                new Supplier(
                        "Supplier D",
                        390,
                        0.82,
                        10
                ),

                new Supplier(
                        "Supplier E",
                        470,
                        0.93,
                        6
                )
        );
    }

    private static double[] getTask4Weights() {

        return new double[]{
            0.40,
            0.40,
            0.20
        };
    }

    private static void runWSM() {

        DecisionModule algorithm =
                new DecisionModule();

        List<DecisionModule.ScoredSupplier> results =
                algorithm.weightedSumModel(
                        createTask4Suppliers(),
                        getTask4Weights()
                );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("WEIGHTED SUM MODEL (WSM)");
        System.out.println("============================================================");

        for (DecisionModule.ScoredSupplier result : results) {
            System.out.println(result);
        }
    }

    private static void runTOPSIS() {

        DecisionModule algorithm =
                new DecisionModule();

        List<DecisionModule.ScoredSupplier> results =
                algorithm.topsis(
                        createTask4Suppliers(),
                        getTask4Weights()
                );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("TOPSIS");
        System.out.println("============================================================");

        for (DecisionModule.ScoredSupplier result : results) {
            System.out.println(result);
        }
    }

    private static void runWPM() {

        DecisionModule algorithm =
                new DecisionModule();

        List<DecisionModule.ScoredSupplier> results =
                algorithm.weightedProductModel(
                        createTask4Suppliers(),
                        getTask4Weights()
                );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("WEIGHTED PRODUCT MODEL");
        System.out.println("============================================================");

        for (DecisionModule.ScoredSupplier result : results) {
            System.out.println(result);
        }
    }

    private static void runTask4Comparison() {

        DecisionModule algorithm =
                new DecisionModule();

        List<Supplier> suppliers =
                createTask4Suppliers();

        double[] weights =
                getTask4Weights();

        List<DecisionModule.ScoredSupplier> wsm =
                algorithm.weightedSumModel(
                        suppliers,
                        weights
                );

        List<DecisionModule.ScoredSupplier> topsis =
                algorithm.topsis(
                        suppliers,
                        weights
                );

        List<DecisionModule.ScoredSupplier> wpm =
                algorithm.weightedProductModel(
                        suppliers,
                        weights
                );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("DECISION ALGORITHM COMPARISON");
        System.out.println("============================================================");

        System.out.println();
        System.out.println("WSM");
        for (DecisionModule.ScoredSupplier result : wsm) {
            System.out.println(result);
        }

        System.out.println();
        System.out.println("TOPSIS");
        for (DecisionModule.ScoredSupplier result : topsis) {
            System.out.println(result);
        }

        System.out.println();
        System.out.println("WPM");
        for (DecisionModule.ScoredSupplier result : wpm) {
            System.out.println(result);
        }
    }

    private static void showTask4Data() {

        System.out.println();
        System.out.println("SUPPLIER DATA");
        System.out.println("-------------");
        System.out.println(
                "Criteria: Cost, Reliability, Delivery Time"
        );

        System.out.println();

        for (Supplier supplier : createTask4Suppliers()) {
            System.out.println(
                    supplier.name
                    + " | Cost=" + supplier.cost
                    + " | Reliability=" + supplier.reliability
                    + " | Delivery Time=" + supplier.deliveryTime
            );
        }

        System.out.println();
        System.out.println(
                "Weights: Cost=0.40, Reliability=0.40, Delivery Time=0.20"
        );
    }

    // ============================================================
    // TASK 5 MENU
    // ============================================================

    private static void task5Menu() {

        boolean back = false;

        while (!back) {

            clearSection();

            System.out.println("============================================================");
            System.out.println("TASK 5 - OPTIMIZATION");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Exact Enumeration");
            System.out.println("2. Nearest Neighbor");
            System.out.println("3. 2-opt");
            System.out.println("4. Compare All Algorithms");
            System.out.println("5. Display TSP Information");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();

            int choice = readInt("Select an option: ");

            try {

                switch (choice) {

                    case 1:
                        runExactTsp();
                        pause();
                        break;

                    case 2:
                        runNearestNeighbor();
                        pause();
                        break;

                    case 3:
                        runTwoOpt();
                        pause();
                        break;

                    case 4:
                        runTask5Comparison();
                        pause();
                        break;

                    case 5:
                        showTask5Data();
                        pause();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        pause();
                }

            } catch (Exception e) {

                System.out.println();
                System.out.println("ERROR: " + e.getMessage());
                pause();
            }
        }
    }

    private static List<List<Integer>> createTask5Matrix() {

        return GraphGenerator.randomTspMatrix(
                8,
                505
        );
    }

    private static String[] getTask5Names() {

        return new String[]{
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H"
        };
    }

    private static void runExactTsp() {

        OptimizationModule algorithm =
                new OptimizationModule();

        OptimizationModule.TspResult result =
                algorithm.exact(
                        createTask5Matrix()
                );

        result.print(
                getTask5Names()
        );
    }

    private static void runNearestNeighbor() {

        OptimizationModule algorithm =
                new OptimizationModule();

        OptimizationModule.TspResult result =
                algorithm.nearestNeighbor(
                        createTask5Matrix()
                );

        result.print(
                getTask5Names()
        );
    }

    private static void runTwoOpt() {

        OptimizationModule algorithm =
                new OptimizationModule();

        OptimizationModule.TspResult result =
                algorithm.twoOpt(
                        createTask5Matrix()
                );

        result.print(
                getTask5Names()
        );
    }

    private static void runTask5Comparison() {

        OptimizationModule algorithm =
                new OptimizationModule();

        List<List<Integer>> matrix =
                createTask5Matrix();

        String[] names =
                getTask5Names();

        OptimizationModule.TspResult exact =
                algorithm.exact(matrix);

        OptimizationModule.TspResult nn =
                algorithm.nearestNeighbor(matrix);

        OptimizationModule.TspResult twoOpt =
                algorithm.twoOpt(matrix);

        exact.print(names);
        nn.print(names);
        twoOpt.print(names);

        System.out.println();
        System.out.println("============================================================");
        System.out.println("TSP ALGORITHM COMPARISON");
        System.out.println("============================================================");

        System.out.println(
                "Exact Enumeration : " + exact.cost + " km"
        );

        System.out.println(
                "Nearest Neighbor  : " + nn.cost + " km"
        );

        System.out.println(
                "2-opt              : " + twoOpt.cost + " km"
        );

        System.out.println();

        double nnGap =
                ((double) nn.cost - exact.cost)
                / exact.cost * 100.0;

        double twoOptGap =
                ((double) twoOpt.cost - exact.cost)
                / exact.cost * 100.0;

        System.out.printf(
                Locale.US,
                "Nearest Neighbor gap from optimum : %.2f%%%n",
                nnGap
        );

        System.out.printf(
                Locale.US,
                "2-opt gap from optimum             : %.2f%%%n",
                twoOptGap
        );

        System.out.println();

        if (twoOpt.cost <= nn.cost) {

            System.out.println(
                    "Improvement Check: PASS"
            );

            System.out.println(
                    "2-opt improved or maintained the Nearest Neighbor solution."
            );

        } else {

            System.out.println(
                    "Improvement Check: FAIL"
            );
        }

        System.out.println();

        if (twoOpt.cost == exact.cost) {

            System.out.println(
                    "Optimality Check: 2-opt reached the exact solution on this dataset."
            );
        }
    }

    private static void showTask5Data() {

        System.out.println();
        System.out.println("TSP DATASET");
        System.out.println("-----------");

        String[] names =
                getTask5Names();

        System.out.println(
                "Number of cities: " + names.length
        );

        System.out.println(
                "Starting city: " + names[0]
        );

        System.out.println(
                "Dataset seed: 505"
        );

        System.out.println();
        System.out.println(
                "Cities: " + String.join(", ", names)
        );
    }

    // ============================================================
    // RUN ALL DEMONSTRATIONS
    // ============================================================

    private static void runAllDemonstrations() {

        System.out.println();
        System.out.println("============================================================");
        System.out.println("PDSA INTELLIGENT DECISION SUPPORT SYSTEM");
        System.out.println("FULL DEMONSTRATION");
        System.out.println("============================================================");

        System.out.println();
        System.out.println("TASK 1 - ROUTE OPTIMIZATION");
        System.out.println("------------------------------------------------------------");

        runTask1Comparison();

        System.out.println();
        System.out.println("TASK 2 - RESOURCE ALLOCATION");
        System.out.println("------------------------------------------------------------");

        runTask2Comparison();

        System.out.println();
        System.out.println("TASK 3 - NETWORK ANALYSIS");
        System.out.println("------------------------------------------------------------");

        runTask3Comparison();

        System.out.println();
        System.out.println("TASK 4 - INTELLIGENT DECISION");
        System.out.println("------------------------------------------------------------");

        runTask4Comparison();

        System.out.println();
        System.out.println("TASK 5 - OPTIMIZATION");
        System.out.println("------------------------------------------------------------");

        runTask5Comparison();

        System.out.println();
        System.out.println("============================================================");
        System.out.println("FULL DEMONSTRATION COMPLETE");
        System.out.println("============================================================");
    }

    // ============================================================
    // INPUT / UI HELPERS
    // ============================================================

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static void pause() {

        System.out.println();
        System.out.println(
                "Press Enter to continue..."
        );

        scanner.nextLine();
    }

    private static void clearSection() {

        System.out.println();
    }
}
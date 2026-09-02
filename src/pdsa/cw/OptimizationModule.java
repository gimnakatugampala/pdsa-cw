package pdsa.cw;

public class OptimizationModule {
    private int minCost;
    private int[] bestPath;

    // --- APPROACH 1: EXACT ALGORITHM (BRUTE FORCE) ---
    public void solveExactTSP(int[][] distMatrix) {
        System.out.println("Solving TSP with Exact Algorithm (Brute Force)...");
        int n = distMatrix.length;
        boolean[] visited = new boolean[n];
        int[] currentPath = new int[n];
        
        visited[0] = true;
        currentPath[0] = 0;
        minCost = Integer.MAX_VALUE;
        bestPath = new int[n];
        
        tspBacktrack(distMatrix, visited, currentPath, 0, 1, 0, n);
        
        System.out.println("Exact TSP Optimal Cost: " + minCost + " km");
        System.out.print("Exact Route: ");
        for (int city : bestPath) {
            System.out.print(city + " -> ");
        }
        System.out.println("0\n"); // Return to start
    }

    private void tspBacktrack(int[][] dist, boolean[] visited, int[] path, int currPos, int count, int cost, int n) {
        // Base case: All cities visited, check return distance
        if (count == n && dist[currPos][0] > 0) {
            if (cost + dist[currPos][0] < minCost) {
                minCost = cost + dist[currPos][0];
                System.arraycopy(path, 0, bestPath, 0, n);
            }
            return;
        }
        
        // Recursive case: Try next unvisited cities
        for (int i = 0; i < n; i++) {
            if (!visited[i] && dist[currPos][i] > 0) {
                visited[i] = true;
                path[count] = i;
                tspBacktrack(dist, visited, path, i, count + 1, cost + dist[currPos][i], n);
                visited[i] = false;
            }
        }
    }

    // --- APPROACH 2: HEURISTIC ALGORITHM (NEAREST NEIGHBOR) ---
    public void solveHeuristicTSP(int[][] distMatrix) {
        System.out.println("Solving TSP with Heuristic Algorithm (Nearest Neighbor)...");
        int n = distMatrix.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;
        
        int currentCity = 0;
        int totalCost = 0;
        System.out.print("Heuristic Route: 0 -> ");
        
        for (int step = 1; step < n; step++) {
            int nextCity = -1;
            int minEdge = Integer.MAX_VALUE;
            
            for (int i = 0; i < n; i++) {
                if (!visited[i] && distMatrix[currentCity][i] > 0 && distMatrix[currentCity][i] < minEdge) {
                    minEdge = distMatrix[currentCity][i];
                    nextCity = i;
                }
            }
            
            visited[nextCity] = true;
            totalCost += minEdge;
            currentCity = nextCity;
            System.out.print(currentCity + " -> ");
        }
        
        // Return to starting city
        totalCost += distMatrix[currentCity][0];
        System.out.println("0");
        System.out.println("Heuristic TSP Cost: " + totalCost + " km\n");
    }
}
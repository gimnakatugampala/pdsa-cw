package pdsa.cw;

import java.util.Arrays;

public class ResourceAllocation {

    // --- EXACT ALGORITHM: DYNAMIC PROGRAMMING ---
    public void allocateExactDP(CargoItem[] items, int truckCapacity) {
        int n = items.length;
        int[][] dp = new int[n + 1][truckCapacity + 1];

        // Build the DP table
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= truckCapacity; w++) {
                if (items[i - 1].weight <= w) {
                    dp[i][w] = Math.max(
                        items[i - 1].value + dp[i - 1][w - items[i - 1].weight],
                        dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        System.out.println("DP Exact Max Value: $" + dp[n][truckCapacity]);
        
        // Backtrack to find which items were selected
        int res = dp[n][truckCapacity];
        int w = truckCapacity;
        System.out.print("Items loaded (DP): ");
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i - 1][w]) {
                System.out.print(items[i - 1].name + " ");
                res -= items[i - 1].value;
                w -= items[i - 1].weight;
            }
        }
        System.out.println("\n");
    }

    // --- HEURISTIC ALGORITHM: GREEDY APPROXIMATION ---
    public void allocateHeuristicGreedy(CargoItem[] items, int truckCapacity) {
        // Sort items by value-to-weight ratio in descending order
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));

        int currentWeight = 0;
        int totalValue = 0;
        
        System.out.print("Items loaded (Greedy): ");
        for (CargoItem item : items) {
            if (currentWeight + item.weight <= truckCapacity) {
                currentWeight += item.weight;
                totalValue += item.value;
                System.out.print(item.name + " ");
            }
        }
        System.out.println("\nGreedy Heuristic Max Value: $" + totalValue);
    }
}
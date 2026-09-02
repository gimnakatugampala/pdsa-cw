package pdsa.cw;

import java.util.Arrays;
import java.util.Comparator;

public class DecisionModule {

    // --- APPROACH 1: WEIGHTED SUM MODEL (WSM) ---
    public void rankWithWSM(Supplier[] suppliers, double[] weights) {
        System.out.println("Ranking Suppliers using WSM (Heuristic)...");
        
        // Normalize and calculate score (assuming Cost/Time need to be inverted, so we subtract from a max value)
        double maxCost = Arrays.stream(suppliers).mapToDouble(s -> s.cost).max().orElse(1);
        double maxTime = Arrays.stream(suppliers).mapToDouble(s -> s.deliveryTime).max().orElse(1);

        for (Supplier s : suppliers) {
            double normCost = 1.0 - (s.cost / maxCost); 
            double normRel = s.reliability / 100.0;
            double normTime = 1.0 - (s.deliveryTime / maxTime);

            s.finalScore = (normCost * weights[0]) + (normRel * weights[1]) + (normTime * weights[2]);
        }

        // Sort by final score descending
        Arrays.sort(suppliers, (a, b) -> Double.compare(b.finalScore, a.finalScore));

        for (int i = 0; i < suppliers.length; i++) {
            System.out.printf("%d. %s (Score: %.4f)%n", (i + 1), suppliers[i].name, suppliers[i].finalScore);
        }
        System.out.println();
    }

    // --- APPROACH 2: SIMPLIFIED TOPSIS ALGORITHM ---
    public void rankWithTOPSIS(Supplier[] suppliers, double[] weights) {
        System.out.println("Ranking Suppliers using TOPSIS (Analytical Distance)...");
        int n = suppliers.length;
        
        // 1. Calculate sum of squares for normalization
        double sumCostSq = 0, sumRelSq = 0, sumTimeSq = 0;
        for (Supplier s : suppliers) {
            sumCostSq += Math.pow(s.cost, 2);
            sumRelSq += Math.pow(s.reliability, 2);
            sumTimeSq += Math.pow(s.deliveryTime, 2);
        }
        sumCostSq = Math.sqrt(sumCostSq);
        sumRelSq = Math.sqrt(sumRelSq);
        sumTimeSq = Math.sqrt(sumTimeSq);

        // 2. Identify Ideal Best and Ideal Worst (Simplified for Cost=min, Rel=max, Time=min)
        double idealBestCost = Double.MAX_VALUE, idealWorstCost = Double.MIN_VALUE;
        double idealBestRel = Double.MIN_VALUE, idealWorstRel = Double.MAX_VALUE;
        double idealBestTime = Double.MAX_VALUE, idealWorstTime = Double.MIN_VALUE;

        double[][] normalizedMatrix = new double[n][3];
        for (int i = 0; i < n; i++) {
            normalizedMatrix[i][0] = (suppliers[i].cost / sumCostSq) * weights[0];
            normalizedMatrix[i][1] = (suppliers[i].reliability / sumRelSq) * weights[1];
            normalizedMatrix[i][2] = (suppliers[i].deliveryTime / sumTimeSq) * weights[2];

            idealBestCost = Math.min(idealBestCost, normalizedMatrix[i][0]);
            idealWorstCost = Math.max(idealWorstCost, normalizedMatrix[i][0]);

            idealBestRel = Math.max(idealBestRel, normalizedMatrix[i][1]);
            idealWorstRel = Math.min(idealWorstRel, normalizedMatrix[i][1]);

            idealBestTime = Math.min(idealBestTime, normalizedMatrix[i][2]);
            idealWorstTime = Math.max(idealWorstTime, normalizedMatrix[i][2]);
        }

        // 3. Calculate distance to ideal best and worst, then calculate relative closeness
        for (int i = 0; i < n; i++) {
            double distBest = Math.sqrt(Math.pow(normalizedMatrix[i][0] - idealBestCost, 2) + 
                                        Math.pow(normalizedMatrix[i][1] - idealBestRel, 2) + 
                                        Math.pow(normalizedMatrix[i][2] - idealBestTime, 2));
                                        
            double distWorst = Math.sqrt(Math.pow(normalizedMatrix[i][0] - idealWorstCost, 2) + 
                                         Math.pow(normalizedMatrix[i][1] - idealWorstRel, 2) + 
                                         Math.pow(normalizedMatrix[i][2] - idealWorstTime, 2));

            suppliers[i].finalScore = distWorst / (distBest + distWorst);
        }

        // Sort by TOPSIS relative closeness descending
        Arrays.sort(suppliers, (a, b) -> Double.compare(b.finalScore, a.finalScore));

        for (int i = 0; i < suppliers.length; i++) {
            System.out.printf("%d. %s (Relative Closeness: %.4f)%n", (i + 1), suppliers[i].name, suppliers[i].finalScore);
        }
        System.out.println();
    }
}
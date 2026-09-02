package pdsa.cw;

public class Supplier {
    String name;
    double cost;        // Lower is better
    double reliability; // Higher is better (out of 100)
    double deliveryTime;// Lower is better (in days)
    double finalScore;

    public Supplier(String name, double cost, double reliability, double deliveryTime) {
        this.name = name;
        this.cost = cost;
        this.reliability = reliability;
        this.deliveryTime = deliveryTime;
    }
}
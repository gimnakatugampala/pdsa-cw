package pdsa.cw;

public final class Supplier {
    public final String name;
    public final double cost;
    public final double reliability;
    public final double deliveryTime;
    public Supplier(String name, double cost, double reliability, double deliveryTime) {
        this.name = name; this.cost = cost; this.reliability = reliability; this.deliveryTime = deliveryTime;
    }
}

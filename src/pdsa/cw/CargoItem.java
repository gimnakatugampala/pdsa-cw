package pdsa.cw;

public final class CargoItem {
    public final String name;
    public final int weight;
    public final int value;
    public CargoItem(String name, int weight, int value) {
        if (weight <= 0 || value < 0) throw new IllegalArgumentException("Invalid cargo item.");
        this.name = name; this.weight = weight; this.value = value;
    }
    @Override public String toString() { return name + "(w=" + weight + ", v=" + value + ")"; }
}

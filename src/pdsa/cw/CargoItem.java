package pdsa.cw;

public class CargoItem {
    String name;
    int weight;
    int value;
    double ratio;

    public CargoItem(String name, int weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.ratio = (double) value / weight;
    }
}
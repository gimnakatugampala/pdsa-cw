package pdsa.cw;

public final class Edge {
    public final int sourceNode;
    public final int targetNode;
    public final int weight;

    public Edge(int targetNode, int weight) { this(-1, targetNode, weight); }
    public Edge(int sourceNode, int targetNode, int weight) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.weight = weight;
    }
}

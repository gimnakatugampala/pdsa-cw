package pdsa.cw;

import java.util.*;

public final class PathResult {
    public final int distance;
    public final List<Integer> path;
    private PathResult(int distance, List<Integer> path) { this.distance=distance; this.path=Collections.unmodifiableList(path); }
    public static PathResult from(int goal, int[] dist, int[] prev, int start) {
        if (dist[goal] == Integer.MAX_VALUE) return new PathResult(Integer.MAX_VALUE, Collections.emptyList());
        List<Integer> path = new ArrayList<>();
        for (int at=goal; at!=-1; at=prev[at]) { path.add(at); if (at==start) break; }
        if (path.get(path.size()-1) != start) return new PathResult(Integer.MAX_VALUE, Collections.emptyList());
        Collections.reverse(path); return new PathResult(dist[goal], path);
    }
    public void print(String label, String[] names) {
        System.out.println("\n--- " + label + " ---");
        if (path.isEmpty()) { System.out.println("No route found."); return; }
        System.out.println("Distance: " + distance + " km");
        System.out.println("Route: " + path.stream().map(i -> names == null ? String.valueOf(i) : names[i]).reduce((a,b)->a+" -> "+b).orElse(""));
    }
}

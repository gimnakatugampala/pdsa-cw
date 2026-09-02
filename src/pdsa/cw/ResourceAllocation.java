package pdsa.cw;

import java.util.*;

public class ResourceAllocation {
    public AllocationResult dynamicProgramming(List<CargoItem> items, int capacity) {
        validate(items, capacity);
        int n=items.size(); int[][] dp=new int[n+1][capacity+1];
        for(int i=1;i<=n;i++) for(int c=0;c<=capacity;c++) {
            CargoItem item=items.get(i-1);
            dp[i][c]=dp[i-1][c];
            if(item.weight<=c) dp[i][c]=Math.max(dp[i][c],dp[i-1][c-item.weight]+item.value);
        }
        List<CargoItem> selected=new ArrayList<>(); int c=capacity;
        for(int i=n;i>=1;i--) if(dp[i][c]!=dp[i-1][c]) { CargoItem x=items.get(i-1); selected.add(x); c-=x.weight; }
        Collections.reverse(selected); return new AllocationResult(dp[n][capacity], selected, "Dynamic Programming");
    }

    public AllocationResult greedy(List<CargoItem> items, int capacity) {
        validate(items, capacity);
        List<CargoItem> sorted=new ArrayList<>(items);
        sorted.sort((a,b)->Double.compare((double)b.value/b.weight,(double)a.value/a.weight));
        List<CargoItem> selected=new ArrayList<>(); int used=0,value=0;
        for(CargoItem x:sorted) if(used+x.weight<=capacity) {selected.add(x); used+=x.weight; value+=x.value;}
        return new AllocationResult(value, selected, "Greedy (Value/Weight)");
    }

    public AllocationResult branchAndBound(List<CargoItem> items, int capacity) {
        validate(items, capacity);
        List<CargoItem> sorted=new ArrayList<>(items);
        sorted.sort((a,b)->Double.compare((double)b.value/b.weight,(double)a.value/a.weight));
        BnBState state=new BnBState(); branch(sorted,capacity,0,0,0,new ArrayList<>(),state);
        return new AllocationResult(state.bestValue,state.bestItems,"Branch and Bound");
    }

    private void branch(List<CargoItem> a,int cap,int idx,int weight,int value,List<CargoItem> chosen,BnBState best){
        if(weight>cap)return; if(value>best.bestValue){best.bestValue=value;best.bestItems=new ArrayList<>(chosen);}
        if(idx==a.size())return;
        int bound=upperBound(a,cap,idx,weight,value); if(bound<=best.bestValue)return;
        CargoItem x=a.get(idx); chosen.add(x); branch(a,cap,idx+1,weight+x.weight,value+x.value,chosen,best); chosen.remove(chosen.size()-1);
        branch(a,cap,idx+1,weight,value,chosen,best);
    }
    private int upperBound(List<CargoItem>a,int cap,int idx,int weight,int value){
        double bound=value; int w=weight;
        for(int i=idx;i<a.size()&&w<cap;i++){CargoItem x=a.get(i); if(w+x.weight<=cap){w+=x.weight;bound+=x.value;}else{bound+=(cap-w)*((double)x.value/x.weight);break;}}
        return (int)Math.ceil(bound);
    }
    private static void validate(List<CargoItem> items,int capacity){if(items==null)throw new IllegalArgumentException("Items cannot be null.");if(capacity<0)throw new IllegalArgumentException("Capacity cannot be negative.");}

    public static final class AllocationResult {
        public final int value; public final List<CargoItem> selected; public final String algorithm;
        AllocationResult(int value,List<CargoItem> selected,String algorithm){this.value=value;this.selected=Collections.unmodifiableList(selected);this.algorithm=algorithm;}
        public int totalWeight(){int w=0;for(CargoItem x:selected)w+=x.weight;return w;}
        public void print(){System.out.println("\n--- "+algorithm+" ---\nValue: "+value+"\nWeight: "+totalWeight()+"\nSelected: "+selected);}
    }
    private static final class BnBState {int bestValue;List<CargoItem>bestItems=new ArrayList<>();}
}

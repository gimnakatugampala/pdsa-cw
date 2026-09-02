package pdsa.cw;

import java.util.*;

public final class GraphGenerator {
    private GraphGenerator() {}
    public static Graph connectedGeographicGraph(int nodes,int extraEdges,long seed){
        if(nodes<2)throw new IllegalArgumentException("nodes must be >= 2");
        Random r=new Random(seed); Graph g=new Graph(nodes);
        for(int i=0;i<nodes;i++){double lat=6.9+(i/100)*0.04+(i%100)*0.0005;double lon=79.8+(i/100)*0.05+(i%100)*0.0005;g.setCoordinate(i,lat,lon);}
        Set<Long> used=new HashSet<>();
        for(int i=1;i<nodes;i++){int parent=r.nextInt(i);add(g,parent,i,used);}
        int max=Math.min(nodes*(nodes-1)/2-used.size(),Math.max(0,extraEdges));
        while(max>0){int a=r.nextInt(nodes),b=r.nextInt(nodes);if(a==b)continue;if(add(g,a,b,used))max--;}
        return g;
    }
    private static boolean add(Graph g,int a,int b,Set<Long>used){int x=Math.min(a,b),y=Math.max(a,b);long key=((long)x<<32)|(y&0xffffffffL);if(!used.add(key))return false;int w=(int)Math.ceil(haversine(g.getLatitude(a),g.getLongitude(a),g.getLatitude(b),g.getLongitude(b))*1.20+1);g.addEdge(a,b,w);return true;}
    public static double haversine(double lat1,double lon1,double lat2,double lon2){double R=6371.0;double p1=Math.toRadians(lat1),p2=Math.toRadians(lat2),dp=p2-p1,dl=Math.toRadians(lon2-lon1);double a=Math.sin(dp/2)*Math.sin(dp/2)+Math.cos(p1)*Math.cos(p2)*Math.sin(dl/2)*Math.sin(dl/2);return R*2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));}

    public static List<CargoItem> randomCargo(int n,long seed){Random r=new Random(seed);List<CargoItem>items=new ArrayList<>();for(int i=0;i<n;i++)items.add(new CargoItem("Item"+(i+1),1+r.nextInt(40),10+r.nextInt(490)));return items;}
    public static List<Supplier> randomSuppliers(int n,long seed){Random r=new Random(seed);List<Supplier>s=new ArrayList<>();for(int i=0;i<n;i++)s.add(new Supplier("Supplier"+(i+1),100+r.nextInt(900),0.50+r.nextDouble()*0.50,1+r.nextInt(20)));return s;}
    public static List<List<Integer>> randomTspMatrix(int n,long seed){Random r=new Random(seed);int[][] c=new int[n][2];for(int i=0;i<n;i++){c[i][0]=r.nextInt(1000);c[i][1]=r.nextInt(1000);}List<List<Integer>>d=new ArrayList<>();for(int i=0;i<n;i++){List<Integer>row=new ArrayList<>();for(int j=0;j<n;j++)row.add(i==j?0:Math.max(1,(int)Math.round(Math.hypot(c[i][0]-c[j][0],c[i][1]-c[j][1]))));d.add(row);}return d;}
}

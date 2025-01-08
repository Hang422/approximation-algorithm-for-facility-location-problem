package Base; /**
 * projectName: FLP3.0
 * fileName: UI.Graph.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 21:35
 * copyright(c) 2017-2020 xxx公司
 */

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: UI.Graph
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 21:35
 **/
public class Graph {
    private final double[][] graph;
    private final int sizeF;
    private final int sizeD;

    public double[][] getGraph() {
        return graph;
    }

    public Graph(int SizeF, int SizeD, MyComponent myComponent){
        sizeF = SizeF;
        sizeD = SizeD;
        graph = new double[sizeD][sizeF];
        for(int i=0;i<sizeD;i++){
            for(int j=0;j<sizeF;j++){
                graph[i][j] = myComponent.getPoint2Ds().get(1).get(i).distance(myComponent.getPoint2Ds().get(0).get(j));
            }
        }
    }
}
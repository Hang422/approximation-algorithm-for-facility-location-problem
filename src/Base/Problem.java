package Base; /**
 * projectName: FLP3.0
 * fileName: Initiazation.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-24 15:51
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Initiazation
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-24 15:51
 **/

public class Problem implements Cloneable{
    //Some function we need static, so the parameters we must change by hands.
    //And for some error input, we do not implement detection to show the input are error.
    //It's possible that the array exceed or other error if the initial parameters are invalid.
    private static final int LEVEL = 3;
    private static final int sizeC = 1000;
    private static final int sizeF = 100;
    private static final int sizeO = 50;

    private static final int BIG = 100000;
    private static final int TooBIG = 100000000;
    private static final int LittleBIG = 10000;
    private static final int MIDDLE = 1000;
    private static final int SMALL = 10;

    private double[][] cost = new double[sizeF][LEVEL];
    private final int[] level = new int[sizeC];
    private final double[][] distance = new double[sizeC][sizeF];
    MyComponent myComponent = new MyComponent();

    public Problem() {
        myComponent.setPoint2Ds(sizeF,sizeC);
        Graph graph = new Graph(sizeF,sizeC,myComponent);
        for(int j=0;j<sizeF;j++){
            cost[j][0] = ((Math.random() + 0.1)* BIG);
            for(int i=1;i<LEVEL;i++){
                cost[j][i] = cost[j][i-1] + (Math.random() + 0.1)* BIG;
            }
        }
        for(int i=0;i<sizeC;i++)
            level[i] = (int) ((Math.random()) * 2 + 1 + 0.5);
        for(int i=0;i<sizeC;i++){
            for(int j=0;j<sizeF;j++){
                distance[i][j] = graph.getGraph()[i][j];
            }
        }
    }

    public void setCost(double[][] cost) {
        this.cost = cost;
    }

    public ArrayList<Problem> getInstances() {
        ArrayList<Problem> problems = new ArrayList<>();

        for(int j=0;j<sizeF;j++){
            Problem problem = new Problem();
            problem = (Problem) this.clone();
            double [][] temp = new double[sizeF][LEVEL];

            for(int i=0;i<sizeF;i++){
                for(int x=0;x<LEVEL;x++)
                    temp[i][x] = cost[i][x];
            }

            for(int z=0;z<sizeF;z++){
                for(int k=0;k<LEVEL;k++) {
                    if (temp[j][LEVEL - 1] < temp[z][k])
                        temp[z][k] = Double.MAX_VALUE;
                }
            }
            problem.setCost(temp);
            problems.add(problem);
        }
        return problems;
    }

    public void printProblem(){
        System.out.println("Matrix:" );
        for(int i=0;i<sizeC;i++){
            for(int j=0;j<sizeF;j++)
                System.out.print(distance[i][j] + "  ");
            System.out.println();
        }
        System.out.println("Cost:");
        for(int j=0;j<sizeF;j++){
            for(int k=0;k<LEVEL;k++){
                System.out.print(cost[j][k] + " ");
            }
            System.out.println();
        }
        System.out.println("level:");
        for(int i=0;i<sizeC;i++)
            System.out.print(level[i] + "  ");
    }

    public MyComponent getMyComponent() {
        return myComponent;
    }

    public double[][] getCost(){
        return cost;
    }

    public double[][] getDistance(){
        return distance;
    }

    public int[] getLevel(){
        return level;
    }

    public static int getSizeO() {
        return sizeO;
    }

    public static int getSizeF() {
        return sizeF;
    }

    public static int getSizeC() {
        return sizeC;
    }

    public static int getLEVEL() {
        return LEVEL;
    }

    @Override
    public String toString() {
        return "Base.Problem{" +
                "cost=" + Arrays.toString(cost) +
                '}';
    }

    @Override
    public Object clone() {
        Problem stu = null;
        try{
            stu = (Problem) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
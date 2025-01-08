package Heuristic.Algorithms; /**
 * projectName: FLP3.0
 * fileName: Greedy.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-24 16:21
 * copyright(c) 2017-2020 xxx公司
 */

import Base.Function;
import Base.Problem;
import Heuristic.SimpleClient;
import Heuristic.SimpleFacility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Greedy
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-24 16:21
 **/
public class GreedyAlgorithm {

    private static final double INFINITE = Double.MAX_VALUE;

    public static double solve(Problem problem) {

        double[][] matrix = problem.getDistance();
        double[][] cost = problem.getCost();
        int[] level = problem.getLevel();
        int sizeO = problem.getSizeO();
        int sizeC = problem.getSizeC();
        int sizeF = problem.getSizeF();
        int LEVEL = problem.getLEVEL();

        ArrayList<SimpleClient> clients = new ArrayList<>();
        ArrayList<SimpleFacility> facilities = new ArrayList<>();
        ArrayList<SimpleFacility> openedFacilities = new ArrayList<>();
        double totalCost = 0;

        for(int j=0;j<sizeF;j++){
            SimpleFacility simpleFacility = new SimpleFacility();
            simpleFacility.setNum(j);
            ArrayList<Double> arrayList = new ArrayList<>();
            for(int i=0;i<sizeC;i++){
                arrayList.add(matrix[i][j]);
            }
            simpleFacility.setDistance(arrayList);
            facilities.add(simpleFacility);
        }

        int firstOpen = (int) Function.firstOpen(cost,matrix,sizeC,sizeO,sizeF,LEVEL)[0];
        totalCost = Function.firstOpen(cost,matrix,sizeC,sizeO,sizeF,LEVEL)[1];
        facilities.get(firstOpen).setLevel(LEVEL);
        openedFacilities.add(facilities.get(firstOpen));

        for(int i=0;i<sizeC;i++){
            SimpleClient simpleClient = new SimpleClient(i, level[i]);
            simpleClient.setSum(matrix[i][firstOpen]);
            simpleClient.setConnectFacility(facilities.get(firstOpen));

            clients.add(simpleClient);
        }

        Collections.sort(clients);


        while (true){
            SimpleFacility nextOpen = null;
            double improvement = 0;
            int nextLevel = 0;
            for(SimpleFacility facility : facilities){
                if(!openedFacilities.contains(facility)){
                    for(int i=1;i<LEVEL+1;i++){
                        double temp = Function.addImprovement(clients,i,facility,cost,matrix,sizeC,sizeO);
                        if(temp > improvement){
                            improvement = temp;
                            nextOpen = facility;
                            nextLevel = i;
                        }
                    }

                }
            }
            if(nextOpen==null)
                break;

            for(int i=0;i<sizeC;i++){
                SimpleClient client = clients.get(i);
                if(nextLevel>=client.getLevel() && matrix[client.getNum()][client.getConnectFacility().getNum()] > matrix[client.getNum()][nextOpen.getNum()]){
                    client.setConnectFacility(nextOpen);
                }
                client.setSum(matrix[client.getNum()][client.getConnectFacility().getNum()]);
            }

            Collections.sort(clients);
            totalCost -= improvement * cost[nextOpen.getNum()][nextLevel-1];

            nextOpen.setLevel(nextLevel);
            openedFacilities.add(nextOpen);
        }

//        for(SimpleFacility simpleFacility : openedFacilities)
//            System.out.println(simpleFacility.getNum());
//        System.out.println("WWW");
//        for(int i=sizeC;i>sizeC-sizeO;i--){
//            System.out.println(clients.get(i-1).getNum());
//        }
//        System.out.println("WWW");
        return totalCost;
    }

}
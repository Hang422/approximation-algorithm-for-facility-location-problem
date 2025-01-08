package Heuristic.Algorithms;

import Base.Function;
import Base.Problem;
import Heuristic.SimpleClient;
import Heuristic.SimpleFacility;

import java.util.*;

public class LocalSearchAlgorithm {
    private static final double INFINITE = Double.MAX_VALUE;

    private static double[][] matrix;
    private static double[][] cost;
    private static int[] level;
    private static int LEVEL;
    private static int sizeO;
    private static int sizeC;
    private static int sizeF;


    public static double solve(Problem problem) {

        matrix = problem.getDistance();
        cost = problem.getCost();
        level = problem.getLevel();
        sizeO = problem.getSizeO();
        sizeC = problem.getSizeC();
        sizeF = problem.getSizeF();
        LEVEL = problem.getLEVEL();

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
            if(addChange(openedFacilities,clients,facilities))
                continue;
            else if(deleteChange(openedFacilities,clients))
                continue;
            else if(exChange(openedFacilities,clients,facilities))
                continue;
            break;
        }


        return Function.getCost(clients,openedFacilities,sizeC-sizeO,cost,matrix);

    }

    public static boolean addChange(ArrayList<SimpleFacility> openFacilities, ArrayList<SimpleClient> clients,ArrayList<SimpleFacility> facilities){
        Collections.sort(clients);
        for(SimpleFacility simpleFacility:facilities){
            if(openFacilities.contains(simpleFacility))
                continue;
            for(int i = 1; i<LEVEL+1; i++){
                if(Function.addImprovement(clients,i,simpleFacility,cost,matrix,sizeC,sizeO)>0){
                    simpleFacility.setLevel(i);
                    openFacilities.add(simpleFacility);
                    Function.connect(openFacilities,clients,matrix);
                    return true;
                }
            }

        }
        return false;
    }

    public static boolean deleteChange(ArrayList<SimpleFacility> openFacilities, ArrayList<SimpleClient> clients){
        Collections.sort(clients);
        for(SimpleFacility delete:openFacilities){
            if(Function.deleteImprovement(clients,openFacilities,delete,cost,matrix,sizeC,sizeO)>0){
                openFacilities.remove(delete);
                Function.connect(openFacilities,clients,matrix);
                return true;
            }
        }
        return false;
    }

    public static boolean exChange(ArrayList<SimpleFacility> openFacilities, ArrayList<SimpleClient> clients,ArrayList<SimpleFacility> facilities){
        Collections.sort(clients);
        for(SimpleFacility inF:openFacilities){
            for(SimpleFacility outF:facilities){
                if(openFacilities.contains(outF))
                    continue;
                for(int i = 1; i<LEVEL+1; i++){
                    outF.setLevel(i);
                    if(Function.exchangeImprovement(clients,openFacilities,inF,outF,cost,matrix,sizeC,sizeO)>0){
                        openFacilities.remove(inF);
                        openFacilities.add(outF);
                        Function.connect(openFacilities,clients,matrix);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}


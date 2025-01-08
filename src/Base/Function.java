package Base; /**
 * projectName: FLP3.0
 * fileName: Function2.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-22 11:50
 * copyright(c) 2017-2020 xxx公司
 */

import Dual.Algorithm.DualAlgorithm;
import Heuristic.SimpleClient;
import Heuristic.SimpleFacility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Function2
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-22 11:50
 **/
public class Function {

    private static final double INFINITE = Double.MAX_VALUE;

    public static double average(ArrayList<Double> arrayList) {
        double sum = 0;
        for (Double d : arrayList) {
            sum += d;
        }
        return sum / arrayList.size();
    }

    public static double[] firstOpen(double[][] cost, double[][] matrix, int sizeC, int sizeO, int sizeF, int LEVEL) {
        double min = INFINITE;
        double[] t = new double[2];
        for (int j = 0; j < sizeF; j++) {
            double[] temp = new double[sizeC];
            double sum = 0;
            for (int i = 0; i < sizeC; i++) {
                temp[i] = matrix[i][j];
            }
            Arrays.sort(temp);
            for (int i = 0; i < sizeC - sizeO; i++)
                sum += temp[i];

            sum += cost[j][LEVEL - 1];
            if (sum < min) {
                min = sum;
                t[0] = j;
                t[1] = min;
            }
        }

        return t;
    }

    public static void writeIntoFile(){
        ArrayList<Double> dualWithClearResult = new ArrayList<>();
//        ArrayList<Double> greedyResult = new ArrayList<>();
//        ArrayList<Double> locationResult = new ArrayList<>();
//        ArrayList<Double> GeneticResult = new ArrayList<>();

        //Run 50 times on 50 random problems
        for(int i=0;i<1;i++){
            Base.Problem problem = new Base.Problem();
            dualWithClearResult.add(DualAlgorithm.solve(problem,true));
//            greedyResult.add(GreedyAlgorithm.solve(problem));
//            locationResult.add(LocalSearchAlgorithm.solve(problem));
//
//            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem);
//            GeneticResult.add(geneticAlgorithm.solve(10,1000).getCost());
        }

        //This is writing the average results to .txt. Each writing just corresponding a single point.
//        writeFile("E:\\FLP\\Dataa\\Big\\2000-50-50.txt",
//                Base.Function.average(dualWithClearResult) + "\n" +
//                        Base.Function.average(greedyResult) + "\n" +
//                        Base.Function.average(locationResult) + "\n" +
//                        Base.Function.average(GeneticResult));

        //This is writing the total data to .txt for each single point.
//        writeFile("E:\\FLP\\Data\\BBig\\500-250.txt",
//                "Dual with clear up:\n" +
//                        dualWithClearResult + "\n Average:" + Base.Function.average(dualWithClearResult) + "\n\n" +
//                        "Greedy:\n" +
//                        greedyResult + "\n Average:" + Base.Function.average(greedyResult) + "\n\n"+
//                        "Local Search:\n" +
//                        locationResult + "\n Average:" + Base.Function.average(locationResult) + "\n\n");



        //solve a particular problem instance.
//        Problem problem = new Problem();
//        System.out.println(Arrays.deepToString(problem.getCost()));
//
//        //clear up is an optional step, it's true in report and the algorithm design. But we find that it's false some time will get better result.
//        System.out.println("Dual with clear up:" + DualAlgorithm.solve(problem,true));
//        System.out.println("Dual without clear up:" + DualAlgorithm.solve(problem,false));
//        System.out.println("Greedy:" + GreedyAlgorithm.solve(problem));
//        System.out.println("Local Search:" + LocalSearchAlgorithm.solve(problem));
//
//        //The genetic algorithm is special so we need a new instance to solve.
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem);
//        System.out.println("geneticAlgorithm: " + geneticAlgorithm.solve(10,1000).getCost());
//
//        //print problem
//        problem.printProblem();

    }

    public static void writeFile(String filePath, String fileContent) {

        try {

            File f = new File(filePath);

            if (!f.exists()) {

                f.createNewFile();

            }

            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");

            BufferedWriter writer = new BufferedWriter(write);

            writer.write(fileContent);

            writer.close();

        } catch (Exception e) {

            System.out.println("写文件内容操作出错");

            e.printStackTrace();

        }
    }

    public static boolean verify(double totalCost, ArrayList<SimpleClient> simpleClients, ArrayList<SimpleFacility> facilities, int length, double cost[][],double[][] matrix) {
        double sum = getCost(simpleClients,facilities,length,cost,matrix);
        return Math.abs(totalCost-sum)<1;
    }

    public static double getCost(ArrayList<SimpleClient> simpleClients, ArrayList<SimpleFacility> facilities, int length, double[][] cost, double[][] matrix){
        Collections.sort(simpleClients);
        double sum = 0;
        for (int i = 0; i < length; i++) {
            if(!facilities.contains(simpleClients.get(i).getConnectFacility()) || simpleClients.get(i).getLevel() > simpleClients.get(i).getConnectFacility().getLevel())
                System.out.println("Error");
            sum += matrix[simpleClients.get(i).getNum()][simpleClients.get(i).getConnectFacility().getNum()];
        }
        for (SimpleFacility simpleFacility : facilities) {
            sum += cost[simpleFacility.getNum()][simpleFacility.getLevel() - 1];
        }

        return sum;
    }

    public static double addImprovement(ArrayList<SimpleClient> simpleClients, int openLevel, SimpleFacility facility, double[][] cost,
                                        double[][] matrix, int sizeC, int sizeO) {
        double oudCost = 0;
        ArrayList<SimpleClient> temp = new ArrayList<>();
        for (SimpleClient simpleClient : simpleClients) {
            SimpleClient n = (SimpleClient) simpleClient.clone();
            temp.add(n);
        }
        Collections.sort(temp);
        for (int i = 0; i < sizeC; i++) {
            SimpleClient client = temp.get(i);
            if (i < sizeC - sizeO) {
                oudCost += matrix[client.getNum()][client.getConnectFacility().getNum()];
            }

            if (openLevel >= client.getLevel() && matrix[client.getNum()][facility.getNum()] < client.getSum()) {
                client.setConnectFacility(facility);
                client.setSum(matrix[client.getNum()][facility.getNum()]);
            }
        }

        Collections.sort(temp);
        double nCost = 0;
        for (int i = 0; i < sizeC - sizeO; i++) {
            nCost += matrix[temp.get(i).getNum()][temp.get(i).getConnectFacility().getNum()];
        }
        return (oudCost - nCost - cost[facility.getNum()][openLevel - 1]) / cost[facility.getNum()][openLevel - 1];

    }

    public static double deleteImprovement(ArrayList<SimpleClient> simpleClients, ArrayList<SimpleFacility> facilities,
                                           SimpleFacility facility, double[][] cost, double[][] matrix, int sizeC, int sizeO) {
        double oudCost = 0;
        int outliers = 0;
        double nCost = 0;

        ArrayList<SimpleClient> temp = new ArrayList<>();
        for (SimpleClient simpleClient : simpleClients) {
            SimpleClient n = (SimpleClient) simpleClient.clone();
            temp.add(n);
        }
        for (int i = 0; i < sizeC - sizeO; i++)
            oudCost += matrix[simpleClients.get(i).getNum()][simpleClients.get(i).getConnectFacility().getNum()];

        for (int i = 0; i < sizeC; i++) {
            SimpleClient tempClient = temp.get(i);
            if (tempClient.getConnectFacility() == facility) {
                double min = INFINITE;
                for (SimpleFacility simpleFacility : facilities) {
                    if (simpleFacility != facility) {
                        if (simpleFacility.getLevel() >= tempClient.getLevel() && min > matrix[tempClient.getNum()][simpleFacility.getNum()]) {
                            min = matrix[tempClient.getNum()][simpleFacility.getNum()];
                            tempClient.setConnectFacility(simpleFacility);
                            tempClient.setSum(min);
                        }
                    }
                }
            }
            if (tempClient.getConnectFacility() == facility || tempClient.getConnectFacility()==null) {
                tempClient.setConnectFacility(null);
                tempClient.setSum(INFINITE);
                outliers++;
            }
        }

        if (outliers > sizeO)
            return -1;

        Collections.sort(temp);

        for (int i = 0; i < sizeC - sizeO; i++) {
            nCost += matrix[temp.get(i).getNum()][temp.get(i).getConnectFacility().getNum()];
        }

        return (cost[facility.getNum()][facility.getLevel() - 1] - nCost + oudCost);

    }

    public static double exchangeImprovement(ArrayList<SimpleClient> simpleClients, ArrayList<SimpleFacility> facilities,
                                             SimpleFacility inF, SimpleFacility outF, double[][] cost, double[][] matrix, int sizeC, int sizeO) {

        double oudCost = 0;
        double nCost = 0;
        int outliers = 0;

        ArrayList<SimpleClient> temp = new ArrayList<>();
        for (SimpleClient simpleClient : simpleClients) {
            SimpleClient n = (SimpleClient) simpleClient.clone();
            temp.add(n);
        }

        ArrayList<SimpleFacility> open = new ArrayList<>();
        for (SimpleFacility simpleFacility : facilities) {
            SimpleFacility n = (SimpleFacility) simpleFacility.clone();
            open.add(n);
        }

        for (int i = 0; i < sizeC - sizeO; i++)
            oudCost += matrix[simpleClients.get(i).getNum()][simpleClients.get(i).getConnectFacility().getNum()];


        open.add(outF);
        open.removeIf(simpleFacility -> simpleFacility.getNum() == inF.getNum());
        outliers=Function.getOutliers(open,temp,matrix);
        Function.connect(open,temp,matrix);

        if (outliers > sizeO)
            return -1;

        Collections.sort(temp);


        for (int i = 0; i < sizeC - sizeO; i++) {
            nCost += matrix[temp.get(i).getNum()][temp.get(i).getConnectFacility().getNum()];
        }

        return (oudCost + cost[inF.getNum()][inF.getLevel()-1] - nCost - cost[outF.getNum()][outF.getLevel()-1]);
    }

    public static int getOutliers(ArrayList<SimpleFacility> openFacilities,ArrayList<SimpleClient> clients,double[][] matrix){
        int outliers = 0;
        for(SimpleClient simpleClient : clients){
            double min = INFINITE;
            for(SimpleFacility simpleFacility : openFacilities){
                if (simpleFacility.getLevel() >= simpleClient.getLevel() && min > matrix[simpleClient.getNum()][simpleFacility.getNum()]) {
                    min = matrix[simpleClient.getNum()][simpleFacility.getNum()];
                    simpleClient.setConnectFacility(simpleFacility);
                    simpleClient.setSum(min);
                }
            }
            if(!openFacilities.contains(simpleClient.getConnectFacility()) || simpleClient.getConnectFacility()==null){
                outliers++;
                simpleClient.setConnectFacility(null);
                simpleClient.setSum(INFINITE);
            }
        }
        return outliers;
    }

    public static void connect(ArrayList<SimpleFacility> openFacilities,ArrayList<SimpleClient> clients,double[][] matrix){
        for(SimpleClient simpleClient : clients){
            double min = INFINITE;
            for(SimpleFacility simpleFacility : openFacilities){
                if (simpleFacility.getLevel() >= simpleClient.getLevel() && min > matrix[simpleClient.getNum()][simpleFacility.getNum()]) {
                    min = matrix[simpleClient.getNum()][simpleFacility.getNum()];
                    simpleClient.setConnectFacility(simpleFacility);
                    simpleClient.setSum(min);
                }
            }
            if(!openFacilities.contains(simpleClient.getConnectFacility())){
                simpleClient.setConnectFacility(null);
                simpleClient.setSum(INFINITE);
            }
        }
    }

}
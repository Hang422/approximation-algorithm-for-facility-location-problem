package Heuristic.Algorithms;

import Base.Problem;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SimulatedAnnealingAlgorithm {

    private final double[][] matrix;
    private final double[][] cost;
    private final int[] levelClient;
    private final int LEVEL;
    private final int SizeO;
    private double T;
    private double alpha;
    private int maxIter;

    public SimulatedAnnealingAlgorithm(Problem problem) {
        this.matrix = problem.getDistance();
        this.cost = problem.getCost();
        this.levelClient = problem.getLevel();
        this.LEVEL = Problem.getLEVEL();
        this.SizeO = Problem.getSizeO();
        initParameters();
    }

    private void initParameters() {
        this.T = 1000;
        this.alpha = 0.7;
        this.maxIter = 1000;
    }

    public Solution solve() {
        List<Integer> currentSolution = initializeSolution();
        int k = 0;
        double currentCost = getTotalCost(currentSolution);
        List<Integer> bestSolution = new ArrayList<>(currentSolution);
        double bestCost = currentCost;
        while (T > 1) {
            for (int i = 0; i < maxIter; i++) {
                List<Integer> newSolution = generateNewSolution(currentSolution);
                double newCost = getTotalCost(newSolution);
                if (newCost < currentCost || accept(currentCost, newCost)) {
                    currentSolution = newSolution;
                    currentCost = newCost;
                    if (currentCost < bestCost) {
                        bestSolution = new ArrayList<>(currentSolution);
                        bestCost = currentCost;
                    }
                }
            }
            T *= alpha;
        }

        setMaxNElementsToMinusOne(bestSolution,matrix,SizeO);
        return new Solution(bestSolution, getTotalCost(bestSolution));
    }

    private List<Integer> initializeSolution() {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            int facility = getRandomFacility(i,solution);
            solution.add(facility);
        }
        return solution;
    }

    private int getRandomFacility(int client ,List<Integer> connection) {
        List<Integer> possibleFacilities = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        double totalWeight = 0.0;

        // Count the connections for each facility.
        int[] connections = new int[cost.length];
        for (int i = 0; i < connection.size(); i++) {
            if (connection.get(i) != -1) {
                connections[connection.get(i)]++;
            }
        }
        for (int j = 0; j < cost.length; j++) {
            if (cost[j][levelClient[client]-1] != Double.MAX_VALUE) {
                possibleFacilities.add(j);
                // Adjust the cost by the number of connections.
                double adjustedCost = cost[j][levelClient[client]-1] / (1 + connections[j]);
                // Calculate the weight by considering the adjusted cost.
                double weight = 1.0 / (adjustedCost + matrix[client][j]);
                weights.add(weight);
                totalWeight += weight;
            }
        }

        if (possibleFacilities.isEmpty()) {
            return -1;
        } else {
            for (int i = 0; i < weights.size(); i++) {
                weights.set(i, weights.get(i) / totalWeight);
            }
            double r = new Random().nextDouble();
            double t = new Random().nextDouble();
            if(t<0.8){
                double min = 2;
                int re = 0;
                for (int i = 0; i < weights.size(); i++) {
                    if(weights.get(i)<min){
                        min = weights.get(i);
                        re = i;
                    }
                    return possibleFacilities.get(re);
                }
            }
            double accumulatedWeight = 0.0;
            for (int i = 0; i < weights.size(); i++) {
                accumulatedWeight += weights.get(i);
                if (accumulatedWeight >= r) {
                    return possibleFacilities.get(i);
                }
            }
        }
        return -1; // Should not reach here
    }


    private List<Integer> generateNewSolution(List<Integer> currentSolution) {
        List<Integer> newSolution = new ArrayList<>(currentSolution);
        int clientToReassign = new Random().nextInt(matrix.length);
        int newFacility = getRandomFacility(clientToReassign,currentSolution);
        newSolution.set(clientToReassign, newFacility);
//        Ensure that the number of outliers does not exceed the limit
//        if (Collections.frequency(newSolution, -1) > SizeO) {
//            int outlierIndex = newSolution.indexOf(-1);
//            int newFacilityForOutlier = getRandomFacility(outlierIndex);
//            while (newFacilityForOutlier == -1) {
//                newFacilityForOutlier = getRandomFacility(outlierIndex);
//            }
//            newSolution.set(outlierIndex, newFacilityForOutlier);
//        }
        return newSolution;
    }

    private double getTotalCost(List<Integer> solution) {
        double totalCost = 0;
        Set<Integer> facilities = new HashSet<>();
        for (int i = 0; i < solution.size(); i++) {
            int facility = solution.get(i);
            if (facility != -1) {
                facilities.add(facility);
                totalCost += matrix[i][facility];
            }
        }
        for (int facility : facilities) {
            int maxLevel = -1;
            for (int i = 0; i < solution.size(); i++) {
                if (solution.get(i) == facility && levelClient[i] > maxLevel) {
                    maxLevel = levelClient[i];
                }
            }
            totalCost += cost[facility][maxLevel-1];
        }
        return totalCost;
    }

    private boolean accept(double currentCost, double newCost) {
        if (newCost < currentCost) {
            return true;
        }
        return Math.random() < Math.exp((currentCost - newCost) / T);
    }

    public void printSolutionDetails(Solution solution) {
        System.out.println("Final Cost: " + solution.getCost());

        List<Integer> facilityLevels = new ArrayList<>();
        for (int i = 0; i < cost.length; i++) {
            facilityLevels.add(-1);
        }

        int outliersCount = 0;
        for (int i = 0; i < solution.getSolution().size(); i++) {
            int facility = solution.getSolution().get(i);
            if (facility == -1) {
                System.out.println("Client " + i + " is an outlier.");
                outliersCount++;
            } else {
                System.out.println("Client " + i + " is assigned to facility " + facility);
                if (levelClient[i] > facilityLevels.get(facility)) {
                    facilityLevels.set(facility, levelClient[i]);
                }
            }
        }

        System.out.println("Outliers count: " + outliersCount);

        for (int i = 0; i < facilityLevels.size(); i++) {
            if (facilityLevels.get(i) != -1) {
                System.out.println("Facility " + i + " is opened with level " + facilityLevels.get(i));
            }
        }
    }

    public static void setMaxNElementsToMinusOne(List<Integer> list, double[][] matrix, int n) {
        List<Double> mappedValues = new ArrayList<>();

        // 获取列表映射的matrix[i][list(i)]值的集合
        for (int i = 0; i < list.size(); i++) {
            int element = list.get(i);
            double value = matrix[i][element];
            mappedValues.add(value);
        }

        // 找到最大的n个值
        List<Double> maxNValues = findMaxNValues(mappedValues, n);

        // 将对应的列表值设置为-1
        for (int i = 0; i < list.size(); i++) {
            double value = mappedValues.get(i);
            if (maxNValues.contains(value)) {
                list.set(i, -1);
            }
        }
    }

    public static List<Double> findMaxNValues(List<Double> values, int n) {
        List<Double> maxNValues = new ArrayList<>(values);
        Collections.sort(maxNValues, Comparator.reverseOrder());
        return maxNValues.subList(0, Math.min(n, maxNValues.size()));
    }

    public static class Solution {
        private final List<Integer> solution;
        private final double cost;

        public Solution(List<Integer> solution, double cost) {
            this.solution = solution;
            this.cost = cost;
        }

        public List<Integer> getSolution() {
            return solution;
        }

        public double getCost() {
            return cost;
        }
    }
}

package Heuristic.Algorithms;

import Base.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

/*概率非常影响整个算法*/

public class GeneticAlgorithm {
    private final double[][] distanceMatrix;
    private final double[][] costMatrix;
    private final int[] customerLevels;
    private final int sizeO;
    private final Random rand;
    private final double[] upgradeProbabilities;
    private final double[] maintainProbabilities;
    private final double[] downgradeProbabilities;
    private static final double Constant = 0.01;
    private static final int MaxTry = 10;

    public GeneticAlgorithm(Problem problem ) {
        this.distanceMatrix = problem.getDistance();
        this.costMatrix = problem.getCost();
        this.customerLevels = problem.getLevel();
        this.sizeO = Problem.getSizeO();
        this.rand = new Random();
        this.upgradeProbabilities = new double[costMatrix.length];
        this.maintainProbabilities = new double[costMatrix.length];
        this.downgradeProbabilities = new double[costMatrix.length];

        // Initialize probabilities for each facility
        for (int i = 0; i < costMatrix.length; i++) {
            upgradeProbabilities[i] = 0.2;
            maintainProbabilities[i] = 0.2;
            downgradeProbabilities[i] = 0.6;
        }
    }

    public Solution solve(int populationSize, int numGenerations) {
        Solution[] population = initializePopulation(populationSize);
        evaluatePopulation(population);

        for (int generation = 0; generation < numGenerations; generation++) {
            Solution[] offspring = createOffspring(population);
            evaluatePopulation(offspring);
            population = selectPopulation(population, offspring);
        }

        //System.out.println(Arrays.toString(upgradeProbabilities) + " " + Arrays.toString(maintainProbabilities) + " " + Arrays.toString(downgradeProbabilities));
        return getBestSolution(population);
    }

    private Solution[] initializePopulation(int populationSize) {
        Solution[] population = new Solution[populationSize];
        int temp = 0;
        for (int i = 0; i < populationSize; i++) {
            int[] facilityLevels = new int[costMatrix.length];
            for (int j = 0; j < costMatrix.length; j++) {
                facilityLevels[j] = rand.nextInt(costMatrix[j].length) + 1;
            }
            Solution solution = new Solution(facilityLevels, new int[distanceMatrix.length], new boolean[distanceMatrix.length]);

            if(isNoSolutionValid(solution)){
                temp++;
                if(temp>MaxTry){
                    solution.facilityLevels[rand.nextInt(rand.nextInt(costMatrix[0].length) + 1)] = costMatrix[0].length;
                }
                else{
                    i--;
                    continue;
                }
            }
            assignCustomers(solution);
            population[i] = solution;
        }
        return population;
    }

    private void evaluatePopulation(Solution[] population) {
        for (Solution solution : population) {
            solution.cost = evaluateSolution(solution);
        }
    }

    private double evaluateSolution(Solution solution) {
        double totalCost = 0.0;
        ArrayList<Integer> openFacilities = new ArrayList<>();
        for (int i = 0; i < solution.customerAssignment.length; i++) {
            if (solution.outliers[i]) {
                continue;
            }
            if(!openFacilities.contains(solution.customerAssignment[i]))
                openFacilities.add(solution.customerAssignment[i]);
            totalCost += distanceMatrix[i][solution.customerAssignment[i] - 1];
        }
        for(int i:openFacilities)
            totalCost += costMatrix[i - 1][solution.facilityLevels[i - 1] - 1];
        solution.setOpenFacilities(openFacilities);
        return totalCost;
    }

    private Solution[] createOffspring(Solution[] population) {
        int populationSize = population.length;
        Solution[] offspring = new Solution[populationSize];

        for (int i = 0; i < populationSize; i++) {
            Solution parent = selectParent(population);
            Solution child = mutate(parent);
            offspring[i] = child;
        }

        return offspring;
    }

    private Solution selectParent(Solution[] population) {
        int populationSize = population.length;
        int parentIndex1 = rand.nextInt(populationSize);
        int parentIndex2 = rand.nextInt(populationSize);
        Solution parent1 = population[parentIndex1];
        Solution parent2 = population[parentIndex2];
        return parent1.cost <= parent2.cost ? parent1 : parent2;
    }

    private Solution mutate(Solution currentSolution) {
        int[] newFacilityLevels = currentSolution.facilityLevels.clone();
        int numFacilities = newFacilityLevels.length;

        for (int i = 0; i < numFacilities; i++) {
            int currentLevel = newFacilityLevels[i];
            int maxLevel = costMatrix[i].length;
            int minLevel = 0;

            // Randomly select a mutation operation
            double mutation = rand.nextDouble();
            if (mutation < upgradeProbabilities[i] && currentLevel < maxLevel) {
                // Upgrade: increase the level by 1
                newFacilityLevels[i] = currentLevel + 1;
            } else if (mutation < upgradeProbabilities[i] + maintainProbabilities[i]) {
                // Maintain: keep the current level (no change)
            } else if (mutation < upgradeProbabilities[i] + maintainProbabilities[i] + downgradeProbabilities[i] && currentLevel > minLevel) {
                // Downgrade: decrease the level by 1
                newFacilityLevels[i] = currentLevel - 1;
            }
        }

        Solution newSolution = new Solution(newFacilityLevels, currentSolution.customerAssignment.clone(), currentSolution.outliers.clone());

        // Check if the solution is valid, otherwise re-mutate
        if (isNoSolutionValid(newSolution)) {
            if(rand.nextDouble()<0.5)
                newSolution =  mutate(currentSolution);
            else
                newSolution.facilityLevels[rand.nextInt(rand.nextInt(costMatrix[0].length) + 1)] = costMatrix[0].length;
        }
        assignCustomers(newSolution);

        //Solution.printSolution(newSolution);
        adoption(currentSolution,newSolution);
        return newSolution;
    }

    public void adoption(Solution parent,Solution child){
        if(parent.cost>child.cost){
            for(int i=0;i<parent.facilityLevels.length;i++){
                if(upgradeProbabilities[i]>0.9 || maintainProbabilities[i]>0.9 || downgradeProbabilities[i]>0.9
                        || upgradeProbabilities[i]< 0.11 || maintainProbabilities[i]<0.11 || downgradeProbabilities[i]<0.11)
                    continue;
                if(parent.facilityLevels[i] > child.facilityLevels[i]){
                    upgradeProbabilities[i] -= Constant;
                    maintainProbabilities[i] += Constant/2;
                    downgradeProbabilities[i] += Constant/2;
                }
                else {
                    upgradeProbabilities[i] += Constant*0.25;
                    maintainProbabilities[i] += Constant*0.75;
                    downgradeProbabilities[i] -= Constant;
                }
            }
        }
        else {
            for(int i=0;i<parent.facilityLevels.length;i++){
                if(upgradeProbabilities[i]>0.9 || maintainProbabilities[i]>0.9 || downgradeProbabilities[i]>0.9
                        || upgradeProbabilities[i]< 0.11 || maintainProbabilities[i]<0.11 || downgradeProbabilities[i]<0.11)
                    continue;
                if(parent.facilityLevels[i] > child.facilityLevels[i]){
                    upgradeProbabilities[i] += Constant * 0.25;
                    maintainProbabilities[i] += Constant * 0.75;
                    downgradeProbabilities[i] -= Constant;
                }
                else {
                    upgradeProbabilities[i] -= Constant;
                    maintainProbabilities[i] += Constant/2;
                    downgradeProbabilities[i] += Constant/2;
                }
            }
        }
    }

    private void assignCustomers(Solution solution) {
        int[] facilityLevels = solution.facilityLevels;
        int[] customerAssignment = solution.customerAssignment;
        boolean[] outliers = solution.outliers;

        for (int i = 0; i < customerAssignment.length; i++) {
            int customerLevel = customerLevels[i];
            int bestFacility = -1;
            double bestDistance = Double.MAX_VALUE;

            for (int j = 0; j < facilityLevels.length; j++) {
                if (facilityLevels[j] >= customerLevel) {
                    double distance = distanceMatrix[i][j];
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestFacility = j + 1;
                    }
                }
            }
            customerAssignment[i] = bestFacility;
        }

        markOutliers(solution);
    }

    private void markOutliers(Solution solution) {
        int[] facilityLevels = solution.facilityLevels;
        int[] customerAssignment = solution.customerAssignment;
        boolean[] outliers = solution.outliers;

        // Mark all customers with facility -1 as outliers
        int temp = 0;
        for (int i = 0; i < customerAssignment.length; i++) {
            if (customerAssignment[i] == -1) {
                outliers[i] = true;
                temp++;
            }
            else
                outliers[i] = false;
        }

        // Select the customers with the highest connection costs as outliers
        int[] sortedIndices = IntStream.range(0, customerAssignment.length)
                .filter(i -> customerAssignment[i] != -1)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> -distanceMatrix[i][customerAssignment[i] - 1]))
                .mapToInt(Integer::intValue)
                .toArray();

        int numOutliers = Math.min(sizeO, sortedIndices.length);
        for (int i = 0; i < numOutliers - temp; i++) {
            outliers[sortedIndices[i]] = true;
        }
    }

    private boolean isNoSolutionValid(Solution solution) {
        int[] facilityLevels = solution.facilityLevels;
        int[] customerAssignment = solution.customerAssignment;

        int numInvalidCustomers = 0;

        for (int i = 0; i < customerAssignment.length; i++) {
            int customerLevel = customerLevels[i];
            for (int facilityLevel : facilityLevels) {
                if (facilityLevel < customerLevel){
                    numInvalidCustomers++;
                    customerAssignment[i] = -1;
                    solution.outliers[i] = true;
                }
            }
        }

        return numInvalidCustomers > sizeO;
    }

    private Solution[] selectPopulation(Solution[] population, Solution[] offspring) {
        Solution[] combinedPopulation = Arrays.copyOf(population, population.length + offspring.length);
        System.arraycopy(offspring, 0, combinedPopulation, population.length, offspring.length);
        Arrays.sort(combinedPopulation, Comparator.comparingDouble(solution -> solution.cost));

        return Arrays.copyOfRange(combinedPopulation, 0, population.length);
    }

    private Solution getBestSolution(Solution[] population) {
        return population[0];
    }

    public static class Solution {
        private final int[] facilityLevels;
        private final int[] customerAssignment;
        private final boolean[] outliers;
        private ArrayList<Integer> openFacilities;
        private double cost;

        public ArrayList<Integer> getOpenFacilities() {
            return openFacilities;
        }

        public void setOpenFacilities(ArrayList<Integer> openFacilities) {
            this.openFacilities = openFacilities;
        }

        public Solution(int[] facilityLevels, int[] customerAssignment, boolean[] outliers) {
            this.facilityLevels = facilityLevels;
            this.customerAssignment = customerAssignment;
            this.outliers = outliers;
            this.cost = 0.0;
        }

        public int[] getFacilityLevels() {
            return facilityLevels;
        }

        public int[] getCustomerAssignment() {
            return customerAssignment;
        }

        public boolean[] getOutliers() {
            return outliers;
        }

        public double getCost() {
            return cost;
        }

        public static void printSolution(Solution solution) {
            System.out.println("Facility Levels: " + Arrays.toString(solution.facilityLevels));
            System.out.println("Customer Assignment: " + Arrays.toString(solution.customerAssignment));
            System.out.println("Outliers: " + Arrays.toString(solution.outliers));
            System.out.println("Open: " + solution.openFacilities);
            System.out.println("Cost: " + solution.cost);
        }
    }
}

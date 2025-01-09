# Priority Facility Location Problem with Outliers (PFLPO)

This repository contains the implementation of approximation and heuristic algorithms for solving the Priority Facility Location Problem with Outliers (PFLPO), as presented in our paper published in Tsinghua Science and Technology.

## Key Innovation

The main innovation of this implementation lies in transforming the highly abstract dual-based approximation algorithm into executable code through physical model mapping. The algorithm is visualized as a water-filling system where:

- Dual Variables → Bucket (Storage Capacity & Water Level)
- Linear Programming Constraints → Line (Connection & Flow) 
- Dual Ascent Process → Level (Dual Variable Value)

This mapping makes the abstract algorithm more intuitive and implementable. The seemingly unusual classes in the code (like Bucket, Line etc.) are actually representing components of this physical model to realize the approximation algorithm.

## Overview

The PFLPO is a generalization of both the Facility Location Problem with Outliers (FLPO) and Priority Facility Location Problem (PFLP). The project provides:

- A 3-approximation algorithm using primal-dual technique
- A greedy-based heuristic algorithm  
- A local search algorithm
- Experimental comparisons between these approaches

## Problem Description

In PFLPO, we have:
- A set of facility locations
- A set of client locations 
- A set of service levels
- Each client has a specific service level requirement
- Each facility can be opened at any service level
- Up to q clients can be designated as outliers (not requiring service)
- The goal is to minimize total costs while satisfying service requirements

## Algorithms

### 1. Primal-Dual Algorithm
- Provides a 3-approximation guarantee
- Uses dual ascent process
- Handles outlier selection through systematic approach

### 2. Greedy-Based Algorithm  
- Starts with highest level facility that minimizes total cost
- Iteratively adds facilities based on cost improvement
- Uses efficient outlier selection strategy

### 3. Local Search Algorithm
- Begins with feasible solution
- Explores neighborhood of current solution
- Makes local improvements until local optimum reached

### 4. Genetic Algorithm
- Population-based evolutionary approach
- Uses mutation and selection operations
- Adapts probability parameters based on solution quality

### 5. Simulated Annealing Algorithm
- Probabilistic technique for approximating global optimum
- Gradually decreases temperature parameter
- Allows escaping local optima through probabilistic acceptance

## Implementation Details

The codebase is structured into several key components:

- `Base/` - Core problem definition and utilities
- `Dual/` - Primal-dual approximation algorithm
- `Heuristic/` - Greedy and local search implementations

Key classes:
- `Problem.java` - Problem instance representation
- `DualAlgorithm.java` - Main primal-dual implementation
- `GreedyAlgorithm.java` - Greedy heuristic
- `LocalSearchAlgorithm.java` - Local search method
- `GeneticAlgorithm.java` - Genetic algorithm implementation
- `SimulatedAnnealingAlgorithm.java` - Simulated annealing implementation

## Quick Start

The simplest way to run all algorithms is through the main function:

```java
public class Main {
    public static void main(String[] args) {
        Function.writeIntoFile();
    }
}
```

This will execute all algorithms and write their results into files.

## Usage

For more detailed usage, you can run individual algorithms:

```java
Problem problem = new Problem();

// Run primal-dual algorithm
double dualResult = DualAlgorithm.solve(problem, true);

// Run greedy algorithm
double greedyResult = GreedyAlgorithm.solve(problem);

// Run local search
double localSearchResult = LocalSearchAlgorithm.solve(problem);
```

## Experimental Results

The implementation includes experimental evaluations examining:
- Effect of number of clients
- Effect of number of outliers  
- Effect of number of facilities

Results demonstrate that:
- Primal-dual algorithm generally outperforms heuristics
- Performance varies with problem parameters
- Trade-offs exist between solution quality and runtime

## References

For more details, please refer to our paper:
Luo, H., Han, L., Shuai, T., & Wang, F. (2024). Approximation and Heuristic Algorithms for the Priority Facility Location Problem with Outliers. Tsinghua Science and Technology, 29(6), 1694-1702.

## License

This project is licensed under the terms specified in the included license file.

## Authors

- Hang Luo
- Lu Han
- Tianping Shuai
- Fengmin Wang

Beijing University of Posts and Telecommunications

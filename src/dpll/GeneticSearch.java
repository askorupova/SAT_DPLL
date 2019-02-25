package dpll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticSearch {

	static final int NUM_OF_GENERATIONS = 300;
	static final int NUM_OF_INDIVIDUALS = 300;
	static final int NUM_OF_PARENTS = 20;
	static final int TOURNAMENT_PARTICIPANTS = 30;
	static final double PROBABILITY_OF_MUTATION = 0.01;
	
	Random rnd = new Random();
	
	//fitness value is time in nanoseconds
	long fitness(Formula formula) {
		List<Integer> assignedVars = new ArrayList<Integer>();
		
		for(int i = 0; i < formula.getNbvar(); i++){
			assignedVars.add(Formula.NONE);
		}
		
		Formula copyOfFormula = new Formula();
		copyOfFormula.setNbvar(formula.getNbvar());
		copyOfFormula.setNbclauses(formula.getNbclauses());
		copyOfFormula.createEmptyFormula();
		
		for (int i = 0; i < formula.list.size(); i++){
			for(int j = 0; j < formula.list.get(i).size(); j++) {
				copyOfFormula.list.get(i).set(j, formula.list.get(i).get(j));
			}
		}
		Solver s = new Solver();
		
		long startTime = System.nanoTime(); 
		s.dpll(copyOfFormula, assignedVars);
		long estimatedTime = (System.nanoTime() - startTime)/1000;
		
		return estimatedTime;
	}
	
	long[] countAllFitnesses(List<Formula> generation) {
		
		long[] allFitnesses = new long[NUM_OF_INDIVIDUALS];
		
		for(int i = 0; i < NUM_OF_INDIVIDUALS; i++){
			allFitnesses[i] = fitness(generation.get(i));
		}
		
		return allFitnesses;
	}
	
	long meanFitness(long[] allFitnesses) {
		int sum = 0;
		
		for(int i = 0; i < NUM_OF_INDIVIDUALS; i++) {
			sum += allFitnesses[i];
		}
		
		return (sum / NUM_OF_INDIVIDUALS);
	}
	
	Formula tournament(List<Formula> generation, long[] allFitnesses) {

		long maxFitness = 0;
		Formula best = new Formula();

		for(int i = 0; i < TOURNAMENT_PARTICIPANTS; i++) {
			int n = rnd.nextInt(NUM_OF_INDIVIDUALS);
			long f = allFitnesses[n];
			if(f > maxFitness) {
				maxFitness = f;
				best = generation.get(n);
			}
		}
		return best;
	}
	
	Formula getBestFormula(List<Formula> generation, long[] allFitnesses) {
		Formula best = new Formula();
		long maxFitness = 0;
		int bestIndex = 0;
		
		for(int i = 0; i < NUM_OF_INDIVIDUALS; i++) {
			if(allFitnesses[i] > maxFitness) {
				maxFitness = allFitnesses[i];
				bestIndex = i;
			}
		}
		best = generation.get(bestIndex);
		allFitnesses[bestIndex] = 0;
		
		return best;
	}
	
	Formula crossover(Formula parent1, Formula parent2) {
		int part = parent1.getNbclauses() / 4;
		
		Formula child = new Formula();
		child.setNbclauses(parent1.getNbclauses());
		child.setNbvar(parent1.getNbvar());
		
		/*
		for(int i = 0; i < part; i++) {
			child.list.add(parent1.list.get(i));
		}
		for(int i = part; i < 2*part; i++) {
			child.list.add(parent2.list.get(i));
		}
		for(int i = 2*part; i < 3*part; i++) {
			child.list.add(parent1.list.get(i));
		}
		for(int i = 3*part; i < child.getNbclauses(); i++) {
			child.list.add(parent2.list.get(i));
		}
		*/
		
		for(int i = 0; i < child.getNbclauses(); i++) {
			if(Math.random() < 0.5)
				child.list.add(parent1.list.get(i));
			else {
				child.list.add(parent2.list.get(i));
			}
		}
		
		return child;
	}
	
	void mutation(Formula child) {
		
		for(int i = 0; i < child.getNbclauses(); i++) {
			for(int j = 0; j < child.getNbvar(); j++) {
				if(child.list.get(i).get(j) != Formula.NONE) {
					if(Math.random() < PROBABILITY_OF_MUTATION) {
						if(child.list.get(i).get(j) == Formula.TRUE) 
							child.list.get(i).set(j, Formula.FALSE);
						else
							child.list.get(i).set(j, Formula.TRUE);
					}
				}
			}
		}
	}
	
	public GeneticSearch() {
		
		List<Formula> generation = new ArrayList<Formula>();
		
		for(int i = 0; i < NUM_OF_INDIVIDUALS; i++) {
			Formula formula = new Formula();
			InstanceGenerator.generateNxMFormula(formula, 20, 84);
			generation.add(formula);
		}
		
		for(int i = 0; i < NUM_OF_GENERATIONS; i++) {
		
			long[] allFitnesses = countAllFitnesses(generation);
			
			long meanFitness = meanFitness(allFitnesses)/1000;
			
			System.out.println("mean fitness value in " + i + ". generation :" + meanFitness);
			
			List<Formula> parents = new ArrayList<Formula>();
			List<Formula> children = new ArrayList<Formula>();
			
			for(int j = 0; j < NUM_OF_PARENTS; j++) {
				//parents.add(tournament(generation, allFitnesses));
				parents.add(getBestFormula(generation, allFitnesses));
			}
			for(int j = 0; j < NUM_OF_INDIVIDUALS; j++) {
				Formula parent1 = parents.get(rnd.nextInt(10));
				Formula parent2 = parents.get(rnd.nextInt(10));
				
				children.add(crossover(parent1, parent2));
			}
			for(int j = 0; j < NUM_OF_INDIVIDUALS; j++) {
				mutation(children.get(j));
			}
			generation = children;
		}
	}
}

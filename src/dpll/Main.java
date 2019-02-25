package dpll;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.*;

public class Main {

	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
		
		//generateInstance(67, 283);
		
		//readFromFile("test2.txt");

		//generateInstancesOfVaryingRatios();
		
		GeneticSearch gs = new GeneticSearch();
		
		meanTime(87, 263);
		meanTime(67, 283);
		meanTime(20, 84);
		meanTime(53, 297);
		
	}
	
	public static void meanTime(int n, int m) {
		long sum = 0;
		int measurements = 100;
		long mean;
		int[] satisfiable = new int[1];
		long time;
		long max = 0;
		
		satisfiable[0] = 0;
		
		for(int i = 0; i < measurements; i++) {
			Formula f = new Formula();
			InstanceGenerator.generateNxMFormula(f, n, m);
			time = solveInstance(f, satisfiable);
			if(time > max)
				max = time;
			sum += time;
		}
			
		mean = sum / measurements;
			
		System.out.println(n + "x" + m + " : time in ms: " + mean + " , max: " + max + " , sat: " + satisfiable[0]);
	}
	
	public static long solveInstance(Formula f, int[] satisfiable) {
		List<Integer> assignedVars = new ArrayList<Integer>();
		
		for(int j = 0; j < f.getNbvar(); j++){
			assignedVars.add(Formula.NONE);
		}
		Solver s = new Solver();
		
		
		//if(s.dpll(f, assignedVars) == false)
			//System.out.println("FORMULA NIE JE SPLNITELNA");
		
		long startTime = System.nanoTime(); 
		if (s.dpll(f, assignedVars) && satisfiable != null)
			satisfiable[0]++;
		long estimatedTime = (System.nanoTime() - startTime)/1000000;
		
		return estimatedTime;
	}
	
	public static long generateInstance(int vars, int clauses) {
		Formula f = new Formula();
	
		//generate formula
		InstanceGenerator.generateNxMFormula(f, vars, clauses);
		//print generated formula
		Formula.printDimacsFormula(f.list);
		
		return solveInstance(f, null);
	}
	
	//generates formulas with different clauses to variables ratios and measures their execution time
	public static void generateInstancesOfVaryingRatios() {
		
		//clauses to variables ratios
		List<List<Integer>> ratios = new ArrayList<List<Integer>>();
		
		for(int i = 11; i > 1; i --) {
			ratios.add(asList(1, i));
		}
		for(int i = 1; i <= 10; i ++) {
			ratios.add(asList(i, 1));
		}
		for(int i = 0; i < ratios.size(); i++) {
			long estimatedTime = generateInstance(ratios.get(i).get(0), ratios.get(i).get(1));
			System.out.println("ratio: " + ratios.get(i).get(0) + "/" +  ratios.get(i).get(1) + " time: " + estimatedTime + " ms");
		}
		
	}
	
	//reads formula from file
	public static void readFromFile(String fileName) {
		Formula f = new Formula();
		
		try {
			f.readFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solveInstance(f, null);
	}
}

package dpll;

import java.util.Random;

public class InstanceGenerator {
	
	static final int N = 300;
	static final double PROBABILITY = 0.5;
			
	public static void generateFormula(Formula formula, int clausesRatio, int varsRatio) {
		/*
		int bigger_ratio = (clausesRatio > varsRatio) ? clausesRatio : varsRatio;
		int x = (int)Math.sqrt((MULTIPLICATOR * MULTIPLICATOR) / bigger_ratio);
		
		formula.setNbclauses(clausesRatio * x);
		formula.setNbvar(varsRatio * x);
		*/
		
		int x = N / (clausesRatio + varsRatio);
		
		formula.setNbclauses(clausesRatio * x);
		formula.setNbvar(varsRatio * x);
		
		formula.createEmptyFormula();
		
		for (int i = 0; i < formula.getNbclauses(); i++) {
			for (int j = 0; j < formula.getNbvar(); j ++) {
				if(Math.random() < PROBABILITY) {
					if(Math.random() < PROBABILITY) {
						formula.list.get(i).set(j, Formula.FALSE);
					}
					else {
						formula.list.get(i).set(j, Formula.TRUE);
					}
				}
			}
		}
	}
	
	public static void generateNxMFormula(Formula formula, int n, int m) {
		
		Random rand = new Random();
		
		formula.setNbvar(n);
		formula.setNbclauses(m);
		
		formula.createEmptyFormula();
		
		for (int i = 0; i < formula.getNbclauses(); i++) {
			for (int j = 0; j < 3; j ++) {
				int x = rand.nextInt(formula.getNbvar());
				if(Math.random() < PROBABILITY) {
					formula.list.get(i).set(x, Formula.FALSE);
				}
				else {
					formula.list.get(i).set(x, Formula.TRUE);
				}
			}
		}
	}
}

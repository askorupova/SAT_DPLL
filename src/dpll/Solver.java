package dpll;

import java.util.ArrayList;
import java.util.List;

public class Solver {
	
	public int negation(int n) {
		if(n == Formula.TRUE)
			return Formula.FALSE;
		else
			return Formula.TRUE;
	} 
	
	/* returns:
	 * -1 empty clause
	 * 1 unit clause was found
	 * 0 unit clause was not found */
	
	public int unitPropagation(List<List<Integer>> formula, List<Integer> unitLiterals) {
		int i, j;
		int n = 0;
		int unit = -1;
		int unitValue = -1;
		
		for (i = 0; i < formula.size(); i ++) {
			for (j = 0; j < formula.get(0).size(); j ++) {
				if(formula.get(i).get(j) != Formula.NONE) {
					n ++;
					unit = j;
				}
			}		
			if(n == 1) {
				if(!unitLiterals.contains(unit)) {
					unitValue = formula.get(i).get(unit);
					break;
				}
			}
			else if(n == 0) {
				return -1;
			}
			n = 0;
		}
		
		if(unitValue != -1) {
			for (i = 0; i < formula.size(); i ++) {
				if(formula.get(i).get(unit) == negation(unitValue))
					formula.get(i).set(unit, Formula.NONE);
			}
			unitLiterals.add(unit);
			return 1;
		}
		return 0;
	}
	
	/* returns:
	 * 2 formula is empty (is satisfiable)
	 * 1 pure literal was found
	 * 0 p. l. was not found */
	
	public int pureLiteralElimination(List<List<Integer>> formula, List<Integer> assignedVars) {
		int i, j;
		int pureLiteral = -1;
		int pureLiteralValue = Formula.NONE;
		boolean found = false; 
		
		for (j = 0; j < formula.get(0).size(); j ++) {
			for (i = 0; i < formula.size(); i ++) {
				if(formula.get(i).get(j) != Formula.NONE){
					if(pureLiteralValue == Formula.NONE) {
						pureLiteralValue = formula.get(i).get(j);
						found = true;
					}
					else if (formula.get(i).get(j) != pureLiteralValue){
						found = false;
					}
				}
			}
			if (found) {
				pureLiteral = j;
				break;
			}
			pureLiteralValue = Formula.NONE;
			found = false;
		}
		if (found){
			assignedVars.set(pureLiteral, pureLiteralValue);

			for (i = 0; i < formula.size(); i ++) {
				if(formula.get(i).get(pureLiteral) == pureLiteralValue) {
					formula.remove(i);
					if(formula.isEmpty())
						return 2;
					i --;
				}
			}
			return 1;
		}
		return 0;
	}
	
	public boolean dpll(Formula f, List<Integer> assignedVars) {
		
		List<List<Integer>> formula = f.list;
		List<Integer> unitLiterals = new ArrayList<Integer>();
		List<Integer> copyOfAssignedVars = new ArrayList<Integer>();

		int result = unitPropagation(formula, unitLiterals);
		
		while(result == 1) {
			result = unitPropagation(formula, unitLiterals);
		}
		
		if(result == -1) {
			return false;
		}
		
		result = pureLiteralElimination(formula, assignedVars);
		
		while(result == 1) {
			result = pureLiteralElimination(formula, assignedVars);
		}
		
		if(result == 2)  {
			/*
			System.out.println("FORMULA JE SPLNITELNA");
			System.out.println("najdene ohodnotenie premennych: ");
			int var;
			for (int i = 0; i < assignedVars.size(); i ++) {
				var = assignedVars.get(i); 
				if(var != Formula.NONE) {
					
					if(var == Formula.FALSE)
						System.out.println((i + 1) * (-1));
					else
						System.out.println(i + 1);
				}
			}*/
			return true;
		}

		int pickedVar = 0;
		
		for(int i = 0; i < f.getNbvar(); i ++) {
			if(assignedVars.get(i) == Formula.NONE) {
				pickedVar = i;
				break;
			}
		}
		
		//new row at the end of formula list containing picked variable
		List<Integer> newRowTrue = new ArrayList<Integer>();
		List<Integer> newRowFalse = new ArrayList<Integer>();
		
		for(int i = 0; i < f.getNbvar(); i++) {
			newRowTrue.add(Formula.NONE);
			newRowFalse.add(Formula.NONE);
		}
		
		newRowTrue.set(pickedVar, Formula.TRUE);
		newRowFalse.set(pickedVar, Formula.FALSE);
		
		List<List<Integer>> list1 = new ArrayList<List<Integer>>();
		List<List<Integer>> list2 = new ArrayList<List<Integer>>();
		
		//copy formula to list1 and list2
		for (int i = 0; i < formula.size(); i++){
			List<Integer> l1 = new ArrayList<Integer>();
			List<Integer> l2 = new ArrayList<Integer>();
			for(int j = 0; j < formula.get(i).size(); j++) {
				l1.add(formula.get(i).get(j));
				l2.add(formula.get(i).get(j));
			}
			list1.add(l1);
			list2.add(l2);
		}

		list1.add(newRowTrue);
		list2.add(newRowFalse);
		
		Formula formula1 = new Formula();
		Formula formula2 = new Formula();
		
		formula1.createFormula(list1);
		formula2.createFormula(list2);

		for (int i = 0; i < assignedVars.size(); i++) {
			copyOfAssignedVars.add(assignedVars.get(i));
		}
		
		return (dpll(formula1, assignedVars) || dpll(formula2, copyOfAssignedVars));
	}
}

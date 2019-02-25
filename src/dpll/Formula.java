package dpll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Formula {

	static final int TRUE = 1;
	static final int FALSE = 0;
	static final int NONE = 8;
	
	List<List<Integer>> list = new ArrayList<List<Integer>>();
	
	private int nbvar = 0;
	private int nbclauses = 0;
	
	public void readFile(String file_name) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(file_name));	
		String line = br.readLine();
		String clause[] = line.split(" ");

		while (clause[0].equals("c")){
			line = br.readLine();
			clause = line.split(" ");
		}

		nbvar = Integer.parseInt(clause[2]);
		nbclauses = Integer.parseInt(clause[3]);
		
		System.out.println(nbvar + " " + nbclauses);
		
		createEmptyFormula();
		
		line = br.readLine();
		int row = 0;
		
		while(line != null) {
			clause = line.split(" ");
			int i = 0;
			int var = Integer.parseInt(clause[i]);
			while (var != 0) {
				i++;
				if(var < 0)
					list.get(row).set(Math.abs(var) - 1, FALSE);
				else 
					list.get(row).set(Math.abs(var) - 1, TRUE);
				//System.out.println(var);
				//System.out.println(clause[i]);
				var = Integer.parseInt(clause[i]);
			}
			row ++;			
			line = br.readLine();
		}
		if(br != null)
			br.close();
	}
	
	public void createEmptyFormula() {
		for (int i = 0; i < nbclauses; i ++){
			List<Integer> l = new ArrayList<Integer>();
			for(int j = 0; j < nbvar; j++) {
				l.add(NONE);
			}
			list.add(l);
		}
		
		if(nbclauses != list.size())
			System.out.println("ahoj, chyba");
	}
	
	public void createFormula(List<List<Integer>> list) {
		this.list = list;
		this.nbvar = list.get(0).size();
		this.nbclauses = list.size();
	}

	public int getNbvar() {
		return nbvar;
	}

	public void setNbvar(int nbvar) {
		this.nbvar = nbvar;
	}

	public int getNbclauses() {
		return nbclauses;
	}

	public void setNbclauses(int nbclauses) {
		this.nbclauses = nbclauses;
	}
	
	static void printFormula(List<List<Integer>> formula) {
		for (int i = 0; i < formula.size(); i++){
			for(int j = 0; j < formula.get(0).size(); j++) {
				System.out.print(formula.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static void printDimacsFormula (List<List<Integer>> formula) {
		for (int i = 0; i < formula.size(); i++){
			for(int j = 0; j < formula.get(0).size(); j++) {
				int n = formula.get(i).get(j);
				if(n != Formula.NONE) {
					int var;
					if(n == Formula.TRUE)
						var = j + 1;
					else 
						var = (j + 1) * (-1); 
					System.out.print(var + " ");
				}
			}
			System.out.println("0");
		}
		System.out.println();
	}
}

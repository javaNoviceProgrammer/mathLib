package tests;

import mathLib.sfg.numeric.SFG;
import mathLib.util.StringUtils;

public class TestSFG3 {
	
	public static void main(String[] args) {
		String[] nodes = {"A", "B", "C", "D", "I", "O"} ;
		SFG sfg = new SFG(StringUtils.toArrayList(nodes)) ;
		sfg.addArrow("I", "A", 1) ;
		sfg.addArrow("A", "C", 1);
		sfg.addArrow("C", "B", 1);
		sfg.addArrow("B", "B", 1);
		sfg.addArrow("B", "D", 1);
		sfg.addArrow("D", "C", 1);
		sfg.addArrow("C", "C", 1);
		sfg.addArrow("D", "O", 1);
		sfg.addArrow("A", "D", 1);
		sfg.addArrow("B", "C", 1);
		System.out.println(sfg.getGain("I", "O"));
		System.out.println(sfg.printForwardPaths_noGains());
		System.out.print(sfg.printAllLoops_compactForm());
		System.out.println(sfg.printDelta_compactForm());
		System.out.println(sfg.printCofactors());
//		System.out.println(sfg.printDelta(0));
//		System.out.println(sfg.printDelta(1));
	}

}

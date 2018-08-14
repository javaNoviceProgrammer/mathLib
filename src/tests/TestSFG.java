package tests;

import static edu.uta.futureye.function.FMath.C;
import static edu.uta.futureye.function.FMath.s;

import mathLib.sfg.symbolic.SFG;
import mathLib.util.StringUtils;

public class TestSFG {
	public static void main(String[] args) {
		String[] nodes = {"I", "A", "B", "C", "D", "O"} ;
		SFG sfg = new SFG(StringUtils.toArrayList(nodes)) ;
		sfg.addArrow("I", "A", C(1));
		sfg.addArrow("A", "B", s);
		sfg.addArrow("B", "C", s);
		sfg.addArrow("C", "A", C(-1));
		sfg.addArrow("A", "D", C(2));
		sfg.addArrow("D", "C", s);
		sfg.addArrow("C", "O", C(1));
		sfg.buildForwardPaths("I", "O");
		
		System.out.println(sfg.getGain());
		
		System.out.println(sfg.printAllLoops_compactForm());
	}

}

package tests;

import mathLib.sfg.numeric.SFG;
import mathLib.util.StringUtils;
import static mathLib.numbers.Complex.*;

public class TestSFG2 {
	public static void main(String[] args) {


		String[] nodes = {"I", "A", "B", "C", "D", "O"} ;
		SFG sfg = new SFG(StringUtils.toArrayList(nodes)) ;
		sfg.supressAll(false);
		sfg.addArrow("I", "A", 1);
		sfg.addArrow("A", "B", j);
		sfg.addArrow("B", "C", 2+j);
		sfg.addArrow("C", "A", -1);
		sfg.addArrow("A", "D", 2);
		sfg.addArrow("D", "C", null);
		sfg.addArrow("C", "O", 1);
		sfg.buildForwardPaths("I", "O");

		System.out.println(sfg.getGain());

		System.out.println(sfg.printAllLoops_compactForm());
	}

}

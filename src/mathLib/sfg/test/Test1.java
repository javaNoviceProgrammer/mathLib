package mathLib.sfg.test;

import edu.uta.futureye.function.FMath;
import mathLib.sfg.SFG;
import mathLib.utils.StringUtils;

public class Test1 {
	public static void main(String[] args) {
		String[] nodes = { "a", "x", "b" };
		SFG sfg = new SFG(StringUtils.toArrayList(nodes));
		sfg.addArrow("a", "x", FMath.C1);
		sfg.addArrow("x", "b", FMath.s);
		sfg.addArrow("x", "x", FMath.s);
		System.out.println(sfg.printAllNodes());
		System.out.println(sfg.printAllLoops());
		System.out.println(sfg.getGain("a", "b"));
	}

}

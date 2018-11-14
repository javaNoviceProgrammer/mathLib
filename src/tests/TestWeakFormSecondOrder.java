package tests;

import static edu.uta.futureye.function.FMath.C;
import static edu.uta.futureye.function.FMath.sin;

import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.ode.WeakFormSecondOrder;

public class TestWeakFormSecondOrder {
	// for test
	public static void main(String[] args) {
		MathFunc a = C(1.0)*FMath.x*FMath.x ;
		MathFunc b = FMath.x ;
		MathFunc c = C(0.0) ;
		MathFunc f = sin(FMath.x)*FMath.x*FMath.x ;
		WeakFormSecondOrder wf = new WeakFormSecondOrder(a, b, c, f) ;
		wf.setDegub(true);
		wf.setGridSize(100);
		wf.setBoundary(1.0, 10.0);
		wf.setBoundaryValue(0.0, -2.0);
		wf.solve();
		wf.plotSolution();
	}
}

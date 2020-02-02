package tests.test_ode;

import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.Ode;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestFehlberg {

	public static void main(String[] args) {
		test1() ;
	}

	public static void test1() {
		// y' = y^2 , y(0) = 1
		DerivFunction func = (x,y) -> -y*y ;
		double x0 = 0.0 ;
		double y0 = 1.0 ;
		OdeSolver odeSolver = Ode.getSolver(func, x0, y0) ;
		double[] x = MathUtils.linspace(x0, x0+50.0, 1000) ;
		double[] y1 = odeSolver.fehlberg(x) ;
		double[] y2 = odeSolver.rungeKutta(x) ;
		double[] y3 = odeSolver.euler(x) ;
		double[] y4 = odeSolver.midpoint(x) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y1, "b");
		fig.plot(x, y2, "r");
		fig.plot(x, y3, "g");
		fig.plot(x, y4, "k");
		fig.renderPlot();
		fig.show(true);
	}

}

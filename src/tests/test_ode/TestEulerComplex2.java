package tests.test_ode;

import static mathLib.numbers.Complex.j;
import static mathLib.numbers.ComplexMath.* ;
import mathLib.numbers.Complex;
import mathLib.ode.solvers.DerivFunctionComplex;
import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSolverComplex;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestEulerComplex2 {

	public static void main(String[] args) {
		test1() ;
		test2();
	}

	public static void test1() {
		// y' = -y^2 for complex function
		DerivnFunction func = (x, z) -> new double[] {(-z[0]*z[0]+z[1]*z[1])*(x<1? 0.0 : 1.0),
													   -2.0*z[0]*z[1]*(x<1? 0.0 : 1.0)} ;
		double x0 = 0;
		Complex y0 = 1.0 + j * 1.0;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0.re(), y0.im()) ;

		double[] xvals = MathUtils.linspace(0.0, 20.0, 1000) ;
		double[][] yvals = odeSolver.rungeKutta(xvals) ;

		MatlabChart fig = new MatlabChart();
		fig.plot(xvals, yvals[0], "b");
		fig.plot(xvals, yvals[1], "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Re(y), Im(y)");
		fig.font(17);
		fig.show(true);
	}

	public static void test2() {
		// y' = -y^2 for complex function
		DerivFunctionComplex func = (x, y) -> -pow(y, 2.0) * (x<1? 0.0 : 1.0) ;
		double x0 = 0;
		Complex y0 = 1.0 + j * 1.0;
		OdeSolverComplex odeSolver = new OdeSolverComplex(func, x0, y0);

		double[] xvals = MathUtils.linspace(0.0, 20.0, 1000) ;
		double[][] yvals = odeSolver.rungeKutta(xvals) ;

		MatlabChart fig = new MatlabChart();
		fig.plot(xvals, yvals[0], "b");
		fig.plot(xvals, yvals[1], "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Re(y), Im(y)");
		fig.font(17);
		fig.show(true);
	}

}

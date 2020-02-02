package tests.test_ode;

import static java.lang.Math.PI ;
import static mathLib.numbers.Complex.j;

import mathLib.func.ArrayFunc;
import mathLib.numbers.Complex;
import mathLib.ode.solvers.DerivnFunctionComplex;
import mathLib.ode.solvers.OdeSystemSolverComplex;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestEulerSystemComplex {

	public static void main(String[] args) {
		test1() ;
	}

	public static void test1() {
		// z[0]' = -z[1] , z[1]' = z[2] : oscillatory solutions
		DerivnFunctionComplex func = (x, z) -> new Complex[] {-z[1], z[0]} ;
		// initial conditions
		double x0 = 0.0 ;
		Complex[] y0 = {2.0*j, 1.0+j} ;
		OdeSystemSolverComplex odeSolver = new OdeSystemSolverComplex(func, x0, y0) ;
		double[] xvals = MathUtils.linspace(0.0, 4.0*PI, 1000) ;
		double[] xExact = MathUtils.linspace(0.0, 4.0*PI, 50) ;
		double[][] z = odeSolver.rungeKutta(xvals) ;

//		double[] y1Real = z[0] ;
//		double[] y1Imag = z[1] ;
		double[] y2Real = z[2] ;
		double[] y2Imag = z[3] ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, y2Real, "b", 3f, "RK4 Re");
		fig.plot(xExact, ArrayFunc.apply(t -> Math.cos(t), xExact), "r", 2f, "Exact Re");
		fig.plot(xvals, y2Imag, "k", 3f, "RK4 Im");
		fig.plot(xExact, ArrayFunc.apply(t -> 2*Math.sin(t)+Math.cos(t), xExact), "g", 2f, "Exact Im");
		fig.renderPlot();
		fig.markerON(1);
		fig.setFigLineWidth(1, 0f);
		fig.markerON(3);
		fig.setFigLineWidth(3, 0f);
		fig.xlabel("X values");
		fig.ylabel("y2(x) solution");
		fig.show(true);
	}

}

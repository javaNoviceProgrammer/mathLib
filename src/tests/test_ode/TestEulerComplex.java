package tests.test_ode;

import static mathLib.numbers.Complex.j;
import static mathLib.numbers.ComplexMath.* ;

import mathLib.arrays.NdArray;
import mathLib.func.ArrayFunc;
import mathLib.numbers.Complex;
import mathLib.ode.solvers.DerivFunctionComplex;
import mathLib.ode.solvers.OdeSolverComplex;
import mathLib.plot.MatlabChart;
import mathLib.sequence.ArraySequence;
import mathLib.util.MathUtils;
import mathLib.util.Timer;


public class TestEulerComplex {

	public static void main(String[] args) {
		// test1() ;
		test2();
	}

	public static void test2() {
		// y' = -y^2 for complex function
		DerivFunctionComplex func = (x, y) -> -pow(y, 2.0) ;
		double x0 = 0;
		Complex y0 = 1.0 + j * 1.0;
		OdeSolverComplex odeSolver = new OdeSolverComplex(func, x0, y0);

		double[] xvals = MathUtils.linspace(0.0, 20.0, 50) ;
		double[][] yvals = odeSolver.rungeKutta(xvals) ;
		double[] x = MathUtils.linspace(0.0, 20.0, 1000) ;
		double[] yrealExact = ArrayFunc.apply(t -> (1.0/(t+0.5*(1-j))).re(), x) ;
		double[] yimagExact = ArrayFunc.apply(t -> (1.0/(t+0.5*(1-j))).im(), x) ;

		MatlabChart fig = new MatlabChart();
		fig.plot(x, yrealExact, "m");
		fig.plot(x, yimagExact, "k");
		fig.plot(xvals, yvals[0], "b");
		fig.plot(xvals, yvals[1], "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Re(y), Im(y)");
		fig.font(17);
		fig.markerON(2);
		fig.setFigLineWidth(2, 0f);
		fig.markerON(3);
		fig.setFigLineWidth(3, 0f);
		fig.setFigLineWidth(0, 3f);
		fig.setFigLineWidth(1, 3f);
		fig.show(true);
	}

	public static void test1() {
		// y' = -y^2 for complex function
		DerivFunctionComplex func = (x, y) -> -pow(y, 2.0);
		double x0 = 0.0;
		Complex y0 = 1.0 + j * 1.0;
		OdeSolverComplex odeSolver = new OdeSolverComplex(func, x0, y0);
		ArraySequence yn = odeSolver.eulerSequence(1.5);

		Timer timer = new Timer();
		timer.start();

		double[] indices = new double[100];
		Complex[] ynVals = new Complex[indices.length];
		NdArray temp = null;
		for (int i = 0, len = indices.length; i < len; i++) {
			indices[i] = i;
			temp = yn.evaluate(i);
			ynVals[i] = temp.at(0) + j * temp.at(1);
		}

		timer.stop();
		timer.show();

		MatlabChart fig = new MatlabChart();
		fig.plot(indices, ynVals);
		fig.renderPlot();
		fig.xlabel("Number of partitions");
		fig.ylabel("Re(y), Im(y)");
		fig.legendON();
		fig.font(17);
		fig.markerON();
		fig.show(true);
	}

}

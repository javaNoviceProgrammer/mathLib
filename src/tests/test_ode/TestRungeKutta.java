package tests.test_ode;

import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.Sequence;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class TestRungeKutta {

	public static void main(String[] args) {
//		test1() ;
		test2() ;
	}

	public static void test2() {
		DerivFunction func = (x,y) -> -y ;
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ;

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(0.0, 50.0, 1000) ;
		double[] yExact = ArrayFunc.apply(t -> Math.exp(-t), x) ;
		double[] yEuler = odeSolver.rungeKutta(x) ;

		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(x, yEuler, "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Function") ;
		fig.setFigLineWidth(1, 2f);
		fig.font(17);
		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void test1() {
		DerivFunction func = (x,y) -> -y ;
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ;
		Sequence yn = odeSolver.rungeKuttaSequence(0.5) ;

		double[] indices = new double[100] ;
		double[] ynVals = new double[indices.length] ;
		for(int i=0; i<indices.length; i++) {
			indices[i] = i ;
			ynVals[i] = yn.evaluate(i) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, ynVals);
		fig.renderPlot();
		fig.markerON();
		fig.xlabel("Number of Partitions");
		fig.ylabel("y(0.5) Value") ;
		fig.show(true);
	}

}

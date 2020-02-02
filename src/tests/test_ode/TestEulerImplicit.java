package tests.test_ode;

import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.Sequence;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class TestEulerImplicit {

	public static void main(String[] args) {
//		test1() ;
		test2() ;
	}

	public static void test2() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1

		double[] x = MathUtils.linspace(0.0, 50.0, 1000) ;
		double[] xEuler = MathUtils.linspace(0.0, 50.0, 1000) ;
		double[] yExact = ArrayFunc.apply(t -> Math.exp(-t), x) ;
		double[] yEuler = new double[xEuler.length] ; // explicit euler method
		double[] yEulerImplicit = new double[xEuler.length] ; // implicit euler method
		for(int i=0, len=xEuler.length; i<len; i++) {
			yEuler[i] = odeSolver.eulerSequence(xEuler[i]).evaluate(100) ;
			yEulerImplicit[i] = odeSolver.eulerImplicitSequence(xEuler[i]).evaluate(100) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(xEuler, yEuler, "r");
		fig.plot(xEuler, yEulerImplicit, "g");
		fig.renderPlot();
		fig.xlabel("x values");
		fig.ylabel("y(x) solution") ;
		fig.font(20);
		fig.setFigLineWidth(0, 2f);
		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void test1() {
		Timer timer = new Timer() ;
		timer.start();
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1
		Sequence yn = odeSolver.eulerImplicitSequence(0.5) ; // looking for y(0.5)

		double[] indices = new double[100] ;
		double[] ynVals = new double[indices.length] ;
		for(int i=0; i<indices.length; i++) {
			indices[i] = i ;
			ynVals[i] = yn.evaluate(i) ;
		}
		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, ynVals);
		fig.renderPlot();
		fig.markerON();
		fig.xlabel("Number of Partitions");
		fig.ylabel("y(0.5) Value") ;
		fig.show(true);
	}

}

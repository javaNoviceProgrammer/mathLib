package tests.test_ode;

import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.Sequence;

public class TestMidpoint {

	public static void main(String[] args) {
//		test1() ;
		test2() ;
	}

	public static void test2() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1
		Sequence ynEuler = odeSolver.eulerSequence(0.5) ; // looking for y(0.5) --> 1/n
		Sequence ynMidpiont = odeSolver.midpointSequence(0.5) ; // looking for y(0.5) --> 1/n^2
		Sequence ynRungeKutta = odeSolver.rungeKuttaSequence(0.5) ; // looking for y(0.5) --> 1/n^4

		double exact = Math.exp(-0.5) ;
		double[] indices = new double[100] ;
		double[] ynEulerVals = new double[indices.length] ;
		double[] ynMidpointVals = new double[indices.length] ;
		double[] ynRungeKuttaVals = new double[indices.length] ;
		for(int i=0; i<indices.length; i++) {
			indices[i] = i ;
			ynEulerVals[i] = Math.abs(ynEuler.evaluate(i)-exact) ; // absolute error
			ynMidpointVals[i] = Math.abs(ynMidpiont.evaluate(i)-exact) ;
			ynRungeKuttaVals[i] = Math.abs(ynRungeKutta.evaluate(i)-exact) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, ynEulerVals, "b");
		fig.plot(indices, ynMidpointVals, "r");
		fig.plot(indices, ynRungeKuttaVals, "m");
		fig.renderPlot();
		fig.markerON();
		fig.xlabel("Number of Partitions");
		fig.ylabel("Absolute Error") ;
		fig.setYAxis_to_Log();
		fig.font(17);
		fig.show(true);
	}

	public static void test1() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1
		Sequence ynEuler = odeSolver.eulerSequence(0.5) ; // looking for y(0.5) --> 1/n
		Sequence ynMidpiont = odeSolver.midpointSequence(0.5) ; // looking for y(0.5) --> 1/n^2

		double[] indices = new double[100] ;
		double[] ynEulerVals = new double[indices.length] ;
		double[] ynMidpointVals = new double[indices.length] ;
		for(int i=0; i<indices.length; i++) {
			indices[i] = i ;
			ynEulerVals[i] = ynEuler.evaluate(i) ;
			ynMidpointVals[i] = ynMidpiont.evaluate(i) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, ynEulerVals, "b");
		fig.plot(indices, ynMidpointVals, "r");
		fig.renderPlot();
		fig.markerON();
		fig.xlabel("Number of Partitions");
		fig.ylabel("y(0.5) Value") ;
		fig.show(true);
	}

}

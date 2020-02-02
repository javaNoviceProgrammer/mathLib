package tests.test_ode;


import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.Sequence;
import mathLib.util.MathUtils;
import mathLib.util.Timer;


public class TestEuler {

	public static void example7() {
		DerivFunction func = (x,y) -> -y*y ; // y' = -y^2
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(0.0, 200.0, 2000) ;
		double[] xEuler = MathUtils.linspace(0.0, 200.0, 50) ;
		double[] yExact = ArrayFunc.apply(t -> 1.0/(1.0+t), x) ;
		double[] yEuler = odeSolver.euler(xEuler) ;

		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(xEuler, yEuler, "r");
		fig.renderPlot();
		fig.xlabel("x values");
		fig.ylabel("y(x) solution") ;
		fig.markerON(1);
		fig.setFigLineWidth(1, 0f);
		fig.setFigLineWidth(0, 3f);
		fig.font(20);
		fig.show(true);
	}

	public static void example6() {
		DerivFunction func = (x,y) -> y/x ; // y' = y/x
		OdeSolver odeSolver = new OdeSolver(func, 1.0, 2.0) ; // y(1) = 2

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(1.0, 1000.0, 2000) ; // y(x) for x>1
		double[] yExact = ArrayFunc.apply(t -> 2.0*t, x) ;
//		double[] yEuler = odeSolver.euler(x, false) ;
		double[] yEuler = odeSolver.euler(x) ;
		double[] error = new double[x.length] ;
		for(int i=0, len=x.length; i<len; i++) {
			error[i] = Math.abs(yExact[i]-yEuler[i]) ;
		}


		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, error, "b");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Error") ;
//		fig.markerON(1);
//		fig.setFigLineWidth(1, 0f);
		fig.font(17);
//		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void example5() {
		DerivFunction func = (x,y) -> y/x ; // y' = y/x
		OdeSolver odeSolver = new OdeSolver(func, 1.0, 2.0) ; // y(1) = 2

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(1.0, 10.0, 20) ; // y(x) for x>1
		double[] yExact = ArrayFunc.apply(t -> 2.0*t, x) ;
		double[] yEuler = odeSolver.euler(x) ;

		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(x, yEuler, "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Function") ;
		fig.markerON(1);
		fig.setFigLineWidth(1, 0f);
		fig.font(17);
	//		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void example4() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(0.0, 80.0, 1000) ;
		double[] yExact = ArrayFunc.apply(t -> Math.exp(-t), x) ;
		double[] yEuler = odeSolver.euler(x) ;

		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(x, yEuler, "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Function") ;
//		fig.markerON(1);
		fig.setFigLineWidth(1, 2f);
		fig.font(17);
		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void example3() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1

		Timer timer = new Timer() ;
		timer.start();
		double[] x = MathUtils.linspace(0.0, 50.0, 1000) ;
		double[] yExact = new double[x.length] ;
		double[] yEuler = new double[x.length] ;
		for(int i=0, len = x.length; i<len; i++) {
			yExact[i] = Math.exp(-x[i]) ;
			yEuler[i] = odeSolver.euler(x[i]) ; // calculate y(x) always based on y(0)=1
		}

		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, yExact, "b");
		fig.plot(x, yEuler, "r");
		fig.renderPlot();
		fig.xlabel("X values");
		fig.ylabel("Function") ;
//		fig.markerON(1);
		fig.setFigLineWidth(1, 2f);
		fig.font(17);
		fig.setYAxis_to_Log();
		fig.show(true);
	}

	public static void example2() {
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1
		Sequence yn = odeSolver.eulerSequence(5) ; // y(x=5) = ?
		Sequence ynRichardson = yn.richardson4() ;
		double exact = Math.exp(-5) ;

		double[] indices = new double[100] ;
		double[] ynVals = new double[indices.length] ;
		double[] ynRichardsonVals = new double[indices.length] ;
		for(int i=0; i<indices.length; i++) {
			indices[i] = i ;
			ynVals[i] = Math.abs(yn.evaluate(i)-exact) ;
			ynRichardsonVals[i] = Math.abs(ynRichardson.evaluate(i)-exact) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, ynVals, "b");
		fig.plot(indices, ynRichardsonVals, "r");
		fig.renderPlot();
		fig.markerON();
		fig.xlabel("Number of Partitions");
		fig.ylabel("Error") ;
		fig.setYAxis_to_Log();
		fig.font(13);
		fig.show(true);
	}

	public static void example1() {
		Timer timer = new Timer() ;
		timer.start();
		DerivFunction func = (x,y) -> -y ; // y' = -y
		OdeSolver odeSolver = new OdeSolver(func, 0.0, 1.0) ; // y(0) = 1
		Sequence yn = odeSolver.eulerSequence(0.5) ; // looking for y(0.5)

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

	public static void main(String[] args) {
//		example1() ;
//		example2() ;
//		example3() ;
//		example4() ;
//		example5() ;
//		example6() ;
		example7() ;
	}

}

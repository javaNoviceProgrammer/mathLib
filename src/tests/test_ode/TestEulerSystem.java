package tests.test_ode;

import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.ArraySequence;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class TestEulerSystem {

	public static void main(String[] args) {
//		test1() ;
//		test2() ;
		example2() ;
	}

	public static void example2() {
		DerivnFunction func = (x, y) -> new double[]{y[1], -y[0]} ; // y'[0] = y[1], y'[1] = -y[0]
		double x0 = 0.0 ;
		double[] y0 = {0.0, 1.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0) ;

		Timer timer = new Timer() ;
		timer.start();

		double[] x = MathUtils.linspace(0.0, 50.0, 1000) ;
		double[] y1Exact = ArrayFunc.apply(t -> Math.sin(t), x) ;
		double[] y2Exact = ArrayFunc.apply(t -> Math.cos(t), x) ;
		double[] xEuler = MathUtils.linspace(0.0, 50.0, 400) ;
		double[][] yEuler = odeSolver.euler(xEuler) ;

		timer.stop();
		timer.show();

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y1Exact, "b");
		fig.plot(xEuler, yEuler[0], "r");
		fig.plot(x, y2Exact, "k");
		fig.plot(xEuler, yEuler[1], "g");
		fig.renderPlot();
		fig.markerON(1);
		fig.setFigLineWidth(1, 0f);
		fig.markerON(3);
		fig.setFigLineWidth(3, 0f);
		fig.xlabel("x values");
		fig.ylabel("y(x) solution");
		fig.font(15);
		fig.show(true);
	}

	public static void test1() {
		DerivnFunction func = (x, y) -> new double[]{x, 1.0} ; // y'[0] = x, y'[1] = 1.0
		double x0 = 0.0 ;
		double[] y0 = {1.0, 1.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0) ;
		ArraySequence seq0 = odeSolver.eulerSequence(4.0) ;

		double[] indices = new double[100] ;
		double[] y0Vals = new double[indices.length] ;
		for(int i=0, len=indices.length; i<len; i++) {
			indices[i] = i ;
			y0Vals[i] = seq0.evaluate(i).at(0) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(indices, y0Vals);
		fig.renderPlot();
		fig.show(true);
	}

	public static void test2() {
		DerivnFunction func = (x, y) -> new double[]{x*x, 1.0} ; // y'[0] = x, y'[1] = 1.0
		double x0 = 0.0 ;
		double[] y0 = {1.0, 1.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0) ;

		Timer timer = new Timer() ;
		timer.start();

		double[] x = MathUtils.linspace(1, 50.0, 10000) ;
		double[][] y0values = odeSolver.euler(x) ;
		double[] exact = ArrayFunc.apply(t -> t*t*t/3.0+1, x) ;
//		double[] exact = ArrayFunc.apply(t -> t+1, x) ;
		double[] error = new double[x.length] ;
		for(int i=0, len=x.length; i<len; i++)
			error[i] = Math.abs(y0values[0][i]-exact[i]) ;
		timer.stop();
		timer.show();
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, error, "b");
//		fig.plot(x, y0values, "r");
		fig.renderPlot();
//		fig.setYAxis_to_Log();
		fig.show(true);
	}




}

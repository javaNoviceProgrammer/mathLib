package tests.test_ode;

import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.plot.MatlabChart;
import mathLib.sequence.ArraySequence;
import mathLib.util.MathUtils;

public class TestRungeKuttaSystem {

	public static void main(String[] args) {
//		test1() ;
		test2() ;
	}

	public static void test1() {
		DerivnFunction func = (x, y) -> new double[]{x, 1.0} ; // y'[0] = x, y'[1] = 1.0
		double x0 = 0.0 ;
		double[] y0 = {1.0, 1.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0) ;
//		ArraySequence seq0 = odeSolver.rungeKuttaSequence(4.0) ;
		ArraySequence seq0 = odeSolver.fehlbergSequence(4.0) ;

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
		DerivnFunction func = (x, y) -> new double[]{x, 1.0} ; // y'[0] = x, y'[1] = 1.0
		double x0 = 0.0 ;
		double[] y0 = {1.0, 1.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0) ;

		double[] x = MathUtils.linspace(0.0, 15.0, 1000) ;
		double[] xOde = MathUtils.linspace(0.0, 15.0, 20) ;
//		double[][] yvalues = odeSolver.rungeKutta(xOde) ;
		double[][] yvalues = odeSolver.fehlberg(xOde) ;
		double[] y1exact = ArrayFunc.apply(t -> 0.5*t*t+1.0, x) ;
//		double[] y2exact = ArrayFunc.apply(t -> t+1.0, x) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y1exact, "b");
		fig.plot(xOde, yvalues[0], "r");
		fig.renderPlot();
		fig.markerON(1);
		fig.setFigLineWidth(0, 3f);
		fig.setFigLineWidth(1, 0f);
		fig.xlabel("x values");
		fig.ylabel("y1(x)");
		fig.font(20);
		fig.show(true);
	}




}

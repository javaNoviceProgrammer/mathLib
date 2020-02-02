package tests.test_ode;

import java.util.ArrayList;
import java.util.List;

import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.optimize.pso.FitnessFunction;
import mathLib.optimize.pso.ParticleSwarmOptimization;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import static mathLib.optimize.pso.ParticleSwarmOptimization.interval ;

public class TestSteadyStateSolution {

	public static void main(String[] args) {
//		test1() ;
//		test2() ;
//		test3() ;
		test4() ;
	}

	public static void test4() {
		// solve y1' = 2-y1-y2, y2' = 3*y1^2-5
		// z = (y1, y2) : state vector
		DerivnFunction func = (x, z) -> new double[] {2.0-z[0]-z[1], 3.0*z[0]*z[0]-5.0} ;
		double x0 = 0.0 ;
		double[] initialConditions = {-4.0, 0.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, initialConditions) ;
		double[] xvals = MathUtils.linspace(x0, x0+50.0, 1000) ;
		double[][] z = odeSolver.rungeKutta(xvals) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, z[0], "b");
		fig.plot(xvals, z[1], "r");
		fig.renderPlot();
		fig.font(17);
		fig.xlabel("x");
		fig.ylabel("y1(x), y2(x)");
		fig.show(true);
	}

	public static void test3() {
		List<Double> y1SteadyState = new ArrayList<>() ;
		List<Double> y2SteadyState = new ArrayList<>() ;
		for(int i=0; i<50; i++) {
			// y1 = t[0], y2 = t[1]
			FitnessFunction func = t -> Math.abs(2.0-t[0]-t[1]) + Math.abs(3.0*t[0]*t[0]-5.0) ;
			ParticleSwarmOptimization pso = new ParticleSwarmOptimization(100, func, interval(-100.0, 100.0), interval(-100.0, 100.0)) ;
			pso.setMinimize(true);
			pso.setBoundedOptimization(false); // important
			pso.solve(100);
			y1SteadyState.add(pso.bestPosition().at(0)) ;
			y2SteadyState.add(pso.bestPosition().at(1)) ;
		}
		MatlabChart fig = new MatlabChart() ;
		fig.plot(y1SteadyState.toArray(), y2SteadyState.toArray());
		fig.renderPlot();
		fig.markerON();
		fig.setFigLineWidth(0, 0f);
		fig.font(20);
		fig.xlabel("Y1 steady-state");
		fig.ylabel("Y2 steady-state");
		fig.show(true);
	}

	public static void test2() {
		// y1 = t[0], y2 = t[1]
		FitnessFunction func = t -> Math.abs(2.0-t[0]-t[1]) + Math.abs(3.0*t[0]-5.0) ;
		ParticleSwarmOptimization pso = new ParticleSwarmOptimization(100, func, interval(-100.0, 100.0), interval(-100.0, 100.0)) ;
		pso.setMinimize(true);
		pso.setBoundedOptimization(false); // important
		pso.solveAndPlot(100);
		System.out.println(pso.bestPosition());
	}

	public static void test1() {
		// solve y1' = 2-y1-y2, y2' = 3*y1-5
		// z = (y1, y2)
		DerivnFunction func = (x, z) -> new double[] {2.0-z[0]-z[1], 3.0*z[0]-5.0} ;
		double x0 = 0.0 ;
		double[] initialConditions = {1.0, 0.0} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, initialConditions) ;
		double[] xvals = MathUtils.linspace(x0, x0+50.0, 1000) ;
		double[][] z = odeSolver.rungeKutta(xvals) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, z[0], "b");
		fig.plot(xvals, z[1], "r");
		fig.renderPlot();
		fig.font(17);
		fig.xlabel("x");
		fig.ylabel("y1(x), y2(x)");
		fig.show(true);
	}

}

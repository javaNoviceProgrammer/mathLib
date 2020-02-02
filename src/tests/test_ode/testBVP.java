package tests.test_ode;

import java.util.List;

import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.plot.MatlabChart;
import mathLib.root.RealRoot;
import mathLib.root.RealRootFunction;
import mathLib.util.MathUtils;
import mathLib.util.Timer;


public class testBVP {

	public static void main(String[] args) {
//		test1() ;
		test2() ;
	}

	public static void test2() {
		double a = 1.0 ;
		double x0 = -0.5*a, x1 = 0.5*a ;
		double y0 = 0.0, y0prime = 1.0 ;
		double[] initialConditions = {y0, y0prime} ;
		RealRootFunction funcE = E -> {
			// z = (y, y') --> state vector
			// y'' = -E y --> E = (n*pi/a)^2
			DerivnFunction func = (x,z) -> new double[] {z[1], -E*z[0]} ;
			OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, initialConditions) ;
//			double[] z = odeSolver.rungeKutta(x1) ; // --> O(h^4)
			double[] z = odeSolver.fehlberg(x1) ; // --> O(h^5)
			// y(x1) = z[0] --> equation: y(x1)==0
			return z[0] ;
		} ;

		RealRoot rootFinder = new RealRoot(funcE) ;
		List<Double> E = rootFinder.trisection(0.0, 500.0, 1000) ;
		for(double e : E)
			System.out.println(e/(Math.PI*Math.PI));

		int index = 5 ;
		DerivnFunction func = (x,z) -> new double[] {z[1], -E.get(index)*z[0]} ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, y0, y0prime) ;
		double[] x = MathUtils.linspace(x0, x1, 1000) ;
		double[][] z = odeSolver.rungeKutta(x) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, z[0], "b");
		fig.renderPlot();
		fig.xlabel("x");
		fig.ylabel("y(x) [mode = " + index + "]");
		fig.show(true);
	}

	public static void test1() {
		// y1' = y2, y2' = -y1+2
		// z = (y1, y2) --> state vector
		DerivnFunction func = (x,z) -> new double[] {z[1], -z[0]+2.0} ;
		double a = 5.0 ;
		double x0 = 0.0 ;
		double y10 = 0.0 ;
		OdeSystemSolver odeSolver = new OdeSystemSolver(func, x0, new double[2]) ;
		Timer timer = new Timer() ;
		timer.start();
		RealRootFunction rootFunc = y20 -> {
			double[] initialConditions = {y10, y20} ;
			odeSolver.setY0(initialConditions);
			double[] z = odeSolver.rungeKutta(a) ;
			// y1(a) = z[0] --> equation: y1(a)==0
			return z[0] ;
		} ;
		RealRoot rootFinder = new RealRoot(rootFunc) ;
		double y20 = rootFinder.newton(0.0, 100) ;

		System.out.println(y20);
		double[] x = MathUtils.linspace(x0, x0+50.0, 1000) ;
		odeSolver.setY0(new double[] {y10, y20});
		double[][] z = odeSolver.rungeKutta(x) ;
		timer.stop();
		timer.show();

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, z[0], "b");
		fig.plot(x, z[1], "r");
		fig.renderPlot();
		fig.xlabel("x");
		fig.ylabel("y1(x),y2(x)");
		fig.show(true);
	}

}

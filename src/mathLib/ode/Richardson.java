package mathLib.ode;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

import static edu.uta.futureye.function.FMath.*;

public class Richardson {

	public static double deriv(RealFunction func, double x, double dx, int n) {
		return extrapolate(func, x, n, dx) ;
	}

	/**
	 * default dx = 0.1
	 * @param func
	 * @param x
	 * @param n
	 * @return
	 */

	public static double deriv(RealFunction func, double x, int n) {
		return extrapolate(func, x, n, 1e-1) ;
	}

	/**
	 * default dx = 0.1 ,
	 * default iteration order = 5
	 * @param func
	 * @param x
	 * @return
	 */

	public static double deriv(RealFunction func, double x) {
		return extrapolate(func, x, 5, 1e-1) ;
	}

	private static double extrapolate(RealFunction func, double x, int n, double h) {
		if(n<0)
			throw new IllegalArgumentException("n must be greater than or equal to 0") ;
		if(n==0)
			return (func.evaluate(x+h)-func.evaluate(x-h))/(2.0*h) ; // order h^2
		return (Math.pow(2, n+1) *extrapolate(func, x, n-1, h/2.0) - extrapolate(func, x, n-1, h))/(Math.pow(2, n+1)-1) ;
	}

	// for test
	public static void main(String[] args) {
		RealFunction func = t -> Math.cos(t*t)/t ;
		MathFunc f = cos(x*x)/x ;
		System.out.println("Exact deriv=      " + f.diff("x").apply(2.0));
		System.out.println("zeroth iteration: " +  deriv(func, 2, 1e-1, 0));
		System.out.println("first iteration:  "  + deriv(func, 2, 1e-1, 1));
		System.out.println("second iteration: " +  deriv(func, 2, 1e-1, 2));
		System.out.println("third iteration:  "  + deriv(func, 2, 1e-1, 3));
		System.out.println("forth iteration:  "  + deriv(func, 2, 1e-1, 4));
		System.out.println("fifth iteration:  "  + deriv(func, 2, 1e-1, 5));

		RealFunction dfunc = t -> deriv(func, t) ;
		// plotting first deriv
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(0.5, Math.PI*4, 1000) ;
		double[] y1 = ArrayFunc.apply(t -> f.diff("x").apply(t), x) ;
		double[] y2 = ArrayFunc.apply(dfunc, x) ;
		fig.plot(x, y1, "b");
		fig.plot(x, y2, "r");
		fig.renderPlot();
		fig.run(true);
		fig.markerON();
		fig.title("First Derivative");

		// plotting second deriv
		RealFunction d2func = t -> deriv(dfunc, t) ;
		MatlabChart fig1 = new MatlabChart() ;
		double[] y3 = ArrayFunc.apply(t -> f.diff("x").diff("x").apply(t), x) ;
		double[] y4 = ArrayFunc.apply(d2func, x) ;
		fig1.plot(x, y3, "b");
		fig1.plot(x, y4, "r");
		fig1.renderPlot();
		fig1.run(true);
		fig1.markerON();
		fig1.title("Second Derivative");
	}

}

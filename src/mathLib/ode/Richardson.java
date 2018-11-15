package mathLib.ode;

import static mathLib.func.symbolic.FMath.*;

import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class Richardson {

	// df/dx

	public static double deriv(RealFunction func, double x, double dx, int n) {
		return extrapolateDeriv(func, x, n, dx) ;
	}

	/**
	 * default dx = 0.1
	 * @param func
	 * @param x
	 * @param n
	 * @return
	 */

	public static double deriv(RealFunction func, double x, int n) {
		return extrapolateDeriv(func, x, n, 1e-1) ;
	}

	/**
	 * default dx = 0.1 ,
	 * default iteration order = 5
	 * @param func
	 * @param x
	 * @return
	 */

	public static double deriv(RealFunction func, double x) {
		return extrapolateDeriv(func, x, 5, 1e-1) ;
	}

	// d^2 f/dx^2

	public static double deriv2(RealFunction func, double x, double dx, int n) {
		RealFunction deriv = t -> deriv(func, t, dx, n) ;
		return deriv(deriv, x, dx, n) ;
	}

	public static double deriv2(RealFunction func, double x, int n) {
		RealFunction deriv = t -> deriv(func, t, n) ;
		return deriv(deriv, x, n) ;
	}

	public static double deriv2(RealFunction func, double x) {
		RealFunction deriv = t -> deriv(func, t) ;
		return deriv(deriv, x) ;
	}

	// d^3 f/dx^3

	public static double deriv3(RealFunction func, double x, double dx, int n) {
		RealFunction deriv2 = t -> deriv2(func, t, dx, n) ;
		return deriv(deriv2, x, dx, n) ;
	}

	public static double deriv3(RealFunction func, double x, int n) {
		RealFunction deriv2 = t -> deriv2(func, t, n) ;
		return deriv(deriv2, x, n) ;
	}

	public static double deriv3(RealFunction func, double x) {
		RealFunction deriv2 = t -> deriv2(func, t, 2) ;
		return deriv(deriv2, x) ;
	}

	private static double extrapolateDeriv(RealFunction func, double x, int n, double h) {
		if(n<0)
			throw new IllegalArgumentException("n must be greater than or equal to 0") ;
		if(n==0) {
			try {
				return (func.evaluate(x+h)-func.evaluate(x-h))/(2.0*h) ; // order h^2, symmetric
			} catch (Exception e) {
				try {
					return (func.evaluate(x+h)-func.evaluate(x))/(2.0*h) ; // order h, forward
				} catch (Exception e2) {
					return (func.evaluate(x)-func.evaluate(x-h))/(2.0*h) ; // order h, backward
				}
			}
		}
			
		return (Math.pow(2, n+1) *extrapolateDeriv(func, x, n-1, h/2.0) - extrapolateDeriv(func, x, n-1, h))/(Math.pow(2, n+1)-1) ;
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

		// plotting first deriv
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(0.5, Math.PI*4, 1000) ;
		double[] y1 = ArrayFunc.apply(t -> f.diff("x").apply(t), x) ;
		double[] y2 = ArrayFunc.apply(t -> deriv(func, t, 2), x) ;
		fig.plot(x, y1, "b");
		fig.plot(x, y2, "r");
		fig.renderPlot();
		fig.run(true);
		fig.markerON();
		fig.title("First Derivative");

		// plotting second deriv
		MatlabChart fig1 = new MatlabChart() ;
		double[] y3 = ArrayFunc.apply(t -> f.diff("x").diff("x").apply(t), x) ;
		double[] y4 = ArrayFunc.apply(t -> deriv2(func, t, 2), x) ;
		fig1.plot(x, y3, "b");
		fig1.plot(x, y4, "r");
		fig1.renderPlot();
		fig1.run(true);
		fig1.markerON();
		fig1.title("Second Derivative");

		// plotting third deriv
		MatlabChart fig2 = new MatlabChart() ;
		double[] y5 = ArrayFunc.apply(t -> f.diff("x").diff("x").diff("x").apply(t), x) ;
		double[] y6 = ArrayFunc.apply(t -> deriv3(func, t, 2), x) ;
		fig2.plot(x, y5, "b");
		fig2.plot(x, y6, "r");
		fig2.renderPlot();
		fig2.run(true);
		fig2.markerON();
		fig2.title("Third Derivative");

		System.out.println("Exact: " + f.diff("x").diff("x").diff("x").apply(0.5));
		System.out.println("Richardson: " + deriv3(func, 0.5, 2));
	}

}

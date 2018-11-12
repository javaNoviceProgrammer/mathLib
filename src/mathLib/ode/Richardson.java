package mathLib.ode;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.func.intf.RealFunction;
import static edu.uta.futureye.function.FMath.*;

public class Richardson {

	public static double deriv(RealFunction func, double x, double dx, int n) {
		return extrapolate(func, x, n, dx) ;
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
		RealFunction func = t -> (t*Math.sin(t*t)/Math.cos(t)) ;
		MathFunc f = x*sin(x*x)/cos(x) ;
		System.out.println("Exact deriv=      " + f.diff("x").apply(2.0));
		System.out.println("zeroth iteration: " +  deriv(func, 2, 1e-1, 0));
		System.out.println("first iteration:  "  + deriv(func, 2, 1e-1, 1));
		System.out.println("second iteration: " +  deriv(func, 2, 1e-1, 2));
		System.out.println("third iteration:  "  + deriv(func, 2, 1e-1, 3));
		System.out.println("forth iteration:  "  + deriv(func, 2, 1e-1, 4));
		System.out.println("fifth iteration:  "  + deriv(func, 2, 1e-1, 5));
	}

}

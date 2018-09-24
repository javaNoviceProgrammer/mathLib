package mathLib.ode;

import mathLib.func.intf.RealFunction;

public class Richardson {

	public static double deriv(RealFunction func, double x, int n) {
		double eps = 1e-3 ;
		double h = eps * x ;

		return extrapolate(func, x, n, h) ;
	}

	private static double extrapolate(RealFunction func, double x, int n, double h) {
		if(n<0)
			throw new IllegalArgumentException("n must be greater than or equal to 0") ;
		if(n==0)
			return (func.evaluate(x+h)-func.evaluate(x))/h ;
		return (Math.pow(2, n) *extrapolate(func, x, n-1, h/Math.pow(2, n)) - extrapolate(func, x, n-1, h))/(Math.pow(2, n)-1) ;
	}

	// for test
	public static void main(String[] args) {
		RealFunction func = t -> t*t ;
		System.out.println(deriv(func, 1, 0));
		System.out.println(deriv(func, 1, 1));
		System.out.println(deriv(func, 1, 2));
		System.out.println(deriv(func, 1, 3));
	}

}

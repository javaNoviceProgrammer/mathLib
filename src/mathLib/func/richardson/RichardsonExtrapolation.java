package mathLib.func.richardson;

import mathLib.root.RealRoot;
import mathLib.root.RealRootFunction;

public class RichardsonExtrapolation {

	RichardsonFunction func ;
	double initialOrder ;

	public RichardsonExtrapolation(RichardsonFunction func, double initialOrder) {
		this.func = func ;
		this.initialOrder = initialOrder ;
	}

	public RichardsonExtrapolation(RichardsonFunction func) {
		this.func = func ;
		this.initialOrder = 0.0 ;
	}

	public void setRichardsonFunction(RichardsonFunction func) {
		this.func = func ;
	}

	public void setInitialOrder(int initialOrder) {
		this.initialOrder = initialOrder ;
	}

	// order is the order of sequence (recursion) --> 0, 1, 2, 3, ...
	private RichardsonFunction next(RichardsonFunction func, int order) {
		double m = initialOrder + order ;
		double coeff = Math.pow(2.0, m) ;
		return (var1, var2) -> (coeff*func.value(var1, 0.5*var2)-func.value(var1, var2))/(coeff-1) ;
	}

	// iterate n times
	public double iterate(double t, double h, int n) {
		// check if the error order is given
		if(Math.abs(initialOrder)<1e-10)
			initialOrder = estimateInitialOrder(t, h) ;
		// compute richardson iterations
		RichardsonFunction funcCopy = func ;
		for(int i=0; i<n; i++) {
			funcCopy = next(funcCopy, i) ;
		}
		return funcCopy.value(t, h) ;
	}

	public double estimateInitialOrder(double t, double h) {
		// use first iteration h/2, h/3
		double term = func.value(t, h) ;
		double termHalfH = func.value(t, h/2.0) ;
		double termOneThirdH = func.value(t, h/3.0) ;
		// solve for m
		double arg1 = termHalfH - termOneThirdH ;
		double arg2 = termHalfH - term ;
		double arg3 = termOneThirdH - term ;
		RealRootFunction rootFunc = m -> arg1 + arg2/(Math.pow(2.0, m)-1) - arg3/(Math.pow(3.0, m)-1) ;

		RealRoot rootFinder = new RealRoot(rootFunc) ;
		double m = rootFinder.trisection(0.1, 100) ;
		return m ;
	}

}

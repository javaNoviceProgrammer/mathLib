package mathLib.polynom.special;

import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;
import static mathLib.polynom.Polynomial.x;

public class LegendrePolynom {

	private LegendrePolynom() {

	}

	public static Polynomial fromBonnet(int degree) {
		// stop condition
		if(degree == 0)
			return 1.0 + 0.0*x ;
		if(degree == 1)
			return x ;
		// return
		int n = degree - 1 ;
		Polynomial result = ((2.0*n+1.0)*x*fromBonnet(n) - n*fromBonnet(n-1))/(n+1) ;
		return result.reduce() ;
	}

	public static Polynomial fromRodrigues(int degree) {
		// step 1: (x^2-1)^n
		Polynomial base = (x*x-1).pow(degree) ;
		// step 2: d^n/dx^n (x^2-1)^n
		Polynomial diffBase = base.diff(degree) ;
		// step 3: 2^n * n!
		double coeff = Math.pow(2.0, degree) * MathUtils.factorial(degree) ;
		// return the result
		return (diffBase/coeff).reduce() ;
	}

}

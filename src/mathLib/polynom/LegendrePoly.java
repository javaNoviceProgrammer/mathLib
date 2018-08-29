package mathLib.polynom;

import static mathLib.polynom.Polynomial.X;
import static mathLib.util.MathUtils.Functions.* ;

public class LegendrePoly {

	public static Polynomial getPoly(int degree) {
		if(degree == 0)
			return 0*X + 1 ;

		Polynomial poly = (X*X-1).pow(degree) ;
		double coeff = 1.0/(Math.pow(2, degree) * factorial(degree)) ;
		return (coeff*poly.diff(degree)) ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(getPoly(0));
		System.out.println(getPoly(1));
		System.out.println(getPoly(2));
		System.out.println(getPoly(3));
		System.out.println(getPoly(4));
		System.out.println(getPoly(5));
		System.out.println(getPoly(6));
		System.out.println(getPoly(7));
		System.out.println(getPoly(8));
		System.out.println(getPoly(9));
		System.out.println(getPoly(10));
	}




}

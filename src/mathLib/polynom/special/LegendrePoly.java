package mathLib.polynom.special;

import static mathLib.polynom.ComplexPolynomial.Xc;
import static mathLib.polynom.Polynomial.X;
import static mathLib.util.MathUtils.factorial;

import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;

public class LegendrePoly {

	public static Polynomial legendre(int degree) {
		if(degree == 0)
			return 0*X + 1 ;

		Polynomial poly = (X*X-1).pow(degree) ;
		double coeff = 1.0/(Math.pow(2, degree) * factorial(degree)) ;
		return (coeff*poly.diff(degree)) ;
	}

	public static Polynomial associatedLegendre(int l, int m) {
		int c1 = 1 ;
		if(m % 2 != 0)
			throw new IllegalArgumentException("m must be even!") ;
		Polynomial poly = (1-X*X).pow(m/2) ;
		return (c1 * poly * legendre(l).diff(m)) ;

	}
	
	public static ComplexPolynomial legendreC(int degree) {
		return legendre(degree)+0*Xc ;
	}

	public static ComplexPolynomial associatedLegendreC(int l, int m) {
		return associatedLegendre(l, m) + 0*Xc ;
	}
	
	// for test
	public static void main(String[] args) {
		System.out.println(legendre(0));
		System.out.println(legendre(1));
		System.out.println(legendre(2));
		System.out.println(legendre(3));
		System.out.println(legendre(4));
		System.out.println(legendre(5));
		System.out.println(legendre(6));
		System.out.println(legendre(7));
		System.out.println(legendre(8));
		System.out.println(legendre(9));
		System.out.println(legendre(10));

		System.out.println(associatedLegendre(2, 0));
		
		System.out.println(MathUtils.Arrays.toString(legendre(41).getRoots()));
	}




}

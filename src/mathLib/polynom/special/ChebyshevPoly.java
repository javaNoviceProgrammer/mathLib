package mathLib.polynom.special;

import static mathLib.polynom.ComplexPolynomial.Xc;
import static mathLib.polynom.Polynomial.X;

import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class ChebyshevPoly {

	public static Polynomial firstKind(int n) {
		if(n==0)
			return 0*X + 1 ;
		else if(n==1)
			return X ;
		else if(n >= 2)
			return 2*X*firstKind(n-1)-firstKind(n-2) ;
		else
			throw new IllegalArgumentException("degree must be 0 or higher") ;
	}

	public static Polynomial secondKind(int n) {
		if(n==0)
			return 0*X + 1 ;
		else if(n==1)
			return 2*X ;
		else if(n >= 2)
			return 2*X*secondKind(n-1)-secondKind(n-2) ;
		else
			throw new IllegalArgumentException("degree must be 0 or higher") ;
	}

	/**
	 * Complex version of the Chebyshev polynomial
	 * @param n
	 * @return
	 */

	public static ComplexPolynomial firstKindC(int n) {
		return firstKind(n)+0*Xc ;
	}

	public static ComplexPolynomial secondKindC(int n) {
		return secondKind(n)+0*Xc ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(firstKind(0));
		System.out.println(firstKind(1));
		System.out.println(firstKind(2));
		System.out.println(firstKind(3));
		System.out.println(firstKind(4));
		System.out.println(firstKind(5));
		System.out.println(firstKind(6));
		System.out.println(firstKind(7));
		System.out.println(firstKind(8));
		System.out.println(firstKind(9));
		System.out.println(firstKind(10));
		System.out.println(firstKind(11));

//		System.out.println("\n \n");
//
//		System.out.println(secondKind(0));
//		System.out.println(secondKind(1));
//		System.out.println(secondKind(2));
//		System.out.println(secondKind(3));
//		System.out.println(secondKind(4));
//
//		System.out.println("\n"+(firstKind(1)*firstKind(2)).integrate());

	}


}

package mathLib.polynom.special;

import static mathLib.polynom.Polynomial.* ;
import static mathLib.util.MathUtils.* ;
import static mathLib.polynom.ComplexPolynomial.*;
import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class BesselPoly {

	public static Polynomial bessel(int n) {
		if(n==0)
			return 0*X + 1 ;
		else if(n==1)
			return X + 1 ;
		else if(n >= 2) {
			Polynomial p = ZERO ;
			for(int k=0; k<=n; k++) {
				double coeff = factorial(n+k)/(factorial(n-k)*factorial(k)) ;
				p = p + coeff * (X/2.0).pow(k) ;
			}
//			return (2*n-1)*X*bessel(n-1) + bessel(n-2) ;	
			return p ;
		}
		else 
			throw new IllegalArgumentException("degree must be 0 or higher") ;
	}
	
	public static Polynomial reverse(int n) {
		if(n==0)
			return 0*X + 1 ;
		else if(n==1)
			return X + 1 ;
		else if(n >= 2) {
			Polynomial p = ZERO ;
			for(int k=0; k<=n; k++) {
				double coeff = factorial(n+k)/(factorial(n-k)*factorial(k)) ;
				p = p + coeff * X.pow(n-k)/Math.pow(2.0, k) ;
			}
//			return (2*n-1)*reverse(n-1) + X*X*reverse(n-2) ;
			return p ;
		}
		else 
			throw new IllegalArgumentException("degree must be 0 or higher") ;
	}
	
	/**
	 * Complex version of the bessel polynomial
	 * @param n
	 * @return
	 */
	
	public static ComplexPolynomial besselC(int n) {
		return bessel(n)+0*Xc ;
	}
	
	/**
	 * Complex version of the reverse bessel polynomial
	 * @param n
	 * @return
	 */
	
	public static ComplexPolynomial reverseC(int n) {
		return reverse(n)+0*Xc ;
	}

	
	// for test
	public static void main(String[] args) {
		System.out.println(bessel(0));
		System.out.println(bessel(1));
		System.out.println(bessel(2));
		System.out.println(bessel(3));
		System.out.println(bessel(4));
		System.out.println(bessel(5));
		System.out.println(besselC(5));
		
		System.out.println("\n \n");
		
		System.out.println(reverse(0));
		System.out.println(reverse(1));
		System.out.println(reverse(2));
		System.out.println(reverse(3));
		System.out.println(reverse(4));
		System.out.println(reverse(5));
		System.out.println(reverseC(5));
		
	}
	
	
}

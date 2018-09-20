package mathLib.polynom.special;

import static mathLib.polynom.Polynomial.* ;
import static mathLib.util.MathUtils.* ;
import static mathLib.polynom.ComplexPolynomial.*;
import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class FibonacciPoly {

	public static Polynomial fibonacci(int n) {
		if(n==1)
			return 0*X + 1 ;
		if(n==2)
			return X ;
		if(n == 3) 
			return X*X + 1 ;
		if(n > 3) {
			int m = (int) Math.floor((n-1)/2.0) ;
			Polynomial p = 0*X ;
			for(int j=0; j <= m ; j++)
				p = p + combination(n-j-1, j) * X.pow(n-2*j-1) ;
			return p ;
		}	
		else 
			throw new IllegalArgumentException("order must be 1 or higher") ;
	}
	
	public static Polynomial recursive(int n) {
		if(n < 1)
			throw new IllegalArgumentException("order must be 1 or higher") ;
		if(n==1)
			return 0*X + 1 ;
		if(n==2)
			return X ;
		if(n == 3) 
			return X*X + 1 ;
		
		return X*recursive(n-1)+recursive(n-2) ;
	}
	
	/**
	 * Complex version of the fibonacci polynomial
	 * @param n
	 * @return
	 */
	
	public static ComplexPolynomial fibonacciC(int n) {
		return fibonacci(n)+0*Xc ;
	}

	
	// for test
	public static void main(String[] args) {
		System.out.println(fibonacci(1));
		System.out.println(fibonacci(2));
		System.out.println(fibonacci(3));
		System.out.println(fibonacci(4));
		System.out.println(fibonacci(5));
		System.out.println(fibonacci(6));
	}
	
	
}

package mathLib.polynom.special;

import static mathLib.polynom.ComplexPolynomial.Xc;
import static mathLib.polynom.Polynomial.X;

import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class FactorialPoly {

	public static Polynomial factorial(int degree) {
		if (degree == 0)
			return 0 * X + 1;
		if (degree == 1)
			return X;

		Polynomial result = 0 * X + 1;
		for (int i = 0; i < degree; i++)
			result = result * (X - i);

		return result;
	}
	
	/**
	 * Complex version of the factorial polynomial
	 * @param degree
	 * @return
	 */

	public static ComplexPolynomial factorialC(int degree) {
		return factorial(degree) + 0 * Xc;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(factorial(0));
		System.out.println(factorial(1));
		System.out.println(factorial(2));
		System.out.println(factorial(3));
		System.out.println(factorial(4));
		System.out.println(factorial(5));
		System.out.println(factorial(6));
		System.out.println(factorial(7));
		System.out.println(factorial(8));
	}

}

package mathLib.polynom.special;

import static mathLib.polynom.Polynomial.X;

import java.math.BigInteger;

import mathLib.polynom.Polynomial;

public class EulerPoly {

	public static Polynomial euler(int degree) {
		if (degree == 0)
			return 0*X + 1 ;
		else if (degree == 1)
			return X-0.5 ;
		Polynomial p = X.pow(degree) ;
		for(int s=0; s<degree; s++) {
			double coeff = 0.5 * factorial(degree)/(factorial(s)*factorial(degree-s)) ;
			p = p - coeff * euler(s) ;
		}
		return p ;
	}

	public static double eulerNumber(int degree) {
		if (degree % 2 != 0)
			return 0 ;
		if (degree == 0)
			return 1 ;
		double sum = 0 ;
		for (int k=0; k<degree; k++){
			double coeff = -0.5 * factorial(degree)/(factorial(k)*factorial(degree-k)) ;
			int s = (degree-k)%2 == 0 ? 1 : -1 ;
			sum += coeff*(s+1.0)*eulerNumber(k) ;
		}
		return sum ;
	}


	private static double factorial(int var) {
		if(var == 0)
			return 1.0 ;
		else
			return var*factorial(var-1) ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(euler(0));
		System.out.println(euler(1));
		System.out.println(euler(2));
		System.out.println(euler(3));
		System.out.println(euler(4));
		System.out.println(euler(5));
		System.out.println(euler(6));
		System.out.println(euler(7));
		System.out.println(euler(8));
		System.out.println(euler(9));
		System.out.println(euler(18));

		System.out.println();

		System.out.println(eulerNumber(0));
		System.out.println(eulerNumber(2));
		System.out.println(eulerNumber(4));
		System.out.println(eulerNumber(6));
		System.out.println(eulerNumber(8));
		System.out.println(eulerNumber(10));
		System.out.println(eulerNumber(12));
		System.out.println(eulerNumber(14));
		System.out.println(eulerNumber(16));
		System.out.println(eulerNumber(18));

		System.out.println();
		BigInteger c = new BigInteger("-2404879675441") ;
		System.out.println(c);

	}

}

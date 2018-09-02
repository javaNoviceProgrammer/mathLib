package mathLib.polynom.special;

import mathLib.polynom.Polynomial;
import static mathLib.polynom.Polynomial.* ;

public class HermitePoly {

	public static Polynomial hermite (int degree) {
		if(degree < 0)
			throw new IllegalArgumentException("degree must be greater than or equal to 0") ;
		if(degree == 0)
			return 0*X+1 ;
		if(degree == 1)
			return 2*X ;
		if(degree == 2)
			return 4*X*X-2 ;

		return 2*X*hermite(degree-1) - hermite(degree-1).diff() ;
	}


	// for test
	public static void main(String[] args) {
		System.out.println(hermite(0));
		System.out.println(hermite(1));
		System.out.println(hermite(2));
		System.out.println(hermite(3));
		System.out.println(hermite(4));
		System.out.println(hermite(5));
		System.out.println(hermite(6));
		System.out.println(hermite(7));
		System.out.println(hermite(8));
		System.out.println(hermite(9));
		System.out.println(hermite(10));
	}

}

package mathLib.polynom.special;

import mathLib.polynom.Polynomial;
import static mathLib.polynom.Polynomial.*;

public class FactorialPoly {

	public static Polynomial factorial(int degree) {
		if(degree==0)
			return 0*X+1 ;
		if(degree==1)
			return X ;

		Polynomial result = 0*X+1 ;
		for(int i=0; i<degree; i++)
			result = result*(X-i) ;

		return result ;
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

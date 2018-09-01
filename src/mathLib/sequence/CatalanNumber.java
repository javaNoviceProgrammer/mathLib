package mathLib.sequence;

import static mathLib.util.MathUtils.*;

public class CatalanNumber {

	public static double catalan(int n) {
		if(n<0)
			throw new IllegalArgumentException("argument must be greater than or equal to 0") ;
		if(n==0)
			return 1 ;
		if(n==1)
			return 1 ;
		return 1.0/(n+1) * combination(2*n, n) ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(catalan(0));
		System.out.println(catalan(1));
		System.out.println(catalan(2));
		System.out.println(catalan(3));
		System.out.println(catalan(4));
		System.out.println(catalan(5));
		System.out.println(catalan(6));
		System.out.println(catalan(7));
		System.out.println(catalan(8));
		System.out.println(catalan(9));
		System.out.println(catalan(10));
		System.out.println(catalan(11));
		System.out.println(catalan(12));
		System.out.println(catalan(13));
		System.out.println(catalan(14));
		System.out.println(catalan(15));
		System.out.println(catalan(16));
		System.out.println(catalan(17));
		System.out.println(catalan(18));
		System.out.println(catalan(19));
		System.out.println(catalan(20));
		System.out.println(catalan(21));
		System.out.println(catalan(22));
	}

}

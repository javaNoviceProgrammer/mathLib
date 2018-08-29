package mathLib.polynom;

import static mathLib.polynom.Polynomial.X;
import static mathLib.polynom.Polynomial.ZERO;
import static mathLib.util.MathUtils.Functions.factorial;

import edu.uta.futureye.function.FMath;
import edu.uta.futureye.function.intf.MathFunc;

public class EulerPoly {

	public static Polynomial euler(int degree) {
		if (degree == 0)
			return 0*X + 1 ;
		else if (degree == 1)
			return X-0.5 ;
		Polynomial p = X.pow(degree) ;
		for(int s=0; s<degree; s++) {
			double coeff = 0.5 * factorial(degree)/(1.0*factorial(s)*factorial(degree-s)) ;
			p = p - coeff * euler(s) ;
		}
		return p ;
	}

	public static double eulerNumber(int degree) {
		if (degree % 2 != 0)
			return 0.0 ;
		if (degree == 0)
			return 1.0 ;
		double sum = 0 ;
		for (int k=0; k<degree; k++){
			double coeff = -0.5 * factorial(degree)/(1.0*factorial(k)*factorial(degree-k)) ;
			int s = (degree-k)%2 == 0 ? 1 : -1 ;
			sum += coeff*(s+1.0)*eulerNumber(k) ;
		}
		return sum ;
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

		System.out.println("\n \n ");

		System.out.println(euler(3));
		System.out.println(euler(3).compose(1-X));
		System.out.println((euler(3) + euler(3).compose(1-X)).equals(ZERO));

		System.out.println("\n \n ");

		System.out.println(eulerNumber(0));
		System.out.println(eulerNumber(2));
		System.out.println(eulerNumber(4));
		System.out.println(eulerNumber(6));
		System.out.println(eulerNumber(8));
		System.out.println(eulerNumber(10));
		System.out.println(eulerNumber(12));
		System.out.println(eulerNumber(14));
		System.out.println(eulerNumber(18));

		System.out.println(euler(6).evaluate(0.5)*Math.pow(2, 6));

		MathFunc f = FMath.asin(FMath.x) ;
		System.out.println(f);
		System.out.println(f.diff("x"));
		System.out.println(f.diff("x").apply(0.0));

	}

}

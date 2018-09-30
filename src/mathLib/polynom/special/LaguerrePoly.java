package mathLib.polynom.special;

import static mathLib.polynom.ComplexPolynomial.Xc;
import static mathLib.polynom.Polynomial.X;
import static mathLib.polynom.Polynomial.ZERO;
import static mathLib.util.MathUtils.combination;
import static mathLib.util.MathUtils.factorial;
import static mathLib.util.MathUtils.minusOnePower;

import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class LaguerrePoly {

	public static Polynomial lagurre(int degree) {
		if (degree < 0)
			throw new IllegalArgumentException("degree must be greater than or equal to 0");
		if (degree == 0)
			return 0 * X + 1;
		if (degree == 1)
			return -X + 1;
		if (degree == 2)
			return 0.5 * X * X - 2 * X + 1;

		Polynomial result = ZERO;
		for (int k = 0; k <= degree; k++) {
			double coeff = minusOnePower(k) * combination(degree, k) / factorial(k);
			result = result + coeff * X.pow(k);
		}
		return result;
	}

	public static Polynomial generalizedLagurre(int degree, double alpha) {
		if (degree < 0)
			throw new IllegalArgumentException("degree must be greater than or equal to 0");
		if (degree == 0)
			return 0 * X + 1;
		if (degree == 1)
			return 1 + alpha - X;

		Polynomial result = (2 * degree - 1 + alpha - X) * generalizedLagurre(degree - 1, alpha)
				- (degree - 1 + alpha) * generalizedLagurre(degree - 2, alpha);
		return 1.0 / degree * result;
	}
	
	/**
	 * Complex version of the polynomial
	 * @param degree
	 * @return
	 */
	
	public static ComplexPolynomial lagurreC(int degree) {
		return lagurre(degree)+0*Xc ;
	}
	
	public static ComplexPolynomial generalizedLagurreC(int degree, double alpha) {
		return generalizedLagurre(degree, alpha)+0*Xc ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(lagurre(0));
		System.out.println(lagurre(1));
		System.out.println(lagurre(2));
		System.out.println(lagurre(3));
		System.out.println(lagurre(4));
		System.out.println(lagurre(5));
		System.out.println(lagurre(6));

		System.out.println();

		System.out.println(generalizedLagurre(0, 1.2));
		System.out.println(generalizedLagurre(1, 1.2));
		System.out.println(generalizedLagurre(2, 1.2));
		System.out.println(generalizedLagurre(3, 1.2));
		System.out.println(generalizedLagurre(4, 1.2));
		System.out.println(generalizedLagurre(5, 1.2));
		System.out.println(generalizedLagurre(6, 1.2));

	}

}

package mathLib.polynom;

import mathLib.utils.MathUtils;
import static mathLib.polynom.Polynomial.*;

public class TaylorExpansion {

	public static Polynomial getExp(int degree) {
		double[] coeffs = new double[degree + 1];
		for (int i = 0; i < coeffs.length; i++) {
			coeffs[i] = 1.0 / MathUtils.Functions.factorial(i);
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getSin(int degree) {
		double[] coeffs = new double[degree + 1];
		int sign = 1;
		for (int i = 0; i < coeffs.length; i++) {
			if (i % 2 != 0) {
				coeffs[i] = 1.0 / MathUtils.Functions.factorial(i) * sign;
				sign *= -1;
			}
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getCos(int degree) {
		double[] coeffs = new double[degree + 1];
		int sign = 1;
		for (int i = 0; i < coeffs.length; i++) {
			if (i % 2 == 0) {
				coeffs[i] = 1.0 / MathUtils.Functions.factorial(i) * sign;
				sign *= -1;
			}
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getSinh(int degree) {
		Polynomial plus = getExp(degree);
		Polynomial minus = getExp(degree).compose(-X);
		return (plus - minus) / 2;
	}

	public static Polynomial getCosh(int degree) {
		Polynomial plus = getExp(degree);
		Polynomial minus = getExp(degree).compose(-X);
		return (plus + minus) / 2;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(getSin(3));
		System.out.println(getSin(4));
		System.out.println(getSin(3).equals(getSin(4)));
	}

}

package mathLib.polynom;

import static mathLib.polynom.Polynomial.*;

import mathLib.util.MathUtils;

public class TaylorExpansion {

	public static Polynomial getExp(int order) {
		double[] coeffs = new double[order + 1];
		for (int i = 0; i < coeffs.length; i++) {
			coeffs[i] = 1.0 / MathUtils.factorial(i);
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getSin(int order) {
		double[] coeffs = new double[order + 1];
		int sign = 1;
		for (int i = 0; i < coeffs.length; i++) {
			if (i % 2 != 0) {
				coeffs[i] = 1.0 / MathUtils.factorial(i) * sign;
				sign *= -1;
			}
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getCos(int order) {
		double[] coeffs = new double[order + 1];
		int sign = 1;
		for (int i = 0; i < coeffs.length; i++) {
			if (i % 2 == 0) {
				coeffs[i] = 1.0 / MathUtils.factorial(i) * sign;
				sign *= -1;
			}
		}
		return new Polynomial(coeffs);
	}

	public static Polynomial getSinh(int order) {
		Polynomial plus = getExp(order);
		Polynomial minus = getExp(order).compose(-X);
		return (plus - minus) / 2;
	}

	public static Polynomial getCosh(int order) {
		Polynomial plus = getExp(order);
		Polynomial minus = getExp(order).compose(-X);
		return (plus + minus) / 2;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(getSin(3));
		System.out.println(getSin(4));
		System.out.println(getSin(3).equals(getSin(4)));
	}

}

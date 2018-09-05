package mathLib.root;

import mathLib.numbers.Complex;
import mathLib.polynom.Polynomial;

public class PolynomialRoot {

	private Polynomial polynomial;
	Complex[] roots;

	public PolynomialRoot(double[] coeffs) {
		polynomial = new Polynomial(coeffs);
		roots = polynomial.getRoots();
	}
	
	public PolynomialRoot(Polynomial p) {
		polynomial = p.copy() ;
		roots = polynomial.getRoots();
	}

	public Polynomial getPolynomial() {
		return polynomial;
	}

	public Complex[] getRootsComplex() {
		return roots;
	}

	public double[] getRootsRealPart() {
		double[] realParts = new double[roots.length];
		for (int i = 0; i < roots.length; i++) {
			realParts[i] = roots[i].re();
		}
		return realParts;
	}

	public double[] getRootsImagPart() {
		double[] imagParts = new double[roots.length];
		for (int i = 0; i < roots.length; i++) {
			imagParts[i] = roots[i].im();
		}
		return imagParts;
	}

}

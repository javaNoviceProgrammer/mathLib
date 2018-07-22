package tests;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import mathLib.numbers.Complex;
import static mathLib.numbers.Complex.*;

public class TestJamaEigen {
	public static void main(String[] args) {
		double[][] ent = new double[][] { { -1, 1 }, { 1, -1 } };
		Matrix A = new Matrix(ent);
		EigenvalueDecomposition eigen = new EigenvalueDecomposition(A);
		// for (double x : eigen.getRealEigenvalues()) {
		// System.out.println(x);
		// }
		// for (double y : eigen.getImagEigenvalues()) {
		// System.out.println(y);
		// }
		double[] eigenReal = eigen.getRealEigenvalues();
		double[] eigenImag = eigen.getImagEigenvalues();
		Complex[] eigenComplex = new Complex[eigenReal.length];
		for (int i = 0; i < eigenReal.length; i++) {
			eigenComplex[i] = eigenReal[i] + j * eigenImag[i];
			System.out.println(eigenComplex[i]);
		}
	}

}

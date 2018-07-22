package tests;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class TestJamaEigen {
	public static void main(String[] args) {
		double[][] ent = new double[][] { { 1, -1.2, 3 }, { 2, 0, 7.2 }, { -1.3, 10.2, 0.5 } };
		Matrix A = new Matrix(ent);
		EigenvalueDecomposition eigen = new EigenvalueDecomposition(A);
		for (double x : eigen.getRealEigenvalues()) {
			System.out.println(x);
		}
		for (double y : eigen.getImagEigenvalues()) {
			System.out.println(y);
		}
	}

}

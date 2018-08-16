package mathLib.matrix.sys;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;

/**
 * Solving A*x = y 
 *
 */

public class ComplexLinearSystem {

	public ComplexLinearSystem(final Complex A[][], final Complex Y[], Complex X[]) {
		int n = A.length;
		Complex B[][] = new Complex[n][n + 1]; // working matrix
		if (A[0].length != n || Y.length != n || X.length != n) {
			throw new IllegalArgumentException("Error in LinearSystem, inconsistent array sizes.") ;
		}
		// build working data structure
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				B[i][j] = A[i][j];
			B[i][n] = Y[i];
		}
		solve(n, B, X);
	}

	ComplexLinearSystem(int n, final Complex A[][], final Complex Y[], Complex X[]) {
		Complex B[][] = new Complex[n][n + 1]; // working matrix
		// build working data structure
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				B[i][j] = A[i][j];
			B[i][n] = Y[i];
		}
		solve(n, B, X);
	}

	ComplexLinearSystem(int n, final Complex AA[], final Complex Y[], Complex X[]) {
		Complex B[][] = new Complex[n][n + 1]; // working matrix

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				B[i][j] = AA[i * n + j];
			B[i][n] = Y[i];
		}
		solve(n, B, X);
	}

	ComplexLinearSystem(int n, final Complex AA[], Complex X[]) {
		Complex B[][] = new Complex[n][n + 1]; // working matrix

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= n; j++)
				B[i][j] = AA[i * (n + 1) + j];
		}
		solve(n, B, X);
	}

	ComplexLinearSystem(int n, final Complex B[][], Complex X[]) {
		solve(n, B, X);
	}

	private void solve(final int n, final Complex B[][], Complex X[]) {
		int hold, I_pivot; // pivot indicies
		Complex pivot; // pivot element value
		double abs_pivot;
		int row[] = new int[n]; // row interchange indicies

		// set up row interchange vectors
		for (int k = 0; k < n; k++) {
			row[k] = k;
		}
		// begin main reduction loop
		for (int k = 0; k < n; k++) {
			// find largest element for pivot
			pivot = B[row[k]][k];
			abs_pivot = ComplexMath.abs(pivot);
			I_pivot = k;
			for (int i = k; i < n; i++) {
				if (ComplexMath.abs(B[row[i]][k]) > abs_pivot) {
					I_pivot = i;
					pivot = B[row[i]][k];
					abs_pivot = ComplexMath.abs(pivot);
				}
			}
			// have pivot, interchange row indicies
			hold = row[k];
			row[k] = row[I_pivot];
			row[I_pivot] = hold;
			// check for near singular
			if (abs_pivot < 1.0E-20) {
				for (int j = k + 1; j < n + 1; j++) {
					B[row[k]][j] = 0.0;
				}
				System.out.println("redundant row (singular) " + row[k]);
			} // singular, delete row
			else {
				// reduce about pivot
				for (int j = k + 1; j < n + 1; j++) {
					B[row[k]][j] = B[row[k]][j] / B[row[k]][k];
				}
				// inner reduction loop
				for (int i = 0; i < n; i++) {
					if (i != k) {
						for (int j = k + 1; j < n + 1; j++) {
							B[row[i]][j] = B[row[i]][j] - B[row[i]][k] * B[row[k]][j];
						}
					}
				}
			}
			// finished inner reduction
		}
		// end main reduction loop
		// build X for return, unscrambling rows
		for (int i = 0; i < n; i++) {
			X[i] = B[row[i]][n];
		}
	} // end solve

}

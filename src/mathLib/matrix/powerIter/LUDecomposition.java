package mathLib.matrix.powerIter;

/**
 * LU Decomposition.
 * 
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n.
 */

public class LUDecomposition {

	public LUDecomposition(PowerIterationMatrix l, PowerIterationMatrix u) {
		super();
		L = l;
		U = u;
	}

	public PowerIterationMatrix getL() {
		return L;
	}

	public PowerIterationMatrix getU() {
		return U;
	}

	private PowerIterationMatrix L;
	private PowerIterationMatrix U;

}

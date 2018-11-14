package mathLib.fem.core.intf;

import mathLib.func.symbolic.intf.MathFunc;

/**
 * The left hand side (LHS) expression of a weak form
 *
 */
public interface LHSExpr {
	
	/**
	 * Return the bilinear form of the left hand side of a weak form
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	MathFunc apply(MathFunc u, MathFunc v);
}

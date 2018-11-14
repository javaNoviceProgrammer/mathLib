package mathLib.fem.core.intf;

import mathLib.func.symbolic.intf.MathFunc;

/**
 * The right hand side (RHS) expression of a weak form
 *
 */
public interface RHSExpr {

	/**
	 * Return the linear form of the right hand side of a weak form
	 * 
	 * @param v
	 * @return
	 */
	MathFunc apply(MathFunc v);
}

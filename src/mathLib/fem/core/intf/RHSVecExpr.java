package mathLib.fem.core.intf;

import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;

/**
 * The right hand side (RHS) expression of a weak form with
 * vector valued shape functions
 *
 */
public interface RHSVecExpr {
	
	/**
	 * Return the linear form of the right hand side
	 * @param v
	 * @return
	 */
	MathFunc apply(VecMathFunc v);
}

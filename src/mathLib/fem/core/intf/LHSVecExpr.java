/**
 * Copyright (c) 2010, nkliuyueming@gmail.com. All rights reserved.
 * 
 * 
 */
package mathLib.fem.core.intf;

import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;

/**
 * The left hand side (LHS) expression of a weak form with
 * vector valued shape functions
 *
 */
public interface LHSVecExpr {
	
	/**
	 * Return the bilinear form of the left hand side
	 * @param u
	 * @param v
	 * @return
	 */
	MathFunc apply(VecMathFunc u, VecMathFunc v);
}

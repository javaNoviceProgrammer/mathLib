package mathLib.fem.core.intf;


import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;

public interface WeakFormOld {
	static enum ItemType {Domain, Border};

	//--- Common approach providing weak form interfaces to assembler-----

//	void setShapeFunction(ShapeFunction trial, int trialDofLocalIndex,
//			ShapeFunction test, int testDofLocalIndex);

	/**
	 * Set DOF objects to the weak form. These objects contain shape functions,
	 * local and global index of DOFs and geometry information of the geometry
	 * objects that possess the corresponding DOF objects
	 * <p>
	 */
	void setDOF(DOF trialDOF, DOF testDOF);

	/**
	 * Get degree of freedom(DOF) which contains trial shape function
	 *
	 * @return
	 */
	DOF getTrialDOF();

	/**
	 * Get degree of freedom(DOF) which contains test shape function
	 *
	 * @return
	 */
	DOF getTestDOF();

	/**
	 * Left hand side of the weak form
	 *
	 * @param e
	 * @param itemType
	 * @return
	 */
	MathFunc leftHandSide(Element e, ItemType itemType);

	/**
	 * Right hand side of the weak form
	 * @param e
	 * @param itemType
	 * @return
	 */
	MathFunc rightHandSide(Element e, ItemType itemType);

	/**
	 * Provide a pre-process function before calling leftHandSide(...) and rightHandSide(...) if necessary
	 * @param e
	 */
	void preProcess(Element e);

	//----------------------------------------------------------

	//--- Fast approach providing weak form interface to assembler-----
	/**
	 * Assemble element <code>e</code> here, instead of providing left hand side
	 * and right hand side.
	 *
	 * @param e
	 * @param globalStiff (I/O): Global stiff matrix
	 * @param globalLoad (I/O): Global load vector
	 *
	 */
	void assembleElement(Element e,
			Matrix globalStiff, Vector globalLoad);
	//--------------------------------------------------------

	/**
	 * Integrate on element <code>e</code>
	 *
	 * @param e
	 * @param fun: LHS or RHS
	 * @return
	 */
	double integrate(Element e, MathFunc fun);

	/**
	 * This interface has NO meaning for scalar valued problems.
	 * For vector valued problems, it is used to indicate if two components of
	 * vector function are coupled or independent variables. This information
	 * can be used to simplify the assembling process. For example, for 2D Stokes
	 * problem (u v p): u and v are independent while u and p or v and p are coupled.
	 *
	 * @param nComponent1
	 * @param nComponent2
	 * @return
	 */
	boolean isVVFComponentCoupled(int nComponent1, int nComponent2);


}

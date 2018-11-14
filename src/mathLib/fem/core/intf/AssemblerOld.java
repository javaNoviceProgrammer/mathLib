package mathLib.fem.core.intf;

import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.matrix.algebra.intf.SparseMatrix;
import mathLib.matrix.algebra.intf.SparseVector;

/**
 * Assembler Interface
 * 
 *
 */
public interface AssemblerOld {
	/**
	 * Assemble element's contributions to global stiffness matrix and global load vector
	 * <p> 
	 */
	void assemble();

	/**
	 * Return assembled global stiffness matrix
	 * @return
	 */
	SparseMatrix getStiffnessMatrix();
	
	/**
	 * Return assembled global load vector
	 * @return
	 */
	SparseVector getLoadVector();
	
	/**
	 * Impose Dirichlet boundary condition constraints for
	 * scalar valued problems. This function will affect
	 * global stiffness matrix and global load vector
	 * 
	 * @param diri
	 */
	void imposeDirichletCondition(MathFunc diri);
	
	/**
	 * Impose Dirichlet boundary condition constraints for 
	 * vector valued problems. This function will affect
	 * global stiffness matrix and global load vector
	 * 
	 * @param diri
	 */
	void imposeDirichletCondition(VecMathFunc diri);
}

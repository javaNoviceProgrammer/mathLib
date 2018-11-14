package mathLib.matrix.algebra.intf;

import java.util.Map;

public interface SparseVector extends Vector,Iterable<VectorEntry> {

	int getNonZeroNumber();
	
	Map<Integer,Double> getAll();
	
	SparseVector setAll(int nBase, Map<Integer,Double> dataMap);
	
	/**
	 * Clear all values but keep dimension of vector
	 */
	void clearData();
	
	/**
	 * Clear all values and set dimension of vector to zero.
	 * <p>
	 */
	void clearAll();
	
	/**
	 * An overriding method can also return a subtype of the type returned by the overridden method. 
	 * This is called a covariant return type.
	 */
	SparseVector setName(String name);
	SparseVector copy();
	SparseVector set(Vector y);
	SparseVector set(double a, Vector y);
	SparseVector add(Vector y);
	SparseVector add(double a, Vector y);
	SparseVector scale(double a);
	SparseVector ax(double a);
	SparseVector axpy(double a, Vector y);
	SparseVector axMuly(double a, Vector y);
	SparseVector axDivy(double a, Vector y);
	SparseVector shift(double dv);
}

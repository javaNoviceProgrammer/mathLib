package mathLib.matrix.algebra.intf;

import java.util.Map;

public interface SparseMatrix extends Matrix,Iterable<MatrixEntry> {

	int getNonZeroNumber();
	
	
	/**
	 * get all non-zero element, instead of iterator
	 * 
	 */
	Map<Integer,Map<Integer,Double>> getAll();

	void setAll(int nRowBase, int nColBase, Map<Integer,Map<Integer,Double>> dataMap);

	void clearAll();

	void clearData();

	SparseMatrix trans();
	SparseMatrix copy();
	SparseMatrix setName(String name);
}

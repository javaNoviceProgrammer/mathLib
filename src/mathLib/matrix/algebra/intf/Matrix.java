package mathLib.matrix.algebra.intf;



/**
 * <blockquote><pre>
 * General matrix interface
 * </blockquote></pre>
 * 
 *
 */
public interface Matrix {

	public static double zeroEps = 1e-15;
	
	void setRowDim(int nRowDim);

	int getRowDim();
	
	void setColDim(int nColDim);

	int getColDim();

	double get(int row, int col);
	
	double apply(int row, int col);

	void set(int row, int col, double value);

	void update(int row, int col, double value);

	void add(int row, int col,double value);

	void mult(Vector x, Vector y);

	Matrix trans();

	Matrix copy();

	void print();
	
	/**
	 * Set matrix name for printing purpose or using in Matlab as variable name
	 * 
	 * @param name Matrix name
	 * @return <tt>this</tt> for convenience only
	 */
	Matrix setName(String name);
	
	/**
	 * Get matrix name
	 * 
	 * @return Matrix name
	 */
	String getName();
	
	/**
	 * Write this matrix to file <tt>fileName</tt> with Matlab mat file format
	 * <p>
	 * If more than one matrix need to be written in a single mat file use <tt>MatlabMatFileWriter</tt> instead.
	 * @param fileName
	 */
	void writeMatFile(String fileName);
	
	/**
	 * Write this matrix to file <tt>fileName</tt> with simple text file format
	 * @param fileName
	 */
	void writeSimpleFile(String fileName);
	
	//writeMatrixMarketFile(String fileName);
}
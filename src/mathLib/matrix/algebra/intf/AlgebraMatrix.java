package mathLib.matrix.algebra.intf;

/**
 * Matrix interface designed for fast algebra operation
 * 
 */
public interface AlgebraMatrix {
	/**
	 * Get number of rows
	 */
	public int getRowDim();
	
	/**
	 * Get number of columns
	 * @return
	 */
	public int getColDim();
	
	/**
	 * Matrix vector multiplication
	 * y=this*x (y=A*x)
	 * 
	 * @param x
	 * @param y
	 */
	public void mult(AlgebraVector x, AlgebraVector y);

	/**
	 * Matrix matrix multiplication
	 * C = this*B (C=A*B)
	 * 
	 * @param x
	 * @param y
	 */
	public void mult(AlgebraMatrix B, AlgebraMatrix C);
	
	/**
	 * Get transpose of A
	 * 
	 * @return A'
	 */
	public AlgebraMatrix getTrans();
	
	/**
	 * print matrix entries
	 */
	public void print();

}

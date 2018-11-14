package mathLib.matrix.algebra.intf;

/**
 * Vector interface designed for fast algebra operation
 *
 */
public interface AlgebraVector {
	/**
	 * Get the dimension of the vector
	 * 
	 * @return
	 */
	public int getDim();
	
	/**
	 * Get <tt>double</tt> array of the vector 
	 * 
	 * @return
	 */
	public double[] getData();
	
	/**
	 * <code>x = y</code>
	 * 
	 * @param y
	 */
	public AlgebraVector set(AlgebraVector y);

	/**
	 * <code>x = a*y</code>
	 * 
	 * @param y
	 */
	public AlgebraVector set(double a, AlgebraVector y);
	
	/**
	 * <code>x = x + y</code>
	 * 
	 * @param a
	 * @param y
	 */
	public AlgebraVector add(AlgebraVector y);
	
	/**
	 * <code>x = x - y</code>
	 * 
	 * @param a
	 * @param y
	 */
	public AlgebraVector subtract(AlgebraVector y);	

	/**
	 * <code>x = x + a*y</code>
	 * 
	 * @param a
	 * @param y
	 */
	public AlgebraVector add(double a, AlgebraVector y);

	/**
	 * <code>x = a*x</code>
	 * 
	 * @param a
	 * @return
	 */
	public AlgebraVector scale(double a);

	/**
	 * <code>x = a*x</code>
	 * Alias of <code>scale(double a)</code>
	 * <p>
	 * <code>scale(double a)</code>
	 * 
	 * @param a
	 * @return
	 */
	public AlgebraVector ax(double a);
	
	/**
	 * <code>x = a*x+y</code>
	 * <p>
	 * Notice: Different from <code>add(double a, AlgebraVector y)</code>
	 * <p>
	 * <code>add(double a, AlgebraVector y)</code>
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public AlgebraVector axpy(double a, AlgebraVector y);
	
	/**
	 * <code>x = (a*x).*y</code>
	 * 
	 * @param a
	 * @param y
	 * @return
	 */
	public AlgebraVector axmy(double a, AlgebraVector y);
	
	/**
	 * Inner product <code>x.y</code>
	 * <p>
	 * 
	 * 
	 * @param y
	 * @return
	 */
	public double dot(AlgebraVector y);
	
	/**
	 * 1 Norm
	 * 
	 * @return
	 */
	public double norm1();
	
	/**
	 * 2 Norm
	 * 
	 * @return
	 */
	public double norm2();
	
	/**
	 * Infinity norm (Maximum norm)
	 * 
	 * @return
	 */
	public double normInf();
	
	/**
	 * Print vector entries
	 */
	public void print();

}

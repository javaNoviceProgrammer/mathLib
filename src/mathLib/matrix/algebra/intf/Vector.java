package mathLib.matrix.algebra.intf;


/**
 * <blockquote><pre>
 * General vector interface
 * </blockquote></pre>
 * 
 * 
 */
public interface Vector {
	/**
	 * Get dimension of the vector
	 */
	int getDim();
	
	/**
	 * Set dimension of the vector
	 * 
	 * @param dim
	 */
	void setDim(int dim);
	
	/**
	 * Get <code>x(index)</code>
	 * 
	 * @param index
	 * @return <code>x(index)</code>
	 */
	double get(int index);
	
	/**
	 * Alias of get(int index), used in ScalaFEM as syntactic sugar: 
	 * <code>val a = x(index)</code>
	 * 
	 * @param index
	 * @return <code>x(index)</code>
	 */
	double apply(int index);
	
	/**
	 * <code>x(index) = value</code>
	 * 
	 * @param index
	 * @param value
	 */
	void set(int index, double value);
	
	/**
	 * Alias of set(int index, double value), used in ScalaFEM as syntactic sugar: 
	 * <code>x(index)=value</code>
	 * 
	 * @param index
	 * @param value
	 */
	void update(int index, double value);
	
	/**
	 * Set all the values of vector to a single value <tt>value</tt>
	 * <p>
	 * 
	 * @param value
	 */
	Vector setAll(double value);
	
	/**
	 * <code>x(index) += value</code>
	 * 
	 * @param index
	 * @param value
	 */
	void add(int index,double value);
	
	////////////////////////////////////////////////
	
	/**
	 * <code>x(i)=y(i), i=1...dim</code>
	 * 
	 * @param y
	 */
	Vector set(Vector y);
	
	/**
	 * <code>x(i)=a*y(i), i=1...dim</code>
	 * 
	 * @param y
	 */
    Vector set(double a, Vector y);
	
	/**
	 * <code>x = x + y</code>
	 * 
	 * @param a
	 * @param y
	 */
	Vector add(Vector y);
	
	/**
	 * <code>x = x + a*y</code>
	 * 
	 * @param a
	 * @param y
	 */
	Vector add(double a, Vector y);
	
	/**
	 * <code>x = a*x</code>
	 * 
	 * @param a
	 * @return
	 */
	Vector scale(double a);
	
	/**
	 * <code>x = a*x</code>
	 * 
	 * @param a
	 * @return
	 */
	Vector ax(double a);
	
	/**
	 * <code>x = a*x + y</code>
	 * 
	 * @param a
	 * @param y
	 * @return
	 */
	Vector axpy(double a, Vector y);
	
	/**
	 * xi = a*xi*yi
	 * @param a
	 * @param y
	 * @return
	 */
	Vector axMuly(double a, Vector y);
	
	/**
	 * xi = a*xi/yi
	 * @param a
	 * @param y
	 * @return
	 */
	Vector axDivy(double a, Vector y);
	
	/**
	 * <code>x = x + dv</code>
	 * 
	 * @param dv
	 * @return
	 */
	Vector shift(double dv);
	
	/**
	 * Dot product, returns <code>x1*y1 + x2*y2 + ... + xn*yn</code>
	 * 
	 * @param y = (y1 y2 ... yn)'
	 * @return
	 */
	double dot(Vector y);
	
	/**
	 * <code>sum(abs(x))</code>
	 * ä¸€èŒƒæ•°
	 * 
	 * @return
	 */
	double norm1();

	/**
	 * <code>sqrt(sum(abs(x).^2))</code>
	 * 
	 * @return
	 */
	double norm2();
	
	/**
	 * <code>max(abs(x))</code>
	 * 
	 * @return
	 */
	double normInf();
	
	///////////////////////////////////////////////////////
	
	/**
	 * Deep copy of the vector
	 * <p>
	 * 
	 * @return
	 */
	Vector copy();
	
	/**
	 * Print the component values of the vector
	 * <p>
	 */
	void print();
	
	/**
	 * Set vector name for printing purpose or using in Matlab as variable name
	 * 
	 * @param name Vector name
	 * @return <tt>this</tt> for convenience only
	 */
	Vector setName(String name);
	
	/**
	 * Get vector name
	 * 
	 * @return Vector name
	 */
	String getName();
	
	/**
	 * Write this vector to a file with Matlab mat file format.
	 * The variable name in matlab workspace is specified by <tt>setName()</tt>.
	 * <p>
	 * If more than one vector need to be written in a single mat file use <tt>MatlabMatFileWriter</tt> instead.
	 * 
	 * @param fileName
	 */
	void writeMatFile(String fileName);
	
	/**
	 * Write this vector to file <tt>fileName</tt> with simple text file format
	 * @param fileName
	 */
	void writeSimpleFile(String fileName);
}

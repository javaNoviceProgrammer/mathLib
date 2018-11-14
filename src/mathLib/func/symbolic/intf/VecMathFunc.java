package mathLib.func.symbolic.intf;

import java.util.List;
import java.util.Map;

import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.VariableArray;
import mathLib.matrix.algebra.intf.Vector;

/**
 * Vector valued mathematical function
 *
 */
public interface VecMathFunc {
	/**
	 * Returns the value of vector function at <tt>v</tt>
	 * <p>
	 * 
	 * @param v
	 * @return
	 */
	Vector value(Variable v);
	
	/**
	 * Returns the array of value of vector function at <tt>valAry</tt>
	 * 
	 * @param v
	 * @param cache
	 * @return
	 */
	Vector[] valueArray(VariableArray valAry, Map<Object,Object> cache);
	
	/**
	 * Set variable names of the vector function
	 * <p>
	 * 
	 * @param varNames
	 */
	void setVarNames(List<String> varNames);
	
	/**
	 * Returns variable names of the vector function
	 * <p>
	 * 
	 * @return
	 */
	List<String> varNames();
	
	/**
	 * Get dimension of vector valued function
	 * <p>
	 * 
	 * @return
	 */
	int getDim();
	
	/**
	 * Set dimension of vector valued function
	 * <p>
	 * 
	 * @return
	 */
	void setDim(int dim);
	
	/**
	 * Set component <tt>index</tt> to function <tt>value</tt>
	 * <p>
	 * 
	 * @param index
	 * @param value
	 */
	void set(int index, MathFunc value);
	
	/**
	 * Get function at <tt>index</tt>
	 * <p>
	 * 
	 * @param index
	 * @return
	 */
	MathFunc get(int index);

	/**
	 * Composite vector valued function
	 * <p>
	 * 
	 * @param fInners
	 * @return
	 */
	VecMathFunc compose(Map<String,MathFunc> fInners);

	///////////////////////////////////////////////
	
	/**
	 * <code>fi(x)=gi(x), i=1...dim</code>
	 * <p>
	 * 
	 * @param <code>\vec{g}(x)=(g1(x),g2(x),...,gn(x)</code>
	 */
	VecMathFunc set(VecMathFunc g);
	
	/**
	 * <code>fi(x)=a*gi(x), i=1...dim</code>
	 * <p>
	 * 
	 * @param <code>\vec{g}(x)=(g1(x),g2(x),...,gn(x)</code>
	 */
	VecMathFunc set(double a, VecMathFunc g);
	
	/**
	 * <code>\vec{f}(x) = \vec{f}(x) + \vec{g}(x)</code>
	 * 
	 * @param <code>\vec{g}(x)=(g1(x), g2(x), ..., gn(x))</code>
	 */
	VecMathFunc inc(VecMathFunc g);
	
	/**
	 * <code>\vec{f}(x) = \vec{f}(x) + a*\vec{g}(x)</code>
	 * 
	 * @param a
	 * @param <code>\vec{g}(x)=(g1(x), g2(x), ..., gn(x))</code>
	 */
	VecMathFunc inc(double a, VecMathFunc g);
	
	/**
	 * <code>\vec{f}(x) = a*\vec{f}(x)</code>
	 * 
	 * @param a
	 * @return
	 */
	VecMathFunc scale(double a);
	
	/**
	 * <code>\vec{f}(x) = a*\vec{f}(x)</code>
	 * 
	 * @param a
	 * @return
	 */
	VecMathFunc ax(double a);
	
	/**
	 * <code>\vec{f}(x) = a*\vec{f}(x) + \vec{g}(x)</code>
	 * 
	 * @param a
	 * @param <code>\vec{g}(x)=(g1(x), g2(x), ..., gn(x))</code>
	 * @return
	 */
	VecMathFunc axpy(double a, VecMathFunc g);
	
	/**
	 * Dot product, returns 
	 * <p>
	 * <code>f1(x)*g1(x) + f2(x)*g2(x) + ... + fn(x)*gn(x)</code>
	 * <p>
	 * 
	 * @param <code>\vec{g}(x) = (g1(x), g2(x), ..., gn(x))</code>
	 * @return
	 */
	MathFunc dot(VecMathFunc g);
	
	/**
	 * Dot product, returns
	 * <p>
	 * <code>f1(x)*g1 + f2(x)*g2 + ... + fn(x)*gn</code>
	 * <p>
	 * 
	 * @param <code>\vec{g} = (g1, g2, ..., gn)</code>
	 * @return
	 */
	MathFunc dot(Vector g);	
	
	////////////////////////////////////////////////////
	
	/**
	 * Add
	 * <p><blockquote><pre>
	 * (f1)   (g1)   (f1+g1)
	 * (f2) + (g2) = (f2+g2)
	 * (..)   (..)   ( ... )
	 * (fn)   (gn)   (fn+gn)
	 * </blockquote></pre>
	 * 
	 * @param g
	 * @return
	 */
	VecMathFunc A(VecMathFunc g);
	
	/**
	 * Add
	 * <p><blockquote><pre>
	 * (f1)   (v1)   (f1+v1)
	 * (f2) + (v2) = (f2+v2)
	 * (..)   (..)   ( ... )
	 * (fn)   (vn)   (fn+vn)
	 * </blockquote></pre>
	 * 
	 * @param v
	 * @return
	 */
	VecMathFunc A(Vector v);
	
	/**
	 * Subtract
	 * <p><blockquote><pre>
	 * (f1)   (g1)   (f1-g1)
	 * (f2) - (g2) = (f2-g2)
	 * (..)   (..)   ( ... )
	 * (fn)   (gn)   (fn-gn)
	 * </blockquote></pre>
	 * 
	 * @param g
	 * @return
	 */
	VecMathFunc S(VecMathFunc g);
	
	/**
	 *  Subtract
	 *  <p><blockquote><pre>
	 * (f1)   (v1)   (f1-v1)
	 * (f2) - (v2) = (f2-v2)
	 * (..)   (..)   ( ... )
	 * (fn)   (vn)   (fn-vn)
	 * </blockquote></pre>
	 * 
	 * @param v
	 * @return
	 */
	VecMathFunc S(Vector v);
	
	/**
	 *  Multiply (componentwise) with vector function
	 *  <p><blockquote><pre>
	 * (f1)   (g1)   (f1*g1)
	 * (f2) * (g2) = (f2*g2)
	 * (..)   (..)   ( ... )
	 * (fn)   (gn)   (fn*gn)
	 * </blockquote></pre>
	 * 
	 * @param g
	 * @return
	 */
	VecMathFunc M(VecMathFunc g);
	
	/**
	 * Multiply (componentwise) with vector
	 *  <p><blockquote><pre>
	 * (f1)   (v1)   (v1*f1)
	 * (f2) * (v2) = (v2*f2)
	 * (..)   (..)   ( ... )
	 * (fn)   (vn)   (vn*fn)
	 * </blockquote></pre>
	 * 
	 * @param v
	 * @return
	 */
	VecMathFunc M(Vector v);	
	
	/**
	 *  Divide (componentwise) by vector function
	 * <p><blockquote><pre>
	 * (f1)   (g1)   (f1/g1)
	 * (f2) / (g2) = (f2/g2)
	 * (..)   (..)   ( ... )
	 * (fn)   (gn)   (fn/gn)
	 * </blockquote></pre>
	 * 
	 * @param g
	 * @return
	 */
	VecMathFunc D(VecMathFunc g);
	
	/**
	 * Divide (componentwise) by vector
	 * <p><blockquote><pre>
	 * (f1)   (v1)   (f1/v1)
	 * (f2) / (v2) = (f2/v2)
	 * (..)   (..)   ( ... )
	 * (fn)   (vn)   (fn/vn)
	 * </blockquote></pre>
	 * 
	 * @param v: a Vector
	 * @return
	 */
	VecMathFunc D(Vector v);
	
	/////////////////////////////////////////////////
	
	/**
	 * Deep copy
	 * 
	 * @return
	 */
	VecMathFunc copy();
	
	/**
	 * return the expression of function
	 * 
	 * @return
	 */
	String getExpression();
	
	/**
	 * Get function name
	 * <p>
	 * If function name is not null, the name instead of the expression of
	 * function is returned by <code>toString()</code> method
	 */
	String getFName();
	
	/**
	 * Set function name
	 * <p>
	 * If function name is not null, the name instead of the expression of
	 * function is returned by <code>toString()</code> method
	 * @param name
	 */
	VecMathFunc setFName(String name);
	
	//////////////Operator overloading support through Java-OO//////////////////
	/**
	 * Operator overloading support:
	 * 
	 * MathFunc a = 5;
	 * 
	 */
	VecMathFunc valueOf(int v);
	VecMathFunc valueOf(long v);
	VecMathFunc valueOf(float v) ;
	VecMathFunc valueOf(double v);
	
	/**
	 * Operator overload support:
	 * a+b
	 */
	VecMathFunc add(VecMathFunc other);
	VecMathFunc add(int other);
	VecMathFunc addRev(int other);
	VecMathFunc add(long other);
	VecMathFunc addRev(long other);
	VecMathFunc add(float other);
	VecMathFunc addRev(float other);
	VecMathFunc add(double other);
	VecMathFunc addRev(double other);
	
	/**
	 * Operator overload support:
	 * a-b
	 */
	VecMathFunc subtract(VecMathFunc other);
	VecMathFunc subtract(int other);
	VecMathFunc subtractRev(int other);
	VecMathFunc subtract(long other);
	VecMathFunc subtractRev(long other);
	VecMathFunc subtract(float other);
	VecMathFunc subtractRev(float other);
	VecMathFunc subtract(double other);
	VecMathFunc subtractRev(double other);
	
	/**
	 * Operator overload support:
	 * a*b
	 */
	VecMathFunc multiply(VecMathFunc other);
	VecMathFunc multiply(int other);
	VecMathFunc multiplyRev(int other);
	VecMathFunc multiply(long other);
	VecMathFunc multiplyRev(long other);
	VecMathFunc multiply(float other);
	VecMathFunc multiplyRev(float other);
	VecMathFunc multiply(double other);
	VecMathFunc multiplyRev(double other);
	
	/**
	 * Operator overload support:
	 * a/b
	 */
	VecMathFunc divide(VecMathFunc other);
	VecMathFunc divide(int other);
	VecMathFunc divideRev(int other);
	VecMathFunc divide(long other);
	VecMathFunc divideRev(long other);
	VecMathFunc divide(float other);
	VecMathFunc divideRev(float other);
	VecMathFunc divide(double other);
	VecMathFunc divideRev(double other);
	
	/**
	 * Operator overload support:
	 * -a
	 */
	VecMathFunc negate();
	
}

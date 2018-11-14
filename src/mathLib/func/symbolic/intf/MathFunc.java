package mathLib.func.symbolic.intf;

import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.MethodGen;

import mathLib.fem.assembler.AssembleParam;
import mathLib.func.symbolic.VariableArray;
import mathLib.util.CompiledFunc;

/**
 * Mathematical function interface (scalar valued)
 * 
 */
public interface MathFunc {
	/**
	 * Get the function name
	 * 
	 */
	String getName();
	
	/**
	 * Set a name for the function
	 * 
	 * @param name
	 * @return
	 */
	MathFunc setName(String name);
	
	/**
	 * Set free variable names of the function
	 * 
	 * @param varNames
	 * @return
	 */
	MathFunc setVarNames(List<String> varNames);
	
	/**
	 * Set active variables by variable names of a composite function.
	 * Active variables are the variables used in evaluating a composite function and
	 * computing derivatives of the composite function.
	 * @param varNames - active variable names
	 * @return
	 */
	MathFunc setActiveVarByNames(List<String> varNames);
	
	/**
	 *  Get the active variable names of a composite function
	 * @return
	 */
	List<String> getActiveVarNames();
	
	/**
	 * Set the outer variables of a composite function as active variables
	 */
	MathFunc setOuterVarActive();
	
	/**
	 * Set the inner variables of a composite function as active variables
	 */
	MathFunc setInnerVarActive();
	
	/**
	 * Return true if the outer variables of a composite function is active
	 */
	boolean isOuterVarActive();
	
	/**
	 * Return true if the inner variables of a composite function is active
	 */
	boolean isInnerVarActive();
	
	/**
	 * Return all free variable names of the function
	 * 
	 * @return
	 */
	List<String> getVarNames();
	
	/**
	 * Return function value at variable v
	 * <p>
	 * 
	 * @param v
	 * @return double function value
	 */
	
	double apply(Variable v);
	
	/**
	 * Evaluating a function at point specified by 'args'
	 * The order of free variables in 'args' is determined
	 * by setArgIdx().
	 * @param args
	 * @return
	 */
	double apply(double ...args);
	
	/**
	 * 
	 * @param ap
	 * @param args
	 * @return
	 */
	double apply(AssembleParam ap, double ...args);
	
	/**
	 * Return function value at variable v with cache enabled
	 * <p>
	 * @param v
	 * @param cache
	 * @return
	 */
	@Deprecated
	double apply(Variable v, Map<Object,Object> cache);
	
	/**
	 * For more efficiency, this interface can be used to return function values
	 * at an array of variables at once.
	 * 
	 * @param valAry VariableArray object which represents an array of variable values
	 * @param cache Cache for efficient evaluation of functions. <tt>null</tt> parameter will disable the cache mechanism
	 * @return Function values evaluated at array of variables <tt>valAry</tt> 
	 */
	double[] applyAll(VariableArray valAry, Map<Object, Object> cache);
	
	/**
	 * Set the index in the parameter 'args' of the 'apply(double ...args)' method
	 * for each variable name (that means the order of variables can be specified by this function)
	 *
	 *??? How is this used in code generation?
	 *
	 *??? argsMap in bytecodeGen() is used for this argument index purpose? 
	 *
	 *There are two solutions for argument ordering
	 *1. The old solution, 'apply(VariableNode n)' which in turn use the map in VairableNode
	 *2. use 'setArgIdx(Map<String, Integer> argsMap) and apply(double ...args)'. The draw back of this
	 *is that the function needs to be copied once the index is changed which can cause a lot of complexity
	 *A simple solution may be order the argument in alphabetical order in force by providing a utility function
	 *to convert a 'Map<String, Double> args' to 'double[] args'
	 *
	 * @param argIdx
	 * @return
	 */
	MathFunc setArgIdx(Map<String, Integer> argsMap); 
	
	/**
	 * Get the index map of free variables of the function
	 * @return
	 */
	Map<String, Integer> getArgIdxMap();
	
	/**
	 * Add: this + g
	 */
	MathFunc A(MathFunc g);
	
	/**
	 * Add: this + g
	 */
	MathFunc A(double g);
	
	/**
	 * Subtract: this - g
	 */
	MathFunc S(MathFunc g);
	
	/**
	 * Subtract: this - g
	 */
	MathFunc S(double g);
	
	/**
	 * Multiply: this*g
	 */
	MathFunc M(MathFunc g);
	
	/**
	 * Multiply: this*g
	 */
	MathFunc M(double g);
	
	/**
	 * Divide: this/g
	 */
	MathFunc D(MathFunc g);
	
	/**
	 * Divide: this/g
	 */	
	MathFunc D(double g);
	
	/**
	 * Construct composite function
	 * <p><blockquote><pre>
	 * For example:
	 *  MathFunc f = r*s + 1;
	 *  Map<String, MathFunc> fInners = new HashMap<String, MathFunc>();
	 *  fInners.put("r", x*x);
	 *  fInners.put("s", y+1);
	 *  MathFunc fc = f.compose(fInners);
	 *  System.out.println(fc); //f(x,y) = (x*x)*(y + 1.0) + 1.0
	 * </pre></blockquote>
	 * 
	 */
	MathFunc compose(Map<String,MathFunc> fInners);
	
	/**
	 * First derivative with respect to <code>varName</code> 
	 * <p>
	 * <p>
	 * f(x)._d(x) := \frac{ \partial{this} }{ \partial{varName} }
	 * 
	 * @param varName
	 * @return
	 */
	MathFunc diff(String varName);

	/**
	 * Returns true if it is a constant function
	 * 
	 * @return
	 */
	boolean isConstant();
	
	/**
	 * Returns true if it is an integer
	 * @return
	 */
	boolean isInteger();
	
	/**
	 * Return true if it is a zero
	 * @return
	 */
	boolean isZero();
	
	/**
	 * Return true if it is a real number
	 * @return
	 */
	boolean isReal();
	
	/**
	 * Shallow copy
	 * 
	 * @return
	 */
	MathFunc copy();
	
	/**
	* Return the expression (a string) of the function
	*
	* @return
	*/
	String getExpr();
	
	/**
	 * Definition of operand order
	 */
	static int OP_ORDER0 = 0; //Parenthesis first
	static int OP_ORDER1 = 1; //Exponents next
	static int OP_ORDER2 = 2; //Multiply and Divide next
	static int OP_ORDER3 = 3; //Add and Subtract last of all
	
	/**
	 * Get order of operations (Priority Rules for Arithmetic)
	 * <blockquote><pre>
	 * 0 Brackets first
	 * 1 Exponents next
	 * 2 Multiply and Divide next
	 * 3 Add and Subtract last of all.
	 * </blockquote></pre>  
	 */	
	int getOpOrder();
	
	/**
	 * Set order of operations (Priority Rules for Arithmetic)
	 * <blockquote><pre>
	 * 0 Brackets first
	 * 1 Exponents next
	 * 2 Multiply and Divide next
	 * 3 Add and Subtract last of all.
	 * </blockquote></pre>  
	 * @param order
	 */
	void setOpOrder(int order);
	
	/**
	 * Implement this function for your own compilation
	 * 
	 * @param clsName
	 * @param mg
	 * @param cp
	 * @param factory
	 * @param il
	 * @param argsMap Arguments name=>index pair
	 * @param argsStartPos Start index of 'args' in generated function apply()
	 * @param funcRefsMap An array of objects in the expression that implements MathFun 
	 * @return
	 */
	InstructionHandle bytecodeGen(String clsName, MethodGen mg, 
			ConstantPoolGen cp, InstructionFactory factory, 
			InstructionList il, Map<String, Integer> argsMap, 
			int argsStartPos, Map<MathFunc, Integer> funcRefsMap);

	/**
	 * Use ASM library to generate bytecode.
	 * Two libraries are supported at the same time
	 * ASM library version implemented more features
	 * 
	 * @param mv
	 * @param clsName TODO
	 */
	void bytecodeGen(MethodVisitor mv, Map<String, Integer> argsMap,
			int argsStartPos, Map<MathFunc, Integer> funcRefsMap, String clsName);

	/**
	 * Compile the function to bytecode with a specified order of arguments
	 * If no argument provided, it uses the the call of getVarNames()
	 * 
	 * @param varNames
	 * @return
	 */
	CompiledFunc compile(String ...varNames);
	
	/**
	 * Compile the function to bytecode with a specified order of
	 * arguments with ASM library (an independent way of bytecode generation)
	 * 
	 * @param varNames
	 * @return
	 */
	CompiledFunc compileWithASM(String ...varNames);
	
	/**
	 * Set the flag so that the evaluation results of this expression 
	 * is compiled to a static filed of the generated class. Any expression
	 * that contains this expression will access the static filed instead for
	 * evaluation.
	 * 
	 * The default flag is false
	 * 
	 * @param flag
	 */
	void compileToStaticField(boolean flag);
	
	//////////////Operator overloading support through Java-OO//////////////////
	/**
	 * Operator overloading support:
	 * 
	 * MathFunc a = 5;
	 * 
	 */
	MathFunc valueOf(int v);
	MathFunc valueOf(long v);
	MathFunc valueOf(float v) ;
	MathFunc valueOf(double v);
	
	/**
	 * Operator overload support:
	 * a+b
	 */
	MathFunc add(MathFunc other);
	MathFunc add(int other);
	MathFunc addRev(int other);
	MathFunc add(long other);
	MathFunc addRev(long other);
	MathFunc add(float other);
	MathFunc addRev(float other);
	MathFunc add(double other);
	MathFunc addRev(double other);
	
	/**
	 * Operator overloading support for Groovy
	 * a+b
	 */
	default MathFunc plus(MathFunc other) {
		return add(other);
	}
	default MathFunc plus(int other) {
		return add(other);
	}
	default MathFunc plus(long other) {
		return add(other);
	}
	default MathFunc plus(float other) {
		return add(other);
	}
	default MathFunc plus(double other) {
		return add(other);
	}
	
	/**
	 * Operator overload support:
	 * a-b
	 */
	MathFunc subtract(MathFunc other);
	MathFunc subtract(int other);
	MathFunc subtractRev(int other);
	MathFunc subtract(long other);
	MathFunc subtractRev(long other);
	MathFunc subtract(float other);
	MathFunc subtractRev(float other);
	MathFunc subtract(double other);
	MathFunc subtractRev(double other);
	
	/**
	 * Operator overloading support for Groovy:
	 * a-b
	 */
	default MathFunc minus(MathFunc other) {
		return subtract(other);
	}
	default MathFunc minus(int other) {
		return subtract(other);
	}
	default MathFunc minus(long other) {
		return subtract(other);
	}
	default MathFunc minus(float other) {
		return subtract(other);
	}
	default MathFunc minus(double other) {
		return subtract(other);
	}

	
	/**
	 * Operator overload support:
	 * a*b
	 */
	MathFunc multiply(MathFunc other);
	MathFunc multiply(int other);
	MathFunc multiplyRev(int other);
	MathFunc multiply(long other);
	MathFunc multiplyRev(long other);
	MathFunc multiply(float other);
	MathFunc multiplyRev(float other);
	MathFunc multiply(double other);
	MathFunc multiplyRev(double other);
	
	/**
	 * Operator overload support:
	 * a/b
	 */
	MathFunc divide(MathFunc other);
	MathFunc divide(int other);
	MathFunc divideRev(int other);
	MathFunc divide(long other);
	MathFunc divideRev(long other);
	MathFunc divide(float other);
	MathFunc divideRev(float other);
	MathFunc divide(double other);
	MathFunc divideRev(double other);

	/**
	 * Operator overloading support for Groovy:
	 * a/b
	 */
	default MathFunc div(MathFunc other) {
		return divide(other);
	}
	default MathFunc div(int other) {
		return divide(other);
	}
	default MathFunc div(long other) {
		return divide(other);
	}
	default MathFunc div(float other) {
		return divide(other);
	}
	default MathFunc div(double other) {
		return divide(other);
	}

	/**
	 * Operator overload support:
	 * -a
	 */
	MathFunc negate();

}

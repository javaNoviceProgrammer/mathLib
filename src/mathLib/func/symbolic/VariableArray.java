package mathLib.func.symbolic;

import java.util.LinkedHashMap;
import java.util.Map;

import mathLib.fem.core.Element;
import mathLib.fem.util.Constant;

/**
 * Array of Function arguments.
 * In mathematical context, this class represents an array of
 * independent variables of functions.
 *
 */
public class VariableArray {
	private int length = 0;
	protected Map<String,double[]> valMap = new LinkedHashMap<String,double[]>();

	//Node Indices
	protected int[] indices = null;

	protected Element element = null;

	/**
	 * Construct an empty variable array.
	 * Use set() to set values for a variable of function
	 */
	public VariableArray() {
	}

	/**
	 * Construct values array of 1D variable x
	 * <p>
	 *
	 */
	public VariableArray(double[] valAry) {
		length = valAry.length;
		valMap.put(Constant.x, valAry);
	}

	/**
	 * Construct values array of 1D variable <tt>name</tt>
	 *
	 * @param name Variable name
	 * @param valAry Array of variable values
	 */
	public VariableArray(String name, double[] valAry) {
		length = valAry.length;
		valMap.put(name, valAry);
	}

	/**
	 * Get values array of 1D variable x
	 * <p>
	 *
	 * @return
	 */
	public double[] get() {
		return valMap.get(Constant.x);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public double[] get(String name) {
		return valMap.get(name);
	}

	/**
	 * Set value array for variable <tt>name</tt>
	 *
	 * @param name variable name
	 * @param valAry value array
	 * @return this variable
	 */
	public VariableArray set(String name, double[] valAry) {
		length = valAry.length;
		valMap.put(name, valAry);
		return this;
	}

	/**
	 * Get the map of variable name and value array
	 *
	 * @return
	 */
	public Map<String,double[]> getValues() {
		return valMap;
	}

	/**
	 * Set an integer index for this variable.
	 * This index can be an index of a vector.
	 * <p>
	 *
	 *
	 * @param index
	 * @return this variable
	 */
	public VariableArray setIndices(int[] index) {
		this.indices = index;
		return this;
	}

	/**
	 * Get the integer index which is set by <tt>setIndex()</tt>
	 *
	 * @return
	 */
	public int[] getIndices() {
		return indices;
	}


	/**
	 * Set an element reference for this variable.
	 * <p>
	 *
	 * @param element finite element object
	 * @return this variable
	 */
	public VariableArray setElement(Element element) {
		this.element = element;
		return this;
	}

	/**
	 * Get the element object which is set by <tt>setElement()</tt>
	 * <p>
	 *
	 * @return
	 */
	public Element getElement() {
		return element;
	}

	public int length() {
		return this.length;
	}

	public String toString() {
		return this.getValues().toString();
	}

}

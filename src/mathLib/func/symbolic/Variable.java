package mathLib.func.symbolic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mathLib.fem.core.Element;
import mathLib.fem.core.geometry.Point;
import mathLib.fem.util.Constant;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * Function arguments (Independent variables of a function)
 *
 */
public class Variable {
	/**
	 * We use LinkedHashMap here. LinkedHashMap is hash table and linked list implementation of the Map interface,
	 * with predictable iteration order.
	 * <p>
	 */
	protected Map<String,Double> valMap = new LinkedHashMap<String,Double>();

	//protected double[] valArray = new double[9];
	//protected boolean[] useArray = {false,false,false,false,false,false,false,false,false};
	//protected boolean bApplyRestirct = false;

	//Node Index
	protected int index = 0;

	protected Element element = null;

	public Variable() {
	}

	public Variable(double val) {
		//valArray[0] = val;
		//useArray[0] = true;
		valMap.put(Constant.x, val);
	}

	public double get() {
		//return valArray[0];
		return valMap.values().iterator().next();
	}

	public Variable(String name, double val) {
		valMap.put(name, val);
	}

	public Variable(VarPair first, VarPair ...pairs) {
		valMap.put(first.name, first.value);
		for(int i=0;i<pairs.length;i++) {
			valMap.put(pairs[i].name, pairs[i].value);
		}
	}

	public double get(String name) {
		return valMap.get(name);
	}
	public double get(VN name) {
		return valMap.get(VN.names[name.getID()]);
	}

	/**
	 * Alias of get(String name), used in ScalaFEM as syntactic sugar:
	 * <code>v(name)</code>
	 *
	 * @param name
	 * @return <tt>v(name)</tt>
	 */
	public double apply(String name) {
		return valMap.get(name);
	}

	@SuppressWarnings("unlikely-arg-type")
	public double apply(VN name) {
		return valMap.get(name);
	}

	public double get(VarPair pair) {
		pair.value = valMap.get(pair.name);
		return pair.value;
	}

	/**
	 * Variable v = new Variable("x",1).set("y",2);
	 */
	public Variable set(String name, double val) {
		valMap.put(name, val);
		return this;
	}
	public Variable set(VN name, double val) {
		valMap.put(VN.names[name.getID()],val);
		return this;
	}

	public Variable set(VarPair pair) {
		valMap.put(pair.name, pair.value);
		return this;
	}

	public Map<String,Double> getNameValuePairs() {
		return valMap;
	}

	public double[] getVarValues() {
		double[] vals = new double[this.valMap.size()];
		int i = 0;
		for(Entry<String, Double> e : valMap.entrySet()) {
			vals[i] = e.getValue();
			i++;
		}
		return vals;
	}

	public String[] getVarNames() {
		String[] names = new String[this.valMap.size()];
		int i = 0;
		for(Entry<String, Double> e : valMap.entrySet()) {
			names[i] = e.getKey();
			i++;
		}
		return names;
	}

	public static Variable createFrom(MathFunc fun, Point point, int index) {
		if(fun == null)
			return null;
		Variable var = new Variable();
		var.setIndex(index);
		if(fun.getVarNames() != null) {
			int ic = 1;
			for(String vn : fun.getVarNames()) {
				var.set(vn, point.coord(ic));
				ic++;
			}
		} else {
			//VectorBasedFunction
		}
		return var;
	}

	public Variable setIndex(int index) {
		this.index = index;
		return this;
	}
	public int getIndex() {
		return index;
	}

	public Element getElement() {
		return element;
	}
	public Variable setElement(Element element) {
		this.element = element;
		return this;
	}

	public String toString() {
		return this.getNameValuePairs().toString();
	}

	public static void main(String[] args) {
		Variable v1 = new Variable();
		System.out.println(v1);
		Variable v2 = new Variable(new VarPair("x",1.0));
		System.out.println(v2);
		Variable v3 = new Variable(new VarPair("x",1.0),
				new VarPair("y",2.0));
		System.out.println(v3);

		Variable v = new Variable("x",5.0).set("y",6.0);
		System.out.println(v.get("x"));
		System.out.println(v.get("y"));
		v.setIndex(20);
		System.out.println(v.getIndex());
		Element e = new Element();
		e.globalIndex = 100;
		v.setElement(e);
		System.out.println(v.getElement());
	}

}

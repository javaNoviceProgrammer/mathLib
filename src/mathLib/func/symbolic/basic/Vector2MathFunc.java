package mathLib.func.symbolic.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uta.futureye.util.Tools;
import mathLib.fem.assembler.AssembleParam;
import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.Utils;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.intf.Vector;

/**
 * <blockquote><pre>
 * Vector to Function
 * Evaluate function values based on vector indices in Variable v
 *
 * 2011/6/27
 * + Function _d(String varName)
 *
 * 2011/?/?
 * + extends evaluation ability on the inner area of an element by interpolation
 *
 * 2011/10/17
 * + defaultFunction
 * </blockquote></pre>
 *
 *
 */
public class Vector2MathFunc extends MultiVarFunc {
	Vector vec = null;
	Mesh mesh = null;

	MathFunc defaultFunction = null;

	boolean enableCache = false;
	Map<Integer, Double> cachedValueMap = null;

	public Vector2MathFunc(Vector u) {
		this.vec = u;
	}

	public Vector2MathFunc(Vector u, Mesh mesh,
			String varName, String ...aryVarNames) {
		this.vec = u;
		this.mesh = mesh;
		if(vec.getDim() != mesh.getNodeList().size())
			throw new FutureyeException("u.getDim() != mesh.getNodeList().size()");
		varNames = new String[1+aryVarNames.length];
		varNames[0] = varName;
		for(int i=0; i<aryVarNames.length; i++)
			varNames[i+1] = aryVarNames[i];
	}

	public Vector2MathFunc(Vector u, Mesh mesh,
			String []aryVarNames) {
		this.vec = u;
		this.mesh = mesh;
		if(vec.getDim() != mesh.getNodeList().size())
			throw new FutureyeException("u.getDim() != mesh.getNodeList().size()");
		varNames = aryVarNames;
	}

	public Vector2MathFunc(Vector u, Mesh mesh,
			List<String> varNames) {
		this.vec = u;
		this.mesh = mesh;
		this.varNames = varNames.toArray(new String[0]);
	}

	public Vector2MathFunc setDefaultFunction(MathFunc fun) {
		this.defaultFunction = fun;
		return this;
	}
	public MathFunc getDefaultFunction() {
		return this.defaultFunction;
	}

	public void cacheInterpolateValue(boolean b) {
		this.enableCache = b;
		if(b && cachedValueMap == null)
			cachedValueMap = new HashMap<Integer, Double>();
		else if(!b)
			cachedValueMap = null;
	}

	public void clearInterpolateValueCache() {
		if(cachedValueMap != null)
			cachedValueMap.clear();
	}
	public Mesh getMesh() {
		return this.mesh;
	}
	public Vector getVector() {
		return this.vec;
	}

	@Override
	public double apply(Variable v) {
		int index = v.getIndex();
		int nDim = vec.getDim();
		if(mesh == null) {
			if(index > 0 && index <= nDim)
				return vec.get(index);
			else if(index == 0) {
				throw new FutureyeException("Error: index(=0), please specify index of Variable!");
			} else
				throw new FutureyeException(String.format("Error: index(=%d) should between [1,%d]\n"+
						"try to use constructor Vector2Function(Vector u, Mesh mesh, String varName, String ...aryVarNames)\n"+
						"to extends evaluation ability on the inner area of an element by interpolation.",
						index,nDim));
		} else {
			boolean needInterpolation = false;
			double[] coord = new double[varNames.length];
			for(int i=0;i<varNames.length;i++) {
				coord[i] = v.get(varNames[i]);
			}

			if(index < 0)
				throw new FutureyeException(String.format("Error: index(=%d) <0!",index));
			else if(index==0 || index > nDim)
				needInterpolation = true;
			else {

				Node node = mesh.getNodeList().at(index);
				Node node2 = new Node().set(0,coord);

				if( ! node.coordEquals(node2) )
					needInterpolation = true;
			}

			if(needInterpolation) {
				if(enableCache && index > 0) {
					Double cacheValue = cachedValueMap.get(index);
					if(cacheValue != null) return cacheValue;
				}

				Element e = mesh.getElementByCoord(coord);
				if(e == null) {
					if(this.defaultFunction == null)
						throw new FutureyeException(
								"Can't interplate "+v.toString()+" on the current mesh! \n"+
								"Try to use setDefaultFunction(Function fun) to extend the evaluation outside of the mesh domain.");
					else
						return this.defaultFunction.apply(v);
				}
				double[] f = new double[e.nodes.size()];
				for(int i=1;i<=e.nodes.size();i++) {
					f[i-1] = vec.get(e.nodes.at(i).globalIndex);
				}

				if(e.vertices().size() == 4 && coord.length==2) {
					double[] coef = Utils.computeBilinearFunctionCoef(e.nodes.toArray(new Point[0]), f);
					//f(x,y) = a1 + a2*x + a3*y + a4*x*y
					double x = coord[0];
					double y = coord[1];
					double interpValue = coef[0] + coef[1]*x + coef[2]*y + coef[3]*x*y;
					if(enableCache && index > 0) {
						cachedValueMap.put(index, interpValue);
					}
					return interpValue;
				}
				throw new FutureyeException("Error: Unsported element type:"+e.toString());
			} else {
				return vec.get(index);
			}
		}
	}


	@Override
	public double apply(AssembleParam ap, double... args) {

		Element e = ap.element;
		int i1 = e.nodes.at(1).globalIndex;
		int i2 = e.nodes.at(2).globalIndex;
		int i3 = e.nodes.at(3).globalIndex;
		double v1 = this.vec.get(i1);
		double v2 = this.vec.get(i2);
		double v3 = this.vec.get(i3);
		int startIdx = 12;
		return v1*args[startIdx] + v2*args[startIdx+1] + v3*args[startIdx+2];

	}

	@Override
	public double apply(double... args) {
		return apply(null, args);
	}


	@Override
	public MathFunc diff(String varName) {
		if(mesh == null)
			throw new FutureyeException(
					"Please use constructor Vector2Function(Vector u, Mesh mesh, String varName, String ...aryVarNames)");
		Vector vd = Tools.computeDerivative(mesh, vec, varName);
		MathFunc fd = new Vector2MathFunc(vd,mesh,this.varNames);
		return fd;
	}

	@Override
	public String toString() {
		return "Vector2Function:"+this.vec.toString();
	}

}

package mathLib.fem.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import mathLib.fem.core.geometry.topology.TetrahedronTp;
import mathLib.fem.shapefun.SFBilinearLocal2D;
import mathLib.fem.shapefun.SFLinearLocal1D;
import mathLib.fem.shapefun.SFLinearLocal2D;
import mathLib.fem.shapefun.SFLinearLocal3D;
import mathLib.fem.shapefun.SFTrilinearLocal3D;
import mathLib.fem.util.BytecodeUtils;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.Utils;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.VariableArray;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;

/**
Example:
	Element e = ...;
	CoordinateTransform trans = new CoordinateTransform(2);
	trans.transformLinear2D(e); //2D linear transform
	trans.computeJacobianMatrix();
	trans.computeJacobian2D();
	MathFunc jac = trans.getJacobian();

 */
public class CoordinateTransform {
	
	protected List<String> fromVarNames = null;
	protected List<String> toVarNames = null;

	//coordinate transform functions
	//e.g. x=x(r,s,t); y=y(r,s,t); z=z(r,s,t)
	protected List<MathFunc> transFuns = null;

	protected MathFunc[] JacobianMatrix = null;
	//Jacobian determinant: the determinant of the Jacobian matrix
	protected MathFunc Jacobian = null;

	//--------------------------------------
	private ScalarShapeFunction sf1d1 = null;
	private ScalarShapeFunction sf1d2 = null;

	private ScalarShapeFunction sf2d1 = null;
	private ScalarShapeFunction sf2d2 = null;
	private ScalarShapeFunction sf2d3 = null;

	private ScalarShapeFunction sfb2d1 = null;
	private ScalarShapeFunction sfb2d2 = null;
	private ScalarShapeFunction sfb2d3 = null;
	private ScalarShapeFunction sfb2d4 = null;

	@SuppressWarnings("unused")
	private ScalarShapeFunction[] sf3dHex = null;
	//-------------------------------------------

	/**
	 * 
	 * e.g.
	 *  2D (x,y)   -> (r,s)
	 *  3D (x,y,z) -> (r,s,t)
	 * @param fromVarName
	 * @param toVarName
	 */
	public CoordinateTransform(List<String> fromVarNames, List<String> toVarNames) {
		if(fromVarNames.size() != toVarNames.size()) {
			FutureyeException e = new FutureyeException("ERROR: Number of variables mismatch.");
			e.printStackTrace();
			return;
		}
		this.fromVarNames = fromVarNames;
		this.toVarNames = toVarNames;
	}

	public CoordinateTransform(String[] fromVarNames, String[] toVarNames) {
		List<String> f = new LinkedList<String>();
		for(String s : fromVarNames)
			f.add(s);
		List<String> t = new LinkedList<String>();
		for(String s : toVarNames)
			t.add(s);

		this.fromVarNames = f;
		this.toVarNames = t;
	}

	public CoordinateTransform(int dim) {
		fromVarNames = new LinkedList<String>();
		toVarNames = new LinkedList<String>();
		String[] f = {"x","y","z"};
		String[] t = {"r","s","t"};
		for(int i=0;i<dim;i++) {
			fromVarNames.add(f[i]);
			toVarNames.add(t[i]);
		}
	}

	public CoordinateTransform(int fromDim, int toDim) {
		fromVarNames = new LinkedList<String>();
		toVarNames = new LinkedList<String>();
		String[] f = {"x","y","z"};
		String[] t = {"r","s","t"};
		for(int i=0;i<fromDim;i++) {
			fromVarNames.add(f[i]);
		}
		for(int i=0;i<toDim;i++) {
			toVarNames.add(t[i]);
		}
	}

	/**
	 * 
	 * @param be
	 * @return
	 */
	public Map<Vertex,ScalarShapeFunction> getTransformLinear1DShapeFunction(Element be) {
		Map<Vertex, ScalarShapeFunction> mapVS = new LinkedHashMap<Vertex, ScalarShapeFunction>();
		VertexList vl = be.vertices();
		if(vl.size() == 2) {
			if(sf1d1 == null) sf1d1 = new SFLinearLocal1D(1);
			if(sf1d2 == null) sf1d2 = new SFLinearLocal1D(2);
//			sf1d1.asignElement(be);
//			sf1d2.asignElement(be);
			mapVS.put(vl.at(1), sf1d1);
			mapVS.put(vl.at(2), sf1d2);
		}
		else {
			throw new FutureyeException(
					"ERROR: transformLinear1D(), vl.size() != 2");
		}
		return mapVS;
	}

	/**
	 * 
	 * @param Element e
	 * @return 
	 */
	public Map<Vertex,ScalarShapeFunction> getTransformLinear2DShapeFunction(Element e) {
		VertexList vl = e.vertices();
		Map<Vertex, ScalarShapeFunction> mapVS = new LinkedHashMap<Vertex, ScalarShapeFunction>();
		if(vl.size() == 3) {
			if(sf2d1 == null) sf2d1 = new SFLinearLocal2D(1);
			if(sf2d2 == null) sf2d2 = new SFLinearLocal2D(2);
			if(sf2d3 == null) sf2d3 = new SFLinearLocal2D(3);

			mapVS.put(vl.at(1), sf2d1);
			mapVS.put(vl.at(2), sf2d2);
			mapVS.put(vl.at(3), sf2d3);
		} else if(vl.size() == 4) {
			if(sfb2d1 == null) sfb2d1 = new SFBilinearLocal2D(1);
			if(sfb2d2 == null) sfb2d2 = new SFBilinearLocal2D(2);
			if(sfb2d3 == null) sfb2d3 = new SFBilinearLocal2D(3);
			if(sfb2d4 == null) sfb2d4 = new SFBilinearLocal2D(4);

			mapVS.put(vl.at(1), sfb2d1);
			mapVS.put(vl.at(2), sfb2d2);
			mapVS.put(vl.at(3), sfb2d3);
			mapVS.put(vl.at(4), sfb2d4);
		} else if(vl.size() == 5) {

			throw new FutureyeException(
					"ERROR: getTransformLinear2DShapeFunction() vl.size() == "+vl.size());
		} else {
			throw new FutureyeException(
					"ERROR: getTransformLinear2DShapeFunction() vl.size() == "+vl.size());
		}
		return mapVS;
	}

	public Map<Vertex,ScalarShapeFunction> getTransformLinear3DShapeFunction(Element e) {
		VertexList vl = e.vertices();
		Map<Vertex, ScalarShapeFunction> mapVS = new LinkedHashMap<Vertex, ScalarShapeFunction>();
		if(vl.size() == 4) {
			for(int i=1;i<=4;i++)
				mapVS.put(vl.at(i), new SFLinearLocal3D(i));
		} else if(vl.size() == 8) {
			for(int i=1;i<=8;i++)
				mapVS.put(vl.at(i), new SFTrilinearLocal3D(i));
		} else {
			return getTransformShapeFunctionByElement(e);
		}
		return mapVS;
	}

	public Map<Vertex,ScalarShapeFunction> getTransformShapeFunctionByElement(Element e) {
		Map<Vertex, ScalarShapeFunction> mapVS = new LinkedHashMap<Vertex, ScalarShapeFunction>();
		VertexList vertices = e.vertices();
		for(int i=1;i<=vertices.size();i++) {
			mapVS.put(
					vertices.at(i),
					e.getNodeDOFList(i).at(1).getSSF()
					);
		}
		return mapVS;
	}

	/**
	 * 
	 * 2D: (x,y)=(x(r,s), y(r,s))
	 * 3D: (x,y,z)=(x(r,s,t), y(r,s,t), z(r,s,t))
	 *
	 * 
	 * 
	 * x = x(r,s,t) = x1*sf1 + x2*sf2 + x3*sf3 = x1*r + x2*s + x3*t
	 * y = y(r,s,t) = y1*sf1 + y2*sf2 + y3*sf3 = y1*r + y2*s + y3*t
	 * where r,s are free variables and t=1-r-s
	 * 
	 *
	 * 
	 * 
	 * x = x(r,s,t,u) = x1*sf1 + x2*sf2 + x3*sf3 * x4*sf4 = x1*r + x2*s + x3*t * x4*u
	 * y = y(r,s,t,u) = y1*sf1 + y2*sf2 + y3*sf3 * y4*sf4 = y1*r + y2*s + y3*t * y4*u
	 * z = z(r,s,t,u) = z1*sf1 + z2*sf2 + z3*sf3 * z4*sf4 = z1*r + z2*s + z3*t * z4*u
	 * where r,s,t are free variables and u=1-r-s-t
	 * 
	 *
	 * 
	 * x = x(r,s,t) = sum_{i=1}^{8}{xi*sfi}
	 * y = y(r,s,t) = sum_{i=1}^{8}{yi*sfi}
	 * z = z(r,s,t) = sum_{i=1}^{8}{zi*sfi}
	 *
	 * @param mapVS
	 * @return 2D:[x,y]; 3D:[x,y,z]
	 */
	public List<MathFunc> getTransformFunction(Map<Vertex,ScalarShapeFunction> mapVS) {
		List<MathFunc> r = new LinkedList<MathFunc>();
		int dim = fromVarNames.size();

		ScalarShapeFunction[] shapeFuns = new ScalarShapeFunction[mapVS.size()];
		double[] coords = new double[mapVS.size()];

		for(int i=1;i<=dim;i++) {
			int j=0;
			for(Entry<Vertex,ScalarShapeFunction> e : mapVS.entrySet()) {
				Vertex v = e.getKey();
				coords[j] = v.coord(i);
				shapeFuns[j] = e.getValue();
				j++;
			}
			r.add(FMath.linearCombination(coords, shapeFuns));
		}
		return r;
	}

	public void setTransformFunction(List<MathFunc> trans) {
		this.transFuns = trans;
	}

	public void transformLinear1D(Element be) {
		setTransformFunction(
				getTransformFunction(
					getTransformLinear1DShapeFunction(be)
					)
				);
	}

	public void transformLinear2D(Element e) {
		setTransformFunction(
				getTransformFunction(
					getTransformLinear2DShapeFunction(e)
					)
				);
	}
	public void transformLinear3D(Element e) {
		setTransformFunction(
				getTransformFunction(
					getTransformLinear3DShapeFunction(e)
					)
				);
	}


	/**
	 * Compute Jacobian matrix, call getJacobianMatrix() to access it.
	 *
	 */
	public void computeJacobianMatrix() {
		
		if(this.JacobianMatrix == null) {
			if(transFuns == null)
				throw new FutureyeException("Call setTransformFunction() first!");
			MathFunc[] funs = new MathFunc[transFuns.size()*toVarNames.size()];
			int index = 0;
			for(MathFunc transFun : transFuns) { //x=x(r,s,t), y=y(r,s,t), z=z(r,s,t)
				for(String var : toVarNames) {   //r,s,t
					funs[index++] = transFun.diff(var);
				}
			}
			this.JacobianMatrix = funs;
		}
	}


	/**
	 * Return Jacobian matrix, call computeJacobianMatrix() first.
	 *
	 * 1D JacMat = (r[0]) = (x_r)
	 *
	 *             (r[0] r[1])   (x_r, x_s)
	 * 2D JacMat = (r[2] r[3]) = (y_r, y_s)
	 *
	 *             (r[0] r[1] r[2])   (x_r, x_s, x_t)
	 * 3D JacMat = (r[3] r[4] r[5]) = (y_r, y_s, y_t)
	 *             (r[6] r[7] r[8])   (z_r, z_s, z_t)
	 *
	 * @return Function[] r
	 */
	public MathFunc[] getJacobianMatrix() {
		return this.JacobianMatrix;
	}

	/**
	 * @return det(Jac)
	 */
	public MathFunc getJacobian() {
		return this.Jacobian;
	}

	/**
	 *  Compute 1D determinant of Jacobian matrix
	 *  1D: det(Jac) = x_r
	 *  2D boundary: det(Jac)= sqrt(x_r^2 + y_r^2)
	 *
	 */
	public void computeJacobian1D() {
		MathFunc[] funs = this.JacobianMatrix;
		this.Jacobian = FMath.sqrt(funs[0].M(funs[0]) .A (funs[1].M(funs[1])));
	}

	/**
	 *  Compute 2D determinant of Jacobian matrix
	 *
	 *             |x_r x_s|
	 *  det(Jac) = |y_r y_s|
	 *
	 */
	public void computeJacobian2D() {
		/*
		 * 
		 * x = x(r,s,t) = x1*sf1 + x2*sf2 + x3*sf3 = x1*r + x2*s + x3*t
		 * y = y(r,s,t) = y1*sf1 + y2*sf2 + y3*sf3 = y1*r + y2*s + y3*t
		 * where r,s are free variables and t=1-r-s
		 */

		MathFunc[] funs = this.JacobianMatrix;
		if(funs.length == 4) {
			/*
			 * 
			 *            |f0 f1|
			 *  det(Jac)= |f2 f3| = f0*f3-f1*f2
			 */
			//this.Jacobian = funs[0].M(funs[3]) .S (funs[1].M(funs[2]));
			this.Jacobian = new Jacobian2D(funs);
		} else if(funs.length == 6) {
			/*
			 * 
			 *
			 *       |f0 f1 f2|
			 * Jac = |f3 f4 f5|
			 *
			 * det(Jac) = sqrt( (f1*f5-f4*f2)^2 + (f0*f5-f3*f2)^2 + (f0*f4-f3*f1)^2 )
			 *
			 */
			this.Jacobian = new Jacobian2DFrom3D(funs);
		}
	}

	public static class Jacobian2D extends MultiVarFunc {
		
		MathFunc[] funs = null;
		
		public Jacobian2D(MathFunc[] funs) {
			this.funs = funs;
			List<String> list = new ArrayList<String>();
			for(MathFunc fun : funs) {
				list = Utils.mergeList(list, fun.getVarNames());
			}
			this.setVarNames(list);
			this.setArgIdx(Utils.getIndexMap(this.getVarNames()));
		}

		@Override
		public double apply(Variable v) {
			return apply(v,null);
		}


		@Override
		public double apply(Variable v, Map<Object,Object> cache) {
			Double detJ = null;
			if(cache != null) {
				detJ = (Double)cache.get(1);
			}
			if(detJ == null) {
				double J11 = funs[0].apply(v);
				double J12 = funs[1].apply(v);
				double J21 = funs[2].apply(v);
				double J22 = funs[3].apply(v);
				detJ = J11*J22-J12*J21;
				if(cache != null) {
					cache.put(1, detJ);
				}
			}
			return detJ;
		}

		@Override
		public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
			double[] detJ = null;
			int len = v.length();
			if(cache != null) {
				detJ = (double[])cache.get(1);
			}
			if(detJ == null) {
				double[][][] J = new double[2][2][len];
				J[0][0] = funs[0].applyAll(v,cache);
				J[0][1] = funs[1].applyAll(v,cache);
				J[1][0] = funs[2].applyAll(v,cache);
				J[1][1] = funs[3].applyAll(v,cache);
				
				detJ = Utils.determinant(J);
				if(cache != null) {
					cache.put(1, detJ);
					cache.put(2, J);
				}
			}
			return detJ;
		}

		public String getExpr() {
			return "Jacobian2D";
		}

		public String toString() {
			return "Jacobian2D";
		}

		@Override
		public double apply(double... args) {
			double J11 = funs[0].apply(args);
			double J12 = funs[1].apply(args);
			double J21 = funs[2].apply(args);
			double J22 = funs[3].apply(args);
			return J11*J22-J12*J21;
		}

		@Override
		public InstructionHandle bytecodeGen(String clsName, MethodGen mg,
				ConstantPoolGen cp, InstructionFactory factory,
				InstructionList il, Map<String, Integer> argsMap, int argsStartPos,
				Map<MathFunc, Integer> funcRefsMap) {
			MathFunc ret = funs[0]*funs[3]-funs[1]*funs[2];
			Map<MathFunc, Integer> refsMap2 = BytecodeUtils.getFuncRefsMap(ret);
			funcRefsMap.putAll(refsMap2);
			ret.setArgIdx(this.getArgIdxMap());
			return ret.bytecodeGen(clsName, mg, cp, factory, il, argsMap, argsStartPos, funcRefsMap);
		}

		@Override
		public MathFunc setArgIdx(Map<String, Integer> argsMap) {
			super.setArgIdx(argsMap);
			return this;
		}
	}

	public static class Jacobian2DFrom3D extends MultiVarFunc {
		MathFunc[] funs = null;
		public Jacobian2DFrom3D(MathFunc[] funs) {
			this.funs = funs;
			List<String> list = new ArrayList<String>();
			for(MathFunc fun : funs) {
				list = Utils.mergeList(list, fun.getVarNames());
			}
			this.setVarNames(list);
		}

		@Override
		public double apply(Variable v) {
			return apply(v,null);
		}


		@Override
		public double apply(Variable v, Map<Object,Object> cache) {
			Double detJ = null;
			if(cache != null) {
				detJ = (Double)cache.get(1);
			}
			if(detJ == null) {
				double f0 = funs[0].apply(v);
				double f1 = funs[1].apply(v);
				double f2 = funs[2].apply(v);
				double f3 = funs[3].apply(v);
				double f4 = funs[4].apply(v);
				double f5 = funs[5].apply(v);
				detJ = Math.sqrt( (f1*f5-f4*f2)*(f1*f5-f4*f2) + (f0*f5-f3*f2)*(f0*f5-f3*f2) + (f0*f4-f3*f1)*(f0*f4-f3*f1) );
				if(cache != null) {
					cache.put(1, detJ);
				}
			}
			return detJ;
		}

		@Override
		public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
			double[] detJ = null;
			int len = v.length();
			if(cache != null) {
				detJ = (double[])cache.get(1);
			}
			if(detJ == null) {
				double[][][] J = new double[2][3][len];
				J[0][0] = funs[0].applyAll(v,cache);
				J[0][1] = funs[1].applyAll(v,cache);
				J[0][2] = funs[2].applyAll(v,cache);
				J[1][0] = funs[3].applyAll(v,cache);
				J[1][1] = funs[4].applyAll(v,cache);
				J[1][2] = funs[5].applyAll(v,cache);
				
				detJ = Utils.determinant23(J);
				if(cache != null) {
					cache.put(1, detJ);
					cache.put(2, J);
				}
			}
			return detJ;
		}

		public String getExpr() {
			return "Jacobian2DFrom3D";
		}

		public String toString() {
			return "Jacobian2DFrom3D";
		}

		@Override
		public double apply(double... args) {
			double f0 = funs[0].apply(args);
			double f1 = funs[1].apply(args);
			double f2 = funs[2].apply(args);
			double f3 = funs[3].apply(args);
			double f4 = funs[4].apply(args);
			double f5 = funs[5].apply(args);
			return Math.sqrt( (f1*f5-f4*f2)*(f1*f5-f4*f2) + (f0*f5-f3*f2)*(f0*f5-f3*f2) + (f0*f4-f3*f1)*(f0*f4-f3*f1) );
		}
	}

	/**
	 *  Compute 3D determinant of Jacobian matrix
	 *
	 *             |x_r x_s x_t|
	 *  det(Jac) = |y_r y_s y_t|
	 *             |z_r z_s z_t|
	 *
	 */
	public void computeJacobian3D() {
		/*
		 * 
		 * x = x(r,s,t,u) = x1*sf1 + x2*sf2 + x3*sf3 * x4*sf4 = x1*r + x2*s + x3*t * x4*u
		 * y = y(r,s,t,u) = y1*sf1 + y2*sf2 + y3*sf3 * y4*sf4 = y1*r + y2*s + y3*t * y4*u
		 * z = z(r,s,t,u) = z1*sf1 + z2*sf2 + z3*sf3 * z4*sf4 = z1*r + z2*s + z3*t * z4*u
		 * where r,s,t are free variable, u=1-r-s-t
		 *
		 * 
		 * x = x(r,s,t) = sum_{i=1}^{8}{xi*sfi}
		 * y = y(r,s,t) = sum_{i=1}^{8}{yi*sfi}
		 * z = z(r,s,t) = sum_{i=1}^{8}{zi*sfi}
		 */

		MathFunc[] funs = this.JacobianMatrix;

		this.Jacobian = new Jacobian3D(funs);
	}

	public static class Jacobian3D extends MultiVarFunc {
		MathFunc[] funs = null;
		public Jacobian3D(MathFunc[] funs) {
			this.funs = funs;
			List<String> list = new ArrayList<String>();
			for(MathFunc fun : funs) {
				list = Utils.mergeList(list, fun.getVarNames());
			}
			this.setVarNames(list);
		}
		@Override
		public double apply(Variable v) {
			return apply(v,null);
		}


		@Override
		public double apply(Variable v, Map<Object,Object> cache) {
			Double detJ = null;
			if(cache != null) {
				detJ = (Double)cache.get(1);
			}
			if(detJ == null) {
				double[][] J = new double[3][3];
				J[0][0] = funs[0].apply(v);
				J[0][1] = funs[1].apply(v);
				J[0][2] = funs[2].apply(v);
				J[1][0] = funs[3].apply(v);
				J[1][1] = funs[4].apply(v);
				J[1][2] = funs[5].apply(v);
				J[2][0] = funs[6].apply(v);
				J[2][1] = funs[7].apply(v);
				J[2][2] = funs[8].apply(v);
				
				detJ = Utils.determinant(J);
				if(cache != null) {
					cache.put(1, detJ);
					cache.put(2, J);
				}
			}
			return detJ;
		}

		@Override
		public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
			double[] detJ = null;
			int len = v.length();
			if(cache != null) {
				detJ = (double[])cache.get(1);
			}
			if(detJ == null) {
				double[][][] J = new double[3][3][len];
				J[0][0] = funs[0].applyAll(v,cache);
				J[0][1] = funs[1].applyAll(v,cache);
				J[0][2] = funs[2].applyAll(v,cache);
				J[1][0] = funs[3].applyAll(v,cache);
				J[1][1] = funs[4].applyAll(v,cache);
				J[1][2] = funs[5].applyAll(v,cache);
				J[2][0] = funs[6].applyAll(v,cache);
				J[2][1] = funs[7].applyAll(v,cache);
				J[2][2] = funs[8].applyAll(v,cache);
				
				detJ = Utils.determinant(J);
				if(cache != null) {
					cache.put(1, detJ);
					cache.put(2, J);
				}
			}
			return detJ;
		}

		public String getExpr() {
			return "Jacobian3D";
		}

		public String toString() {
			return "Jacobian3D";
		}

		@Override
		public double apply(double... args) {
			double[][] J = new double[3][3];
			J[0][0] = funs[0].apply(args);
			J[0][1] = funs[1].apply(args);
			J[0][2] = funs[2].apply(args);
			J[1][0] = funs[3].apply(args);
			J[1][1] = funs[4].apply(args);
			J[1][2] = funs[5].apply(args);
			J[2][0] = funs[6].apply(args);
			J[2][1] = funs[7].apply(args);
			J[2][2] = funs[8].apply(args);
			//@see ./doc_ex/invA33.png
			return Utils.determinant(J);
		}
	}

	/**
	 * Compute det(Jac) for Tetrahedron element only, it is optimized for faster speed
	 *
	 * @param e
	 * @return det(Jac)
	 */
	public MathFunc computeJacobian3DTetrahedron(Element e) {
		double x1,x2,x3,x4;
		double y1,y2,y3,y4;
		double z1,z2,z3,z4;
		if(!(e.getGeoEntity3D().getTopology() instanceof TetrahedronTp))
			throw new FutureyeException(
					"Function computeJacobian3DTetrahedron() is used only for Tetrahedron element!");

		x1 = e.nodes.at(1).coord(1);
		x2 = e.nodes.at(2).coord(1);
		x3 = e.nodes.at(3).coord(1);
		x4 = e.nodes.at(4).coord(1);
		y1 = e.nodes.at(1).coord(2);
		y2 = e.nodes.at(2).coord(2);
		y3 = e.nodes.at(3).coord(2);
		y4 = e.nodes.at(4).coord(2);
		z1 = e.nodes.at(1).coord(3);
		z2 = e.nodes.at(2).coord(3);
		z3 = e.nodes.at(3).coord(3);
		z4 = e.nodes.at(4).coord(3);
		double[] items = new double[9];
		/*
		 * x = x(r,s,t,u) =  x1*r + x2*s + x3*t * x4*u
		 * y = y(r,s,t,u) =  y1*r + y2*s + y3*t * y4*u
		 * z = z(r,s,t,u) =  z1*r + z2*s + z3*t * z4*u
		 * where r,s,t are free variables and u=1-r-s-t
		 */
		items[0] = x1 - x4;
		items[1] = x2 - x4;
		items[2] = x3 - x4;
		items[3] = y1 - y4;
		items[4] = y2 - y4;
		items[5] = y3 - y4;
		items[6] = z1 - z4;
		items[7] = z2 - z4;
		items[8] = z3 - z4;

		/*
		 * 
		 *            |i0 i1 i2|
		 *  det(Jac)= |i3 i4 i5|
		 *            |i6 i7 i8|
		 */
		double det4875 = items[4]*items[8]-items[7]*items[5];
		double det6538 = items[6]*items[5]-items[3]*items[8];
		double det3764 = items[3]*items[7]-items[6]*items[4];

		//9/29/2012 bufix: assign Jacobian
		this.Jacobian = new FC(
				  items[0]*det4875
				+ items[1]*det6538
				+ items[2]*det3764);
		return this.Jacobian;
	}

	public List<String> getFromVarNames() {
		return this.fromVarNames;
	}

	public List<String> getToVarNames() {
		return this.toVarNames;
	}

}

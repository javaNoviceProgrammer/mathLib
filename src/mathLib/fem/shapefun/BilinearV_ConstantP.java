package mathLib.fem.shapefun;

import mathLib.fem.core.Element;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.ObjList;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.VecMathFuncBase;
import mathLib.func.symbolic.basic.SpaceVectorFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;
import mathLib.func.symbolic.intf.ShapeFunction;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.func.symbolic.intf.VectorShapeFunction;
import mathLib.matrix.algebra.intf.Vector;

/**
 * 2D Q1/P0 Element
 * -Continuous bilinear velocity
 * -Piecewise constant pressure
 * 
 * * Velocity: Bilinear shape function: SFBilinearLocal2D
 * * 速度：四边形局部�??标，�?�线性函数
 * 
 * 4----3
 * |    |
 * |    |
 * 1----2
 * 
 * NV = NV(r,s) = NV( r(x,y), s(x,y) )
 * NV1 = (1-r)*(1-s)/4
 * NV2 = (1+r)*(1-s)/4
 * NV3 = (1+r)*(1+s)/4
 * NV4 = (1-r)*(1+s)/4
 * 
 * * Pressure: Piecewise constant shape function: SFConstant1
 * * 压强：分片常数型函数
 * NP=1
 * 
 * * 2D vector valued shape functions
 * * 二维�?�元上的形函数，速度压强共9个自由度：
 * Ni = (u1,u2,p)', i=1,...,9
 * 
 * N1  =  (NV1, 0, 0)'
 * N2  =  (NV2, 0, 0)'
 * N3  =  (NV3, 0, 0)'
 * N4  =  (NV4, 0, 0)'
 * N5  =  (0, NV1, 0)'
 * N6  =  (0, NV2, 0)'
 * N7  =  (0, NV3, 0)'
 * N8 =   (0, NV4, 0)'
 * N9 =   (0, 0, NP)'
 *
 * @author liuyueming
 */
public class BilinearV_ConstantP extends VecMathFuncBase 
								implements VectorShapeFunction {
	//(u1,u2,p)
	protected SpaceVectorFunction sf = null;
	protected int funIndex;
	protected ObjList<String> innerVarNames = null;
	
	/**
	 * 
	 * @param funID
	 */
	public BilinearV_ConstantP(int funID) {
		funIndex = funID - 1;
		dim = 3;
		sf = new SpaceVectorFunction(dim);
		
		varNames.add("r");
		varNames.add("s");
		innerVarNames = new ObjList<String>("x","y");

		if(funIndex>=0 && funIndex<=3) {
			sf.set(1, new SFBilinearLocal2D(funIndex+1));
			sf.set(2, new SFConstant0(innerVarNames));
			sf.set(3, new SFConstant0(innerVarNames));
		} else if(funIndex>=4 && funIndex<=7) {
			sf.set(1, new SFConstant0(innerVarNames));
			sf.set(2, new SFBilinearLocal2D(funIndex-3));
			sf.set(3, new SFConstant0(innerVarNames));
		} else if(funIndex==8) {
			sf.set(1, new SFConstant0(innerVarNames));
			sf.set(2, new SFConstant0(innerVarNames));
			sf.set(3, new SFConstant1(innerVarNames));
		} else {
			throw new FutureyeException("ERROR: funID should be 1,2,3...,9.");
		}
	}
	
	@Override
	public void assignElement(Element e) {
		if(funIndex>=0 && funIndex<=3) {
			((ScalarShapeFunction)sf.get(1)).assignElement(e);
		} else if(funIndex>=4 && funIndex<=7) {
			((ScalarShapeFunction)sf.get(2)).assignElement(e);
		} else if(funIndex==8) {
			((ScalarShapeFunction)sf.get(3)).assignElement(e);
		}
	}

	@Override
	public ObjList<String> innerVarNames() {
		return innerVarNames;
	}

	LinearV_ConstantP1D []sf1D = null;
	/**
	 * Restrict to boundary edge:
	 * * Velocity:
	 * |    |
	 * |    |
	 * 1----2
	 * 
	 * NV1 = (1-r)/2
	 * NV2 = (1+r)/2
	 * 
	 * * Pressure:
	 * NP=1
	 * 
	 * Ni = (u1,u2,p)', i=1,...,5, on boundary
	 * 
	 * N1 =  (NV1,0,0)'
	 * N2 =  (NV2,0,0)'
	 * N3 =  (0,NV1,0)'
	 * N4 =  (0,NV2,0)'
	 * N5 =  (0,0,NP)'
	 * 
	 * @param funIndex
	 * @return
	 */
	@Override
	public ShapeFunction restrictTo(int funIndex) {
		if(sf1D == null) {
			sf1D = new LinearV_ConstantP1D[5];
			for(int i=1;i<=5;i++)
				sf1D[i-1] = new LinearV_ConstantP1D(i);
		}
		return sf1D[funIndex-1];
	}
	
	/**
	 * 需�?定义边界�?�元，因为二维情况，�?��?值形函数dim=3，如果直接使用一维的，dim=2
	 * 
	 * @author liuyueming
	 *
	 */
	public class LinearV_ConstantP1D extends VecMathFuncBase 
						implements VectorShapeFunction {
		//(u1,u2,p)
		protected SpaceVectorFunction sf = new SpaceVectorFunction(3);
		protected int funIndex;
		protected ObjList<String> innerVarNames = null;
		
		public LinearV_ConstantP1D(int funID) {
			dim = 3;
			
			this.funIndex = funID - 1;
			varNames.add("r");
			innerVarNames = new ObjList<String>("x");
			
			if(funIndex>=0 && funIndex<=1) {
				sf.set(1, new SFLinearLocal1D(funIndex+1));
				sf.set(2, new SFConstant0(innerVarNames));
				sf.set(3, new SFConstant0(innerVarNames));
			} else if(funIndex>=2 && funIndex<=3) {
				sf.set(1, new SFConstant0(innerVarNames));
				sf.set(2, new SFLinearLocal1D(funIndex-1));
				sf.set(3, new SFConstant0(innerVarNames));
			} else if(funIndex==4) {
				sf.set(1, new SFConstant0(innerVarNames));
				sf.set(2, new SFConstant0(innerVarNames));
				sf.set(3, new SFConstant1(innerVarNames));
			} else {
				throw new FutureyeException("ERROR: funID should be 1,2,3...,5.");
			}
			
		}

		@Override
		public void assignElement(Element e) {
			if(funIndex>=0 && funIndex<=1) {
				((ScalarShapeFunction)sf.get(1)).assignElement(e);
			} else if(funIndex>=2 && funIndex<=3) {
				((ScalarShapeFunction)sf.get(2)).assignElement(e);
			} else if(funIndex==4) {
				((ScalarShapeFunction)sf.get(3)).assignElement(e);
			}
		}
		
		@Override
		public ObjList<String> innerVarNames() {
			return innerVarNames;
		}
		
		@Override
		public Vector value(Variable v) {
			return (Vector) this.sf.value(v);
		}

		@Override
		public ShapeFunction restrictTo(int funIndex) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public MathFunc get(int index) {
			return sf.get(index);
		}

		@Override
		public void set(int index, MathFunc value) {
			throw new UnsupportedOperationException();
		}
	};

	@Override
	public MathFunc dot(VecMathFunc b) {
		return sf.dot(b);
	}

	@Override
	public MathFunc dot(Vector b) {
		return sf.dot(b);
	}

	@Override
	public MathFunc get(int index) {
		return sf.get(index);
	}

	@Override
	public void set(int index, MathFunc value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Vector value(Variable v) {
		return (Vector) this.sf.value(v);
	}
	
	public String toString() {
		return sf.toString();
	}
}

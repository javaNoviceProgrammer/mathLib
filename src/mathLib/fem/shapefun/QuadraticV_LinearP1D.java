package mathLib.fem.shapefun;

import mathLib.fem.core.Element;
import mathLib.fem.util.container.ObjList;
import mathLib.func.symbolic.VecMathFuncBase;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ShapeFunction;
import mathLib.func.symbolic.intf.VectorShapeFunction;

//TODO
public class QuadraticV_LinearP1D extends VecMathFuncBase implements
		VectorShapeFunction {

	@Override
	public void assignElement(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public ObjList<String> innerVarNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShapeFunction restrictTo(int funIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MathFunc get(int index) {
		return null;
	}

	@Override
	public void set(int index, MathFunc value) {
		// TODO Auto-generated method stub
		
	}

}

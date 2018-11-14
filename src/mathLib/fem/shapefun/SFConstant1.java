package mathLib.fem.shapefun;

import mathLib.fem.util.container.ObjList;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.ScalarShapeFunction;
import mathLib.func.symbolic.intf.ShapeFunction;

/**
 * Shape function: constant one, as piecewise constant shape function 
 * <p>
 *
 */
public class SFConstant1 extends FC implements ScalarShapeFunction {
	protected ObjList<String> innerVarNames = new ObjList<String>();

	public SFConstant1(String ...varNames) {
		super(1.0);
		for(int i=1;i<varNames.length;i++) {
			this.innerVarNames.add(varNames[i]);
		}
	}
	public SFConstant1(ObjList<String> varNames) {
		super(1.0);
		for(int i=1;i<=varNames.size();i++) {
			this.innerVarNames.add(varNames.at(i));
		}
	}
	
	@Override
	public void assignElement(Element e) {
	}

	@Override
	public ObjList<String> innerVarNames() {
		return innerVarNames;
	}

	@Override
	public ShapeFunction restrictTo(int funIndex) {
		return null;
	}
	
	@Override
	public boolean isConstant() {
		return true;
	}
}

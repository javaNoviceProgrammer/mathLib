package mathLib.func.symbolic.intf;

import mathLib.fem.core.Element;
import mathLib.fem.util.container.ObjList;

public interface ShapeFunction {

	ObjList<String> innerVarNames();

	void assignElement(Element e);

	ShapeFunction restrictTo(int funIndex);
	
}

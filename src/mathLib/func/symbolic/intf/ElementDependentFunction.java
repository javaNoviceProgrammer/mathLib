package mathLib.func.symbolic.intf;

import mathLib.fem.core.Element;

public interface ElementDependentFunction extends MathFunc {
	void setElement(Element e);
}

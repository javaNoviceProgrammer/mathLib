package mathLib.fem.util.container;

import mathLib.fem.core.Element;

/**
 * Element List Class
 *
 */
public class ElementList extends ObjList<Element>{
	@Override
	public String toString() {
		return "ElementList"+objs.toString();
	}
}

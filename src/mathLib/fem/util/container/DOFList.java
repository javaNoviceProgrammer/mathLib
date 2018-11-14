package mathLib.fem.util.container;

import mathLib.fem.core.DOF;

/**
 * Degree Of Freedom Class
 *
 */
public class DOFList extends ObjList<DOF> {
	@Override
	public String toString() {
		return "DOFList"+objs.toString();
	}
}

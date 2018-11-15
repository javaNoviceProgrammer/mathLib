package mathLib.fem.util.container;

import mathLib.fem.core.Edge;

/**
 * Edge List Class
 *
 */
public class EdgeList extends ObjList<Edge> {
	@Override
	public String toString() {
		return "EdgeList"+objs.toString();
	}
}


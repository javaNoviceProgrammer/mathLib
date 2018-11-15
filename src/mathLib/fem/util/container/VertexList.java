package mathLib.fem.util.container;

import mathLib.fem.core.Vertex;

/**
 * Vertex List Class
 *
 */
public class VertexList extends ObjList<Vertex> {
	@Override
	public String toString() {
		return "VertexList"+objs.toString();
	}

}

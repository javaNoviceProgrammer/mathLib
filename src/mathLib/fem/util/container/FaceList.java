package mathLib.fem.util.container;

import mathLib.fem.core.Face;

/**
 * Face List Class
 *
 */
public class FaceList extends ObjList<Face> {
	@Override
	public String toString() {
		return "FaceList"+objs.toString();
	}
}

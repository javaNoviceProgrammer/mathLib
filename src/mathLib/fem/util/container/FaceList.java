package mathLib.fem.util.container;

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

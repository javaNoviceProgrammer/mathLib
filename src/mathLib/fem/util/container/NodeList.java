package mathLib.fem.util.container;

import mathLib.fem.core.Node;

/**
 * <P>Node List Container</P>
 *
 * <B>Notes:</B>
 * <P>Node index starts from 1.</P>
 * <P><tt>null</tt> element is not allowed.</P>
 * <P>Auto size increment is supported.</P>
 */
public class NodeList extends ObjList<Node>{
	@Override
	public String toString() {
		return "NodeList"+objs.toString();
	}
}
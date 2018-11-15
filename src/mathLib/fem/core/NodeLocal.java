package mathLib.fem.core;

import mathLib.fem.core.geometry.Point;

/**
 * * Local finite element node class.
 *
 *
 */
public class NodeLocal implements Point {
	public Element owner = null;
	public Node globalNode;
	public int localIndex;

	public NodeLocal(int localIndex, Node globalNode) {
		this.localIndex = localIndex;
		this.globalNode = globalNode;
	}

	public NodeLocal(int localIndex, Node globalNode, Element owner) {
		this.localIndex = localIndex;
		this.globalNode = globalNode;
		this.owner = owner;
	}

	public Node globalNode() {
		return this.globalNode;
	}

	@Override
	public double coord(int index) {
		return this.globalNode.coord(index);
	}

	@Override
	public boolean coordEquals(Point p) {
		return this.globalNode.coordEquals(p);
	}

	@Override
	public double[] coords() {
		return this.globalNode.coords();
	}

	@Override
	public int dim() {
		return this.globalNode.dim();
	}

	@Override
	public int getIndex() {
		return this.localIndex;
	}

	@Override
	public void setCoord(int index, double value) {
		this.globalNode.setCoord(index, value);
	}

	@Override
	public String toString() {
		return "NodeLocal"+localIndex+"<=>"+globalNode.toString();
	}
}

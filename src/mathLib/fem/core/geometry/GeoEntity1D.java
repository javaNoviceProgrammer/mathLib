package mathLib.fem.core.geometry;

import mathLib.fem.util.container.ObjList;

public class GeoEntity1D<TNode extends Point> extends GeoEntity0D {

	protected ObjList<TNode> edgeNodes = null;
	
	public void addEdgeNode(TNode node) {
		if(this.edgeNodes == null)
			this.edgeNodes = new ObjList<TNode>();
		this.edgeNodes.add(node);
	}
	
	public void addAllEdgeNodes(ObjList<TNode> nodes) {
		if(this.edgeNodes == null)
			this.edgeNodes = new ObjList<TNode>();
		this.edgeNodes.clear();
		this.edgeNodes.addAll(nodes);
	}
	
	public ObjList<TNode> getEdgeNodes() {
		return this.edgeNodes;
	}
	
	public void clearEdgeNodes() {
		if(this.edgeNodes != null)
			this.edgeNodes.clear();
	}
	
	public String toString() {
		return "GeoEntity1D:"+this.vertices.toString();
	}	
}
package mathLib.fem.core.geometry;

import mathLib.fem.core.geometry.topology.Topology2D;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.VertexList;

public class GeoEntity2D<
						TEdge extends GeoEntity1D<TNode>,
						TNode extends Point
						> extends GeoEntity0D {
	protected Topology2D topology = null;
	protected ObjList<TEdge> edges = new ObjList<TEdge>();
	protected ObjList<TNode> faceNodes = null;
	
	public void addEdge(TEdge edge) {
		this.edges.add(edge);
	}
	
	public void addAllEdges(ObjList<TEdge> edges) {
		this.edges.clear();
		this.edges.addAll(edges);
	}
	
	public ObjList<TEdge> getEdges() {
		return this.edges;
	}
	
	public void clearEdges() {
		this.edges.clear();
	}
	
	public boolean containsEdge(TNode n1, TNode n2) {
		for(TEdge edge : edges) {
			VertexList vs = edge.getVertices();
			Vertex v1 = vs.at(1);
			Vertex v2 = vs.at(2);
			if(v1.coordEquals(n1) && v2.coordEquals(n2))
				return true;
		}
		return false;
	}
	
	public void addFaceNode(TNode node) {
		if(this.faceNodes == null)
			this.faceNodes = new ObjList<TNode>();
		this.faceNodes.add(node);
	}
	
	public void addAllFaceNodes(ObjList<TNode> faceNodes) {
		if(this.faceNodes == null)
			this.faceNodes = new ObjList<TNode>();
		this.faceNodes.clear();
		this.faceNodes.addAll(faceNodes);
	}
	
	public ObjList<TNode> getFaceNodes() {
		return this.faceNodes;
	}
	
	public void clearFaceNodes() {
		if(this.faceNodes != null)
			this.faceNodes.clear();
	}
	
	public void clearAll() {
		this.edges.clear();
		if(this.faceNodes != null)
			this.faceNodes.clear();
	}
	
	public Topology2D getTopology() {
		return topology;
	}
	
	public void setTopology(Topology2D topology) {
		this.topology = topology;
	}
	
	public String toString() {
		return "GeoEntity2D:"+this.vertices.toString();
	}
}

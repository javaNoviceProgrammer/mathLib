package mathLib.fem.core;

import mathLib.fem.core.geometry.GeoEntity1D;

/**
 * Global edge of an element
 *
 */
public class Edge extends GeoEntity1D<NodeLocal> {
	protected int globalIndex;
	private Vector globalUnitNormVector; 
	
	public Edge() {
	}
	
	public Edge(NodeLocal begin, NodeLocal end) {
		addVertex(new Vertex(1,begin));
		addVertex(new Vertex(2,end));

		globalUnitNormVector = Utils.getNormVector(
				this.vertices.at(1),
				this.vertices.at(2)
				);
	}
	
	public int getGlobalIndex() {
		return globalIndex;
	}

	public void setGlobalIndex(int globalIndex) {
		this.globalIndex = globalIndex;
	}
	
	public Vector getNormVector() {
		if(this.globalUnitNormVector == null)
			globalUnitNormVector = Utils.getNormVector(
					this.vertices.at(1),
					this.vertices.at(2)
					);

		return this.globalUnitNormVector;
	}
	
    public NodeType getBorderType() {                 
    	return getBorderType(1);
    }
    
	/**
	 * For vector valued problems, return boundary type of component <tt>nVVFComponent</tt>
	 * <p>
	 * 对于�?��?值问题，�?个分�?在�?�一边界上的类型�?一定相�?�，
	 * 该函数返回分�?<tt>nVVFComponent</tt>对应的边界类型
	 * 
	 * @param nVVFComponent
	 * @return
	 */
    public NodeType getBorderType(int nVVFComponent) {                 
    	NodeType nt1 = this.vertices.at(1).globalNode().getNodeType(nVVFComponent);
    	NodeType nt2 = this.vertices.at(2).globalNode().getNodeType(nVVFComponent);                       
    	if(nt1 == nt2) return nt1;                  
    	else {
    		//TODO Exception?
    		return null;
    	}
    }
    
	public boolean isBorderEdge() {
		if(this.vertices.at(1).globalNode().getNodeType()==NodeType.Inner ||
				this.vertices.at(2).globalNode().getNodeType()==NodeType.Inner)
			return false;
		else
			return true;
		
//		for(NodeType nt : NodeType.values()) {
//			if(nt == NodeType.Inner) continue;
//			if(nt == this.edgeNodes.at(1).getNodeType() &&
//					nt == this.edgeNodes.at(this.edgeNodes.size()).getNodeType()) 
//				return true;
//		}
//		return false;
	}
	
	/**
	 * 判断�?个�??标点是�?�在edge上
	 * @param coord
	 * @return
	 */
	public boolean isCoordOnEdge(double coords[]) {
		return Utils.isPointOnLineSegment(
				this.vertices.at(1),
				this.vertices.at(2),
				new Vertex().set(0, coords));
	}
	
    public Vertex beginVertex() {
		return this.vertices.at(1);
    }
    
    public Vertex endVertex() {
		return this.vertices.at(2);
    }
    
	public Node beginNode() {
		return this.vertices.at(1).globalNode();
	}

	public Node endNode() {
		return this.vertices.at(2).globalNode();
	}
	
	public NodeList getEndNodes() {
		NodeList rlt = new NodeList();
		rlt.add(this.vertices.at(1).globalNode());
		rlt.add(this.vertices.at(2).globalNode());
		return rlt;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean containsNode(Node node) {
		if(this.beginNode().equals(node))
			return true;
		if(this.endNode().equals(node))
			return true;
		if(this.edgeNodes != null) {
			for(int i=1;i<=this.edgeNodes.size();i++)
				if(this.edgeNodes.at(i).equals(node))
					return true;
		}
		return false;
	}
	
	public double getEdgeLength() {
		return Utils.computeLength(
					this.vertices.at(1),
					this.vertices.at(2)
				);
	}
	
	public String toString() {
		return "Edge"+this.globalIndex+":"+this.vertices.toString();
	}	
	
	@Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) {
        	return true;
        } else {
        	if(obj instanceof Edge) {
        		Edge ed2 = (Edge)obj;
        		if( globalIndex == ed2.globalIndex && 
        			beginNode().equals(ed2.beginNode()) && 
        			endNode().equals(ed2.endNode())) {
        			//全局索引相�?�，两端结点相�?�
        			return true;
        		}
        	}
        }
        return false;
    }
    
    @Override
    public int hashCode() {
    	if(globalIndex != 0) 
    		return globalIndex;
    	else 
    		return super.hashCode();
    }
}

package mathLib.fem.core;

import mathLib.fem.core.geometry.GeoEntity1D;
import mathLib.fem.util.Utils;
import mathLib.fem.util.container.DOFList;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.FMath;
import mathLib.matrix.algebra.intf.Vector;

/**
 * Local edge of an element
 *
 */
public class EdgeLocal extends GeoEntity1D<NodeLocal> {

	public int localIndex;
	//global edge shared with all elements that containing the edge
	protected Edge globalEdge = null;
	public Element owner = null;
	
	private Vector localUnitNormVector = null;
	
	public EdgeLocal(int localIndex, Element owner) {
		this.localIndex = localIndex;
		this.owner = owner;
	}
	
	public EdgeLocal(int localIndex, Edge globalEdge) {
		this.localIndex = localIndex;
		this.globalEdge = globalEdge;
	}
	
	public EdgeLocal(int localIndex, Edge globalEdge, Element owner) {
		this.localIndex = localIndex;
		this.globalEdge = globalEdge;
		this.owner = owner;
	}
	
	public void setGlobalEdge(Edge globalEdge) {
		this.globalEdge = globalEdge;
	}
	
	public Edge getGlobalEdge() {
    	return this.globalEdge;
    }

    public NodeType getBorderType() {
    	return getBorderType(1);
    }
    
	/**
	 * For vector valued problems, return boundary type of component <tt>nVVFComponent</tt>
	 * <p>
	 * 
	 * @param nVVFComponent
	 * @return
	 */
    public NodeType getBorderType(int nVVFComponent) {
    	NodeType nt1 = this.beginNode().getNodeType(nVVFComponent);
    	NodeType nt2 = this.endNode().getNodeType(nVVFComponent);
    	if(nt1 == nt2) return nt1;
    	else {
    		//TODO Exception?
    		return null;
    	}
    }
    
    public boolean isBorderEdge() {
		if(this.beginNode().getNodeType()==NodeType.Inner ||
				this.endNode().getNodeType()==NodeType.Inner)
			return false;
		else
			return true;
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
    
    public Vector getNormVector() {
    	if(localUnitNormVector == null) {
    		if(this.globalEdge != null) {
    		 
    		if(this.beginNode().globalIndex == this.globalEdge.beginNode().globalIndex)
    			localUnitNormVector = this.globalEdge.getNormVector().copy();
    		else
    			localUnitNormVector = FMath.ax(-1.0, this.globalEdge.getNormVector());
    		} else {
    			localUnitNormVector = Utils.getNormVector(
    					this.beginNode(),
    					this.endNode()
    					);
    		}
    	}
    	return this.localUnitNormVector;
    }
    
    public Edge buildEdge() {
    	Edge edge = new Edge();
    	edge.addVertex(new Vertex(1,new NodeLocal(1,this.beginNode())));
    	edge.addVertex(new Vertex(2,new NodeLocal(2,this.endNode())));
		ObjList<NodeLocal> edgeNodes = this.getEdgeNodes();
		if(edgeNodes != null && edgeNodes.size()>0) {
			for(int i=1;i<=edgeNodes.size();i++)
				edge.addEdgeNode(new NodeLocal(2+i,edgeNodes.at(i).globalNode));
		}
		return edge;
    }
	
	public Element changeToElement(Element parentElement) {
		
		Element be = new Element(this.buildEdge());
		be.parent = parentElement;
		Edge edge = (Edge)be.geoEntity;
		ObjList<NodeLocal> edgeNodes = this.getEdgeNodes();
		VertexList vertices = this.getVertices();
		int nNode;
		if(edgeNodes != null) {
			nNode = edgeNodes.size() + vertices.size();
		} else {
			nNode =  vertices.size();
		}
		
		
		int dofIndex = 1;
		DOFList eDOFList = owner.getNodeDOFList(this.vertices.at(1).localNode().localIndex);
		for(int j=1;eDOFList!=null && j<=eDOFList.size();j++) {
			DOF dof = new DOF(
						dofIndex,
						eDOFList.at(j).globalIndex,
						eDOFList.at(j).getSF()==null?null:eDOFList.at(j).getSF().restrictTo(dofIndex)
					);
			dof.setVVFComponent(eDOFList.at(j).getVVFComponent());
			be.addNodeDOF(1, dof);//DOFs on first node
			dofIndex += nNode;
		}
		dofIndex = 2;
		eDOFList = owner.getNodeDOFList(this.vertices.at(2).localNode().localIndex);
		for(int j=1;eDOFList!=null && j<=eDOFList.size();j++) {
			DOF dof = new DOF(
						dofIndex,
						eDOFList.at(j).globalIndex,
						eDOFList.at(j).getSF()==null?null:eDOFList.at(j).getSF().restrictTo(dofIndex)
					);
			dof.setVVFComponent(eDOFList.at(j).getVVFComponent());
			be.addNodeDOF(2, dof);//DOFs on second node
			dofIndex += nNode;
		}
		
		if(edgeNodes != null) {
			for(int i=1; i<=edgeNodes.size(); i++) {
				dofIndex = i;
				NodeLocal node = edgeNodes.at(i);
				eDOFList = owner.getNodeDOFList(node.localIndex);
				for(int j=1;eDOFList!=null && j<=eDOFList.size();j++) {
					DOF dof = new DOF(
						dofIndex,
						eDOFList.at(j).globalIndex,
						eDOFList.at(j).getSF().restrictTo(dofIndex)
					);
					dof.setVVFComponent(eDOFList.at(j).getVVFComponent());
					be.addNodeDOF(edge.getEdgeNodes().at(i).localIndex, dof);//DOFs on nodes 3th,4th,5th...
					//dofIndex += DOFMatrix.rowDim()
					dofIndex += nNode;
				}
			}
		}
		return be;
	}
	
	public String toString() {
		return "EdgeLocal"+this.localIndex+"<=>"+this.vertices.toString();
	}
}

package mathLib.fem.core;

import mathLib.fem.core.geometry.GeoEntity2D;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.DOFList;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.VertexList;

/**
 * Local face of an element
 *
 */
public class FaceLocal extends GeoEntity2D<EdgeLocal,NodeLocal> {
	public int localIndex;
	//global face shared with all elements that containing the face
	protected Face globalFace = null; 
	public Element owner = null;

	protected Vector localUnitNormVector;

	public Vector getLocalUnitNormVector() {
		return localUnitNormVector;
	}

	public void setLocalUnitNormVector(Vector localUnitNormVector) {
		this.localUnitNormVector = localUnitNormVector;
	}

	public FaceLocal(int localIndex, Element owner) {
		this.localIndex = localIndex;
		this.owner = owner;
	}
	
	public FaceLocal(int localIndex, Face globalFace) {
		this.localIndex = localIndex;
		this.globalFace = globalFace;
	}
	
	public FaceLocal(int localIndex, Face globalFace, Element owner) {
		this.localIndex = localIndex;
		this.globalFace = globalFace;
		this.owner = owner;
	}
	
    public NodeType getBorderType() {
    	return getBorderType(1);                  
    }
    
	/**
	 * For vector valued problems, return boundary type of component <tt>nVVFComponent</tt>
	 * <p>
	 * 
	 * 
	 * @param nVVFComponent
	 * @return
	 */
    public NodeType getBorderType(int nVVFComponent) {
    	NodeType nt1 = this.vertices.at(1).globalNode().getNodeType(nVVFComponent);
    	for(int i=2;i<this.vertices.size();i++) {
    		NodeType nt2 = this.vertices.at(2).globalNode().getNodeType(nVVFComponent);
    	   	if(nt1 != nt2) return null;                  
    	}
    	return nt1;
    }
	
	public boolean isBorderFace() {
		ObjList<Vertex> vs = this.getVertices();
		if(vs.size() >= 3) {
			for(int i=1;i<=vs.size();i++) {
				NodeLocal nl = vs.at(i).localNode();
				if(nl.globalNode.getNodeType()==NodeType.Inner)
					return false;
			}
		} else {
			FutureyeException ex = new FutureyeException("Number of vertices on a face is "+vs.size());
			ex.printStackTrace();
			System.exit(0);
		}
		return true;
	}
	
	public Face getGlobalFace() {
		return globalFace;
	}

	public void setGlobalFace(Face globalFace) {
		this.globalFace = globalFace;
	}
	
	public Face buildFace() {
		Face face = new Face();
		ObjList<Vertex> vertices = this.getVertices();
		for(int n=1;n<=vertices.size();n++) {
			face.addVertex(new Vertex(n,new NodeLocal(n,vertices.at(n).globalNode())));
		}
		ObjList<EdgeLocal> localEdges = this.getEdges();
		for(int n=1;n<=localEdges.size();n++) {
			face.addEdge(new EdgeLocal(n, localEdges.at(n).globalEdge));
		}
		ObjList<NodeLocal> localFaceNodes = this.getFaceNodes();
		if(localFaceNodes != null && localFaceNodes.size()>0) {
			for(int n=1;n<=localFaceNodes.size();n++) {
				face.addFaceNode(new NodeLocal(n,localFaceNodes.at(n).globalNode));
			}
		}
		face.setTopology(this.getTopology());
		return face;
	}
	
	public Element changeToElement() {
		
		Element be = new Element(this.buildFace());
		
		//赋予 Node DOFs
		VertexList beVertices = be.vertices();
		int localDOFIndex = 1;
		for(int i=1;i<=beVertices.size();i++) {
			DOFList eDOFList = owner.getNodeDOFList(beVertices.at(i).localNode().localIndex);
			for(int j=1;eDOFList!=null && j<=eDOFList.size();j++) {
				DOF dof = new DOF(
							localDOFIndex,
							eDOFList.at(j).globalIndex,
							eDOFList.at(j).getSF().restrictTo(localDOFIndex)
						);
				dof.setVVFComponent(eDOFList.at(j).getVVFComponent());
				be.addNodeDOF(i, dof);
				localDOFIndex++;
			}
			
		}
		
		return be;		
		
	}
	
	public String toString() {
		if(this.globalFace != null)
			return "LocalFace"+localIndex+"<=>"+globalFace.toString();
		else
			return "LocalFace"+localIndex+this.vertices.toString();
	}
}
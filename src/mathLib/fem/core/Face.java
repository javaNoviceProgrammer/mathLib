package mathLib.fem.core;

import mathLib.fem.core.geometry.GeoEntity2D;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.ObjList;
import mathLib.matrix.algebra.SpaceVector;

/**
 * Global face of an element
 *
 */
public class Face extends GeoEntity2D<EdgeLocal,NodeLocal> {
	protected int globalIndex;
	protected Vector globalUnitNormVector = null; 
	
	public int getGlobalIndex() {
		return globalIndex;
	}

	public void setGlobalIndex(int globalIndex) {
		this.globalIndex = globalIndex;
	}
	
	public Vector getNormVector() {
		if(this.globalUnitNormVector == null) {
			ObjList<Vertex> vs = this.getVertices();
			Point p1 = vs.at(1);
			Point p2 = vs.at(2);
			Point p3 = vs.at(3);
			SpaceVector s1 = new SpaceVector(3);
			SpaceVector s2 = new SpaceVector(3);
			s1.set(1, p2.coord(1)-p1.coord(1));
			s1.set(2, p2.coord(2)-p1.coord(2));
			s1.set(3, p2.coord(3)-p1.coord(3));
			
			s2.set(1, p3.coord(1)-p2.coord(1));
			s2.set(2, p3.coord(2)-p2.coord(2));
			s2.set(3, p3.coord(3)-p2.coord(3));
			
			Vector rlt = s1.crossProduct(s2);
			rlt.scale(1.0/rlt.norm2());
			return rlt;
		}
		return this.globalUnitNormVector;
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
		NodeType nt1 = this.vertices.at(1).globalNode()
				.getNodeType(nVVFComponent);
		for (int i = 2; i < this.vertices.size(); i++) {
			NodeType nt2 = this.vertices.at(2).globalNode()
					.getNodeType(nVVFComponent);
			if (nt1 != nt2)
				return null;
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
	
	public String toString() {
		return "GlobalFace"+this.globalIndex+this.vertices.toString();
	}
}

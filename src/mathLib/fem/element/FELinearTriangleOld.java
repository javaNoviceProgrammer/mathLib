package mathLib.fem.element;

import mathLib.fem.core.DOF;
import mathLib.fem.shapefun.SFLinearLocal2DRS;
import mathLib.fem.util.container.VertexList;

/**
 * Linear Triangle element for 2D
 * 2维三角形线性�?�元
 * 
 * @author liuyueming
 *
 */
public class FELinearTriangleOld implements FiniteElementType {
	//protected static SFLinearLocal2D[] shapeFun = new SFLinearLocal2D[3];
	protected static SFLinearLocal2DRS[] shapeFun = new SFLinearLocal2DRS[3];
	
	public FELinearTriangleOld() {
//		shapeFun[0] = new SFLinearLocal2D(1);
//		shapeFun[1] = new SFLinearLocal2D(2);
//		shapeFun[2] = new SFLinearLocal2D(3);
		shapeFun[0] = new SFLinearLocal2DRS(1);
		shapeFun[1] = new SFLinearLocal2DRS(2);
		shapeFun[2] = new SFLinearLocal2DRS(3);
	}
	
	/**
	 * Assign degree of freedom to element
	 * @param e
	 */
	public void assignTo(Element e) {
		VertexList vertices = e.vertices();
		for(int j=1;j<=vertices.size();j++) {
			Vertex v = vertices.at(j);
			//Assign shape function to DOF
			DOF dof = new DOF(
						j, //Local DOF index
						v.globalNode().getIndex(), //Global DOF index, take global node index
						shapeFun[j-1] //Shape function 
						);
			e.addNodeDOF(j, dof);
		}
	}

	@Override
	public int getDOFNumOnElement(int vsfDim) {
		return 3;
	}

	@Override
	public int getVectorShapeFunctionDim() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getDOFNumOnMesh(Mesh mesh, int vsfDim) {
		return mesh.getNodeList().size();
	}

	@Override
	public void initDOFIndexGenerator(Mesh mesh) {
		// TODO Auto-generated method stub
		
	}	
}

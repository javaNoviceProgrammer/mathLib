package mathLib.fem.element;

import mathLib.fem.core.DOF;
import mathLib.fem.shapefun.SFLinearLocal1D;
import mathLib.fem.util.container.VertexList;

/**
 * One dimension linear element
 * 
 */
public class FELinearLine1DOld implements FiniteElementType {
	protected static SFLinearLocal1D[] shapeFun = new SFLinearLocal1D[2];
	
	public FELinearLine1DOld() {
		shapeFun[0] = new SFLinearLocal1D(1);
		shapeFun[1] = new SFLinearLocal1D(2);
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
		return 2;
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

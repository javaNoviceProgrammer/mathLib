package mathLib.fem.element;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Vertex;
import mathLib.fem.shapefun.SFBilinearLocal2D;
import mathLib.fem.util.container.VertexList;

public class FEBilinearRectangleOld implements FiniteElementType {
	SFBilinearLocal2D[] shapeFun = new SFBilinearLocal2D[4];

	public FEBilinearRectangleOld() {
		shapeFun[0] = new SFBilinearLocal2D(1);
		shapeFun[1] = new SFBilinearLocal2D(2);
		shapeFun[2] = new SFBilinearLocal2D(3);
		shapeFun[3] = new SFBilinearLocal2D(4);
	}
	
	/**
	 * Assign degree of freedom to element
	 * @param e
	 */
	@Override
	public void assignTo(Element e) {
		//Asign degree of freedom to element
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
		return 4;
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

package mathLib.fem.element;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;
import mathLib.fem.shapefun.SFLinearLocal2DVector;
import mathLib.fem.util.FutureyeException;

public class FELinearTriangleVector implements FiniteElementType {

	SFLinearLocal2DVector[] shapeFun = new SFLinearLocal2DVector[6];
	protected int nTotalNodes = -1;

	public FELinearTriangleVector() {
		for(int i=0;i<6;i++)
			shapeFun[i] = new SFLinearLocal2DVector(i+1);
	}
	
	public void initDOFIndexGenerator(int nTotalNodes) {
		this.nTotalNodes = nTotalNodes;
	}
	
	@Override
	public void assignTo(Element e) {
		if(this.nTotalNodes < 0) {
			throw new FutureyeException("nTotalNodes="+nTotalNodes);
		}
		int nNode = e.nodes.size();
		//Assign shape function to DOF
		for(int j=1;j<=nNode;j++) {
			//Asign shape function to DOF
			DOF dof_u1 = new DOF(
					j,//Local DOF index
					//Global DOF index, take global node index
					e.nodes.at(j).globalIndex,
					shapeFun[j-1]//Shape function 
					         );
			dof_u1.setVVFComponent(1);
			DOF dof_u2 = new DOF(
					nNode+j,//Local DOF index
					//Global DOF index, take this.nTotalNodes + global node index
					this.nTotalNodes+e.nodes.at(j).globalIndex,
					shapeFun[nNode+j-1]//Shape function 
					         );
			dof_u2.setVVFComponent(2);
			e.addNodeDOF(j, dof_u1);
			e.addNodeDOF(j, dof_u2);
		}
	}

	@Override
	public int getDOFNumOnElement(int vsfDim) {
		return 6;
	}

	@Override
	public int getVectorShapeFunctionDim() {
		return 2;
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

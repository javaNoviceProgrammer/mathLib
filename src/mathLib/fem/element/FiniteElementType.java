package mathLib.fem.element;

import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;

public interface FiniteElementType {
	/**
	 * Associate degrees of freedom (DOF) to element e
	 * 
	 * @param e
	 */
	void assignTo(Element e);

	void initDOFIndexGenerator(Mesh mesh);
	
	int getVectorShapeFunctionDim();
	
	int getDOFNumOnElement(int vsfDim);
	
	int getDOFNumOnMesh(Mesh mesh,int vsfDim);
}

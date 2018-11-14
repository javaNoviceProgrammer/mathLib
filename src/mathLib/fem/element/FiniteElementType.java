package mathLib.fem.element;

public interface FiniteElementType {
	/**
	 * Associate degrees of freedom (DOF) to element e
	 * 
	 * @param e
	 */
	void assignTo(Element e);
	
	/**
	 * �?始化自由度编�?�生�?器
	 * @param nTotalNodes
	 */
	void initDOFIndexGenerator(Mesh mesh);
	
	/**
	 * 获得�?��?值形函数的维度
	 * @return
	 */
	int getVectorShapeFunctionDim();
	
	/**
	 * 获得�?�元上，自由度总数。如果�?�元上自由度关�?�的形函数为标�?函数，
	 * �?�数vsfDim�?��?�任�?值，如果为�?��?值函数，需�?指定�?��?维度vsfDim，
	 * 将返回该维度对应的自由度总数。
	 * 
	 * @param vsfDim �?��?值形函数的维度
	 * @return
	 */
	int getDOFNumOnElement(int vsfDim);
	
	/**
	 * 获得整个网格上，自由度总数。如果网格的�?�元上自由度关�?�的形函数为标�?函数，
	 * �?�数vsfDim�?��?�任�?值，如果为�?��?值函数，需�?指定�?��?维度vsfDim，
	 * 将返回该维度对应的自由度总数。
	 * 
	 * @param mesh
	 * @param vsfDim
	 * @return
	 */
	int getDOFNumOnMesh(Mesh mesh,int vsfDim);
}

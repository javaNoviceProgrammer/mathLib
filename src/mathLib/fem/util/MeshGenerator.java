package mathLib.fem.util;

import java.util.HashMap;
import java.util.Map;

import mathLib.func.symbolic.intf.MathFunc;

public class MeshGenerator {
	
	public static Mesh linear1D(double x0, double x1, int nx) {
		Mesh mesh = new Mesh();
		double L = x1 - x0 ;
		double h = L/nx;
		Node node1 = new Node(1, x0);
		mesh.addNode(node1);
		for(int i=2; i<= nx+1; i++) {
			Node node2 = new Node(i, x0 + (i-1)*h);
			mesh.addNode(node2);
			NodeList nodeList = new NodeList();
			nodeList.add(node1);
			nodeList.add(node2);
			Element e = new Element(nodeList);
			mesh.addElement(e);
			node1 = node2;
		}
		return mesh;
	}
	
	
	/*
	 * Generate a rectangle mesh on [x0,x1]*[y0,y1] with nx points in X
	 * direction and ny points in Y direction
	 */
	public static Mesh rectangle2D(double x0, double x1, double y0, double y1,
			int nx, int ny) {
		Mesh mesh = new Mesh();
		double stepx = (x1 - x0) / nx;
		double stepy = (y1 - y0) / ny;
		// generate nodes
		/**
		 * nx=3, ny=3 
		 * node index: 1,2,...,9 
		 * element index: (1),(2),...,(4)
		 * 7-----8-----9 
		 * | (3) | (4) | 
		 * 4-----5-----6 
		 * | (1) | (2) | 
		 * 1-----2-----3
		 */
		for (int i = 0; i < ny; i++) {
			double y = y0 + i * stepy;
			for (int j = 0; j < nx; j++) {
				double x = x0 + j * stepx;
				int index = i * ny + j + 1;
				Node node = new Node(index, x, y);
				mesh.addNode(node);
			}
		}

		// generate elements
		NodeList nodes = mesh.getNodeList();
		
		for (int i = 0; i < nx - 1; i++) {
			for (int j = 0; j < ny - 1; j++) {
				int n1 = i * nx + j;
				int n2 = n1 + 1;
				int n3 = (i + 1) * nx + j;
				NodeList list = new NodeList();
				list.add(nodes.at(n1+1));
				list.add(nodes.at(n2+1));
				list.add(nodes.at(n3+1));
				Element e = new Element(list);
				mesh.addElement(e);

				e = new Element();
				n1 = i * nx + j + 1;
				n2 = (i + 1) * nx + j + 1;
				n3 = n2 - 1;
				list = new NodeList();
				list.add(nodes.at(n1+1));
				list.add(nodes.at(n2+1));
				list.add(nodes.at(n3+1));
				e = new Element(list);
				mesh.addElement(e);
			}
		}
		//mesh.printMeshInfo();
		return mesh;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mesh test = rectangle2D(0,1,0,1,3,3);
		test.computeNodeBelongsToElements();
		Map<NodeType, MathFunc> mapNTF = new HashMap<NodeType, MathFunc>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		test.markBorderNode(mapNTF);
		System.out.println(test.getNodeList());
		System.out.println(test.getElementList());
//		test.printMeshInfo();
		
	}
}

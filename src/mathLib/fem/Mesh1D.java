package mathLib.fem;

import mathLib.fem.core.Mesh;
import mathLib.fem.util.MeshGenerator;

public class Mesh1D {

	double xMin, xMax ;
	int numNodes ;
	Mesh mesh ;

	public Mesh1D(double xMin, double xMax, int numNodes) {
		this.xMin = xMin ;
		this.xMax = xMax ;
		this.numNodes = numNodes ;
		this.mesh = MeshGenerator.linear1D(xMin, xMax, numNodes-1) ;
	}

	public Mesh getMesh() {
		return mesh ;
	}

	public int getNumberOfNodes() {
		return mesh.getNodeList().size() ;
	}

	public int getNumberOfElements() {
		return mesh.getElementList().size() ;
	}

	public void refine() {
		numNodes = 2*numNodes ;
		this.mesh = MeshGenerator.linear1D(xMin, xMax, numNodes-1) ;
	}

	public Mesh1D getRefined() {
		// double the number of nodes
		return new Mesh1D(xMin, xMax, 2*numNodes) ;
	}

	// for test
	public static void main(String[] args) {
		Mesh1D mesh1d = new Mesh1D(0, 10, 20) ;
		Mesh1D mesh1d1 = new Mesh1D(0, 10, 30) ;
		System.out.println(mesh1d.getMesh().getNodeList());
		System.out.println(mesh1d.getMesh().getNodeList());
		Node n1 = mesh1d.getMesh().getNodeList().at(20) ;
		Node n11 = mesh1d1.getMesh().getNodeList().at(30) ;
		System.out.println(n1);
		System.out.println(n11);
	}

}

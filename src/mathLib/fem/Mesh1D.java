package mathLib.fem;

import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.util.MeshGenerator;

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
		// divide each element into two
		return new Mesh1D(xMin, xMax, 2*numNodes) ;
	}
	
	// for test
	public static void main(String[] args) {
		Mesh1D mesh1d = new Mesh1D(0, 10, 20) ;
		System.out.println(mesh1d.getMesh().getNodeList());
		mesh1d.refine();
		System.out.println(mesh1d.getMesh().getNodeList());
	}

}

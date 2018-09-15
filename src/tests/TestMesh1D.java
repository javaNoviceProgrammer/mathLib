package tests;

import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.util.MeshGenerator;

public class TestMesh1D {
	public static void main(String[] args) {
		Mesh mesh = MeshGenerator.linear1D(0.0, 10.0, 10) ;
		System.out.println(mesh.getNodeList());
		System.out.println(mesh.getElementList());
	}
}

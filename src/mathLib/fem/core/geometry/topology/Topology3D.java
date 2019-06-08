package mathLib.fem.core.geometry.topology;

public interface Topology3D extends Topology2D {
	
	public int[][] getFaces();
	
	boolean edgeOnface(int[] face, int[] edge);
}

package mathLib.geometry.algebra;

public class Point {

	private double x, y, z ;
	int dim ;
	
	public Point(double x) { // 1D point
		this.x = x ;
		this.y = 0 ;
		this.z = 0 ;
		this.dim = 1 ;
	}
	
	public Point(double x, double y) { // 2D point
		this.x = x ;
		this.y = y ;
		this.z = 0 ;
		this.dim = 2 ;
	}
	
	public Point(double x, double y, double z) { // 3D point
		this.x = x ;
		this.y = y ;
		this.z = z ;
	}
	
	
}

package mathLib.geometry.algebra;

import java.util.HashSet;
import java.util.Set;

public class Point {

	public static final Point Px = new Point(1d, 0d, 0d) ;
	public static final Point Py = new Point(0d, 1d, 0d) ;
	public static final Point Pz = new Point(0d, 0d, 1d) ;
	public static final Point ZERO = new Point(0d, 0d, 0d) ;

	private double x, y, z ;
	String name = "" ; // name of the point

	public Point(double x, double y, double z) { // 3D point
		this.x = x ;
		this.y = y ;
		this.z = z ;
	}
	
	public static Point getInstance(double x, double y, double z) {
		return new Point(x, y, z) ;
	}
	
	public static Point getInstance(double x, double y) {
		return new Point(x, y, 0.0) ;
	}
	
	public static Point getInstance(double x) {
		return new Point(x, 0.0, 0.0) ;
	}

	public void setName(String name) {
		this.name = name ;
	}

	public double getX() {
		return x ;
	}

	public double getY() {
		return y ;
	}

	public double getZ() {
		return z ;
	}
	
	public double getMagnitude() {
		return Math.sqrt(x*x+y*y+z*z) ;
	}

	// math operations

	public Point plus(Point p) {
		return new Point(this.x + p.x, this.y + p.y, this.z + p.z) ;
	}

	public Point minus(Point p){
		return new Point(this.x - p.x, this.y - p.y, this.z - p.z) ;
	}

	public Point times(double alpha){
		return new Point(this.x*alpha, this.y*alpha, this.z*alpha) ;
	}

	public Point divides(double alpha){
		return new Point(this.x/alpha, this.y/alpha, this.z/alpha) ;
	}

	// to String

	@Override
	public String toString() {
		if(name==null || name.equals("")){
			return "(" + x + ", " + y + ", " + z + ")";
		}
		else{
			return name + "=(" + x + ", " + y + ", " + z + ")";
		}
	}
	
 	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		double tol = 1e-10 ;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Math.abs(x-other.x)>tol)
			return false;
		if (Math.abs(y-other.y)>tol)
			return false;
		if (Math.abs(z-other.z)>tol)
			return false;
		return true;
	}

    // ************ operator overloading **********************

	/**
 	 * Operator overloading support:
 	 *
 	 * Object p = 5;
 	 *
 	 */
 	public static Point valueOf(int p) {
 		return new Point(p, 0, 0);
 	}

 	public static Point valueOf(long p) {
 		return new Point(p, 0, 0);
 	}

 	public static Point valueOf(float p) {
 		return new Point(p, 0, 0);
 	}

 	public static Point valueOf(double p) {
 		return new Point(p, 0, 0);
 	}

 	public static Point valueOf(Point p) {
 		return new Point(p.x, p.y, p.z);
 	}

 	/**
 	 * Operator overload support: a+b
 	 */
 	public Point add(Point p) {
 		return this.plus(p) ;
 	}

 	public Point addRev(Point p) {
 		return this.plus(p) ;
 	}

 	public Point add(int v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point addRev(int v) {
 		return new Point(v+x, v+y, v+z);
 	}

 	public Point add(long v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point addRev(long v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point add(float v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point addRev(float v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point add(double v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	public Point addRev(double v) {
 		return new Point(x+v, y+v, z+v) ;
 	}

 	/**
 	 * Operator overload support: a-b
 	 */
 	public Point subtract(Point v) {
 		return new Point(this.x-v.x, this.y-v.y, this.z-v.z) ;
 	}

 	public Point subtractRev(Point v) {
 		return new Point(v.x-this.x, v.y-this.y, v.z-this.z) ;
 	}

 	public Point subtract(int v) {
 		return new Point(x-v, y-v, z-v) ;
 	}

 	public Point subtractRev(int v) {
 		return new Point(v-x, v-y, v-z) ;
 	}

 	public Point subtract(long v) {
 		return new Point(x-v, y-v, z-v) ;
 	}

 	public Point subtractRev(long v) {
 		return new Point(v-x, v-y, v-z) ;
 	}

 	public Point subtract(float v) {
 		return new Point(x-v, y-v, z-v) ;
 	}

 	public Point subtractRev(float v) {
 		return new Point(v-x, v-y, v-z) ;
 	}

 	public Point subtract(double v) {
 		return new Point(x-v, y-v, z-v) ;
 	}

 	public Point subtractRev(double v) {
 		return new Point(v-x, v-y, v-z) ;
 	}

 	/**
 	 * Operator overload support: a*b
 	 */

 	public Point multiply(int v) {
 		return this.times(v) ;
 	}

 	public Point multiplyRev(int v) {
 		return this.times(v) ;
 	}

 	public Point multiply(long v) {
 		return this.times(v) ;
 	}

 	public Point multiplyRev(long v) {
 		return this.times(v) ;
 	}

 	public Point multiply(float v) {
 		return this.times(v) ;
 	}

 	public Point multiplyRev(float v) {
 		return this.times(v) ;
 	}

 	public Point multiply(double v) {
 		return this.times(v) ;
 	}

 	public Point multiplyRev(double v) {
 		return this.times(v) ;
 	}

 	/**
 	 * Operator overload support: a/b
 	 */

 	public Point divide(int v) {
 		return this.divides(v);
 	}

 	public Point divide(long v) {
 		return this.divides(v);
 	}

 	public Point divide(float v) {
 		return this.divides(v);
 	}

 	public Point divide(double v) {
 		return this.divides(v);
 	}


 	/**
 	 * Operator overload support: -a
 	 */
 	public Point negate() {
 		return this.times(-1) ;
 	}


	// for test
	public static void main(String[] args) {
		Point p1 = new Point(2,3, 5.1) ;
		p1.setName("p1");
		System.out.println(p1);

		Point p2 = new Point(-1, -1, -1.2) ;
		p2.setName("P2");
		System.out.println(p2);

		System.out.println(p1.plus(p2)); // without operator overloading
		System.out.println(p1+p2); // with operator overloading

		Point p3 = 2*Px + 3*Py - 4.2*Pz ;
		p3.setName("P3");
		System.out.println(p3);
		System.out.println(2*p3/3);
		
		Point p4 = 2*Px + 3*Py ;
		System.out.println(p4);
		Point p5 = 2.00000000000001*Px + 3.00000000000007*Py ;
		System.out.println(p5);
		System.out.println(p5-p4);
		System.out.println(p4.equals(p5));
		Set<Point> set = new HashSet<>() ;
		set.add(p4) ;
		System.out.println(set);
		set.add(p4) ;
		System.out.println(set);
	}


}

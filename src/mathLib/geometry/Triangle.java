package mathLib.geometry;

import mathLib.geometry.algebra.Point;
import mathLib.util.Conversions;
import mathLib.util.Units;

public class Triangle {
	
	public static final int SIDE = 0 ;
	public static final int ANGLE = 1 ;
	
	private double a, b, c ; // sides
	private double A, B, C ; // angles
	private double R ; // circumcircle radius
	private double s ; // surface area
	private double p ; // perimeter
	
	public Triangle(double a, double b, double c) {
		this.a = a ;
		this.b = b ;
		this.c = c ;
		calculateAngles() ;
		calculateR() ;
		calculateS() ;
		calculateP() ;
	}
	
	public Triangle(Point A, Point B, Point C) {
		this.a = (C-B).getMagnitude() ;
		this.b = (A-C).getMagnitude() ;
		this.c = (A-B).getMagnitude() ;
		calculateAngles() ;
		calculateR() ;
		calculateS() ;
		calculateP() ;
	}

	private void calculateAngles() {
		// a^2 = b^2 + c^2 - 2bc Cos(A)
		double cosA = (b*b + c*c - a*a)/(2*b*c) ;
		A = Math.acos(cosA) ;
		// b^2 = a^2 + c^2 - 2ac Cos(B)
		double cosB = (a*a + c*c - b*b)/(2*a*c) ;
		B = Math.acos(cosB) ;
		// c^2 = a^2 + b^2 - 2ab CosC
		double cosC = (a*a + b*b - c*c)/(2*a*b) ;
		C = Math.acos(cosC) ;
	}
	
	private void calculateR() {
		R = a/(2*Math.sin(A)) ;
	}
	
	private void calculateP() {
		p = a + b + c ;
	}
	
	private void calculateS() {
		s = 0.5*a*b*Math.sin(C) ;
	}

	public double getSideA() {
		return a ;
	}
	
	public double getSideB() {
		return b ;
	}
	
	public double getSideC() {
		return c ;
	}
	
	public double getAngleA() {
		return A ;
	}
	
	public double getAngleB() {
		return B ;
	}
	
	public double getAngleC() {
		return C ;
	}
	
	public double getR() {
		return R ;
	}
	
	public double getPerimeter() {
		return p ;
	}
	
	public double getArea() {
		return s ;
	}
	
	
	// for test
	public static void main(String[] args) {
//		Triangle triangle = new Triangle(3, 4, 5) ;
//		Triangle triangle = new Triangle(5.0, 5.0*Math.sqrt(2.0), 5.0) ;
		Triangle triangle = new Triangle(new Point(0, 0, 0), new Point(5, 0, 0), new Point(0, 5, 0)) ;
		System.out.println("A = " + Conversions.angle(triangle.getAngleA(), Units.radian, Units.degree) + " degree");
		System.out.println("B = " + Conversions.angle(triangle.getAngleB(), Units.radian, Units.degree) + " degree");
		System.out.println("C = " + Conversions.angle(triangle.getAngleC(), Units.radian, Units.degree) + " degree");
		System.out.println("R = " + triangle.getR());
		System.out.println("S = " + triangle.getArea());
	}
	
}

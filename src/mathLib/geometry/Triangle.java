package mathLib.geometry;

import mathLib.util.conversion.Conversions;

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
	
	public Triangle(double a, int defA, double b, int defB, double c, int defC) {
		if (defA == 0 && defB == 0 && defC == 0) {
			this.a = a ;
			this.b = b ;
			this.c = c ;
			calculateAngles() ;
			calculateR() ;
			calculateS() ;
			calculateP() ;
		}
		else if (defA == 1 && defB == 0 && defC == 0) {
			
		}
		else {
			throw new RuntimeException("defA, defB, defC must be either 0 or 1") ;
		}
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
		Triangle triangle = new Triangle(5, Triangle.SIDE, 5, Triangle.SIDE, 5, Triangle.SIDE) ;
		System.out.println("A = " + Conversions.Angles.toDegree(triangle.getAngleA()) + " degree");
		System.out.println("B = " + Conversions.Angles.toDegree(triangle.getAngleB()) + " degree");
		System.out.println("C = " + Conversions.Angles.toDegree(triangle.getAngleC()) + " degree");
		System.out.println("R = " + triangle.getR());
		System.out.println("S = " + triangle.getArea());
	}
	
}

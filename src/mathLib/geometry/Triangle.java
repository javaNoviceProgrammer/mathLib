package mathLib.geometry;

public class Triangle {
	
	public double a, b, c ; // sides
	public double A, B, C ; // angles
	public double R ;
	
	public Triangle(double a, double b, double c) {
		this.a = a ;
		this.b = b ;
		this.c = c ;
		calculateAngles() ;
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

}

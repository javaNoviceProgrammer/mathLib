package mathLib.integral;

import mathLib.polynom.Polynomial;

public class AntiDerive {
	
	public static double getSin(double x) {
		return -Math.cos(x) ;
	}
	
	public static double getCos(double x) {
		return Math.sin(x) ;
	}
	
	public static double getTan(double x) {
		return Math.log(1/Math.cos(x)) ;
	}
	
	public static double getCot(double x) {
		return Math.log(Math.sin(x)) ;
	}
	
	public static double getPolynomial(Polynomial p, double x) {
		return p.integrate().evaluate(x) ;
	}
	
}

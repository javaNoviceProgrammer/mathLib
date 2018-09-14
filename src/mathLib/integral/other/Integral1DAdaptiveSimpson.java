package mathLib.integral.other;

import flanagan.integration.IntegralFunction;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.util.Timer;

public class Integral1DAdaptiveSimpson {

	double errorBound = 1e-10 ;
	IntegralFunction func ;
	
	public Integral1DAdaptiveSimpson(
			IntegralFunction1D func
			) {
		this.func = func ;
	}
	
	public void setAccuracy(double epsilon) {
		this.errorBound = epsilon ;
	}
	
	private double simpsonRule(double start, double end) {
		double c = (start + end) /2.0 ;
		double h3 = Math.abs(end-start) / 6.0 ;
		return h3*(func.function(start) + 4.0 * func.function(c) + func.function(end)) ;
	}
	
	private double recursiveSimpson(double start, double end, double eps, double whole) {
		double c = (start + end) / 2.0 ;
		double left = simpsonRule(start, c) ;
		double right = simpsonRule(c, end) ;
		if(Math.abs(left+right-whole)<= 15*eps)
			return left + right + (left+right-whole)/15.0 ;
		return recursiveSimpson(start, c, eps/2.0, left) + recursiveSimpson(c, end, eps/2.0, right) ;
	}
	
	public double integrate(double start, double end) {
		return recursiveSimpson(start, end, errorBound, simpsonRule(start, end)) ;
	}
	
	// for test
	public static void main(String[] args) {
		
		double xMin = 0.0 ;
		double xMax = 10*Math.PI ;
		
		IntegralFunction1D func = t -> Math.sin(Math.PI*t) ;
		
		Timer timer = new Timer() ;
		timer.start();
		
		Integral1DAdaptiveSimpson integral = new Integral1DAdaptiveSimpson(func) ;
		double result = integral.integrate(xMin, xMax) ;
		
		timer.stop();
		System.out.println(result);
		System.out.println(timer);
		
		Timer timer1 = new Timer() ;
		timer1.start();
		
		Integral1D integral1 = new Integral1D(func, xMin, xMax) ;
		double result1 = integral1.getIntegral() ;
		
		timer1.stop();
		System.out.println(result1);
		System.out.println(timer1);
		
	}
	
}

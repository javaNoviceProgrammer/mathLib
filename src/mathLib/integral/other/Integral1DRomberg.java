package mathLib.integral.other;

import flanagan.integration.IntegralFunction;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.sequence.SumFunction;
import mathLib.sequence.Summation;
import mathLib.util.Timer;

public class Integral1DRomberg {

	IntegralFunction func ;
	
	public Integral1DRomberg(
			IntegralFunction1D func
			) {
		this.func = func ;
	}
	
	private double hn(int n, double start, double end) {
		return (end-start)/Math.pow(2, n) ;
	}
	
	private double romberg(int n, int m, double start, double end) {
		if(n < m)
			throw new IllegalArgumentException("n > m is required!") ;
		if(n < 0 || m < 0)
			throw new IllegalArgumentException("orders should be positive!") ;
		if(n==0 && m==0)
			return hn(1, start, end)*(func.function(start) + func.function(end)) ;
		if(m==0) {
			SumFunction s = k -> func.function(start+(2*k-1)*hn(n, start, end)) ;
			Summation sum = new Summation(s) ;
			return 0.5*romberg(n-1, 0, start, end) + hn(n, start, end)*sum.evaluate(1, (int) Math.pow(2, n-1)) ;
		}
		return (Math.pow(4, m)*romberg(n, m-1, start, end)-romberg(n-1, m-1, start, end))/(Math.pow(4, m)-1) ;
	}
	
	public double integrate(double start, double end) {
		int n = (int) (Math.log(end-start)/Math.log(2)) ;
		n += 10 ;
		return romberg(n, 3, start, end) ;
	}
	
	// for test
	public static void main(String[] args) {
		double xMin = 0.0 ;
		double xMax = 10.1*Math.PI ;
		
		IntegralFunction1D func = t -> Math.sin(Math.sqrt(t)) ;
		
		Timer timer = new Timer() ;
		timer.start();
		
		Integral1DAdaptiveSimpson integral = new Integral1DAdaptiveSimpson(func) ;
		integral.setAccuracy(1e-15);
		double result = integral.integrate(xMin, xMax) ;
		
		timer.stop();
		System.out.println("Simpson = " + result);
		System.out.println(timer);
		
		Timer timer1 = new Timer() ;
		timer1.start();
		
		Integral1D integral1 = new Integral1D(func, xMin, xMax) ;
		double result1 = integral1.getIntegral() ;
		
		timer1.stop();
		System.out.println("Gauss quadrature = " + result1);
		System.out.println(timer1);
		
		
		Timer timer2 = new Timer() ;
		timer2.start();
		
		Integral1DRomberg integral2 = new Integral1DRomberg(func) ;
		double result2 = integral2.integrate(xMin, xMax) ;
		
		timer2.stop();
		System.out.println("Romberg = " + result2);
		System.out.println(timer2);
		
	}
	
}

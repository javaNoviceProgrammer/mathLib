package tests;

import mathLib.integral.Integral2D;
import mathLib.integral.intf.IntegralDomain2D;
import mathLib.integral.intf.IntegralFunction2D;
import mathLib.util.Timer;

public class TestIntegral2D {
	public static void main(String[] args) {
		// calculating the area of ellipse ;
		double a = 5 ;
		double b = 3 ;

		IntegralFunction2D func = new IntegralFunction2D() {
			@Override
			public double function(double var1, double var2) {
				return 1; // dx dy --> f(x,y) = 1 
			}
		};
		
		IntegralDomain2D circle = new IntegralDomain2D() {
			
			@Override
			public double getVar2Min(double var1) {
				return -Math.sqrt(b*b*(1-var1*var1/(a*a))) ;
			}
			
			@Override
			public double getVar2Max(double var1) {
				return Math.sqrt(b*b*(1-var1*var1/(a*a))) ;
			}
			
			@Override
			public double getVar1Min() {
				return -a;
			}
			
			@Override
			public double getVar1Max() {
				return a;
			}
		};

		Timer timer = new Timer() ;
		timer.start();
		Integral2D integral = new Integral2D(func, circle) ;
		System.out.println(integral.getIntegral());
		timer.stop();
		System.out.println(timer);
		
	}
}

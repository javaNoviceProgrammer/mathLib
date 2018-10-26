package mathLib.integral.other;

import flanagan.integration.RungeKutta;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.ode.intf.DerivFunction1D;
import mathLib.util.Timer;

public class Integral1DRungeKutta {

	IntegralFunction1D func ;
	double start, end, stepSize ;

	public Integral1DRungeKutta(IntegralFunction1D func, double start, double end) {
		this.func = func ;
		this.start = start ;
		this.end = end ;
		// default step size is 1e-4 of the interval
		this.stepSize = (end-start)/1e4 ;
	}
	
	public Integral1DRungeKutta(IntegralFunction1D func) {
		this.func = func ;

	}

	public void setStepSize(double stepSize) {
		this.stepSize = stepSize ;
	}
	
	public double inetgrate(double start, double end) {
		this.start = start ;
		this.end = end ;
		if(stepSize == 0D)
			this.stepSize = (end-start)/1e4 ;
		
		DerivFunction1D derivFunc = new DerivFunction1D() {

			@Override
			public double deriv(double x, double y) {
				// y' = g0(x,y)
				double g0 = func.function(x) ;
				return g0;
			}
		};

		RungeKutta integral = new RungeKutta() ;
		integral.setInitialValueOfX(start);
		integral.setInitialValueOfY(0.0);
		integral.setFinalValueOfX(end);
		integral.setStepSize(stepSize);

		return integral.fourthOrder(derivFunc) ;
	}

	public double integrate() {
		DerivFunction1D derivFunc = new DerivFunction1D() {

			@Override
			public double deriv(double x, double y) {
				// y' = g0(x,y)
				double g0 = func.function(x) ;
				return g0;
			}
		};

		RungeKutta integral = new RungeKutta() ;
		integral.setInitialValueOfX(start);
		integral.setInitialValueOfY(0.0);
		integral.setFinalValueOfX(end);
		integral.setStepSize(stepSize);

		return integral.fourthOrder(derivFunc) ;
	}

	// for test
	public static void main(String[] args) {
		IntegralFunction1D func = new IntegralFunction1D() {

			@Override
			public double function(double var1) {
				double f = Math.sin(Math.PI*var1) ;
				return f;
			}
		};

		double end = 1000.2 ;

		Timer timer1 = new Timer() ;
		timer1.start();
		Integral1DRungeKutta integral = new Integral1DRungeKutta(func, 0, end) ;
		double i1 = integral.integrate() ;
		timer1.stop();

		System.out.println("RungeKutta result = "+i1);
		System.out.println(timer1);

		Timer timer2 = new Timer() ;
		timer2.start();
		Integral1D integral2 = new Integral1D(func, 0, end) ;
		double i2 = integral2.getIntegral() ;
		timer2.stop();

		System.out.println("Gauss quadrature result = "+i2);
		System.out.println(timer2);
		
		Timer timer3 = new Timer() ;
		timer3.start();
		
		Integral1DAdaptiveSimpson integral3 = new Integral1DAdaptiveSimpson(func) ;
		double i3 = integral3.integrate(0, end) ;
		timer3.stop();
		System.out.println("Adaptive simpson result = "+i3);
		System.out.println(timer3);
	}

}

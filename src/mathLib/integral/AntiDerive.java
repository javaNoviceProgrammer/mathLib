package mathLib.integral;

import flanagan.integration.DerivFunction;
import flanagan.integration.RungeKutta;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.polynom.Polynomial;
import mathLib.util.Timer;

public class AntiDerive {
	
	double start, end, stepSize ; 
	int numPoints ;
	double[] x, y ;
	IntegralFunction1D derivFunc ;
	RungeKutta rk ;
	MatlabChart fig ;
	
	enum Mode {
		RungeKutta,
		CashKarp,
		Fehlberg
	}
	
	public AntiDerive(IntegralFunction1D derivFunc) {
		this.derivFunc = derivFunc ;
		rk = new RungeKutta() ;
	}
	
	public void calculate(Mode mode, double start, double end, double stepSize, int numPoints) {
		this.start = start ;
		this.end = end ;
		this.stepSize = stepSize ;
		
		rk.setInitialValueOfX(start);
		rk.setFinalValueOfX(end);
		rk.setInitialValueOfY(derivFunc.function(start));
		rk.setStepSize(stepSize);
		DerivFunction dfunc = new DerivFunction() {
			@Override
			public double deriv(double x, double y) {
				return derivFunc.function(x);
			}
		} ;
		
		double[][] sol = null ;
		
		switch (mode) {
		case RungeKutta:
			sol = rk.fourthOrder(dfunc, numPoints) ;
			break;
		case CashKarp:
			sol = rk.cashKarp(dfunc, numPoints) ;
			break ;
		case Fehlberg:
			sol = rk.fehlberg(dfunc, numPoints) ;
			break ;
		default:
			break;
		}
		
		x = sol[0] ;
		y = sol[1] ;
	}
	
	public double[] getVarValues() {
		return x ;
	}
	
	public double[] getFuncValues() {
		return y ;
	}
	
	public void plot() {
		fig = new MatlabChart() ;
		fig.plot(x,y) ;
		fig.RenderPlot();
		fig.xlabel("Variable");
		fig.ylabel("Function");
//		fig.markerON();
		fig.setFigLineWidth(0, 1f);
		fig.run(true);
	}
	
	//*********** static functions **************
	
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
	
	
	// for test
	public static void main(String[] args) {
		IntegralFunction1D func = t -> Math.sin(2*t) ;
		
		AntiDerive ant = new AntiDerive(func) ;
		Timer timer = new Timer() ;
		timer.start();
		ant.calculate(Mode.RungeKutta, 0, 10*Math.PI, 1e-2, 1000);
		timer.stop();
		System.out.println(timer);
		ant.plot();
		
		// testing with discrete integration
		double[] x = ant.getVarValues() ;
		double[] y = new double[x.length] ;
		Timer timer2 = new Timer() ;
		timer2.start();
		for(int i=0; i<x.length; i++) {
			Integral1D integral = new Integral1D(func, 0, x[i]) ;
			y[i] = integral.getIntegral() ;
		}
		timer2.stop();
		System.out.println(timer2);

		MatlabChart fig = ant.fig ;
		fig.plot(x,y, "r") ;
		fig.RenderPlot();
		fig.xlabel("Variable");
		fig.ylabel("Function");
		fig.setFigLineWidth(1, 3f);
	}
	
}

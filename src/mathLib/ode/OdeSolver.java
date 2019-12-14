package mathLib.ode;

import mathLib.ode.intf.DerivFunction;
import mathLib.sequence.Sequence;

public class OdeSolver {

	DerivFunction func ;
	double x0 ;
	double y0 ;

	public OdeSolver(DerivFunction func, double x0, double y0) {
		this.func = func ;
		this.x0 = x0 ;
		this.y0 = y0 ;
	}

	public Sequence eulerSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				double y = y0 ;
				double h = (x1-x0)/(double)n ;
				for(int i=0; i<n; i++) {
					y = y + h*func.value(x, y) ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double euler(double x1) {
		return eulerSequence(x1).richardson4().evaluate(100) ;
	}

	public double[] euler(double[] x1) {
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		double[] y = new double[x1.length] ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = euler(x1[i]) ;
			x0 = x1[i] ;
			y0 = y[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}

	public Sequence midpointSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				double y = y0 ;
				double h = (x1-x0)/(double)n ;
				for(int i=0; i<n; i++) {
					y = y + h*func.value(x+0.5*h, y+0.5*h*func.value(x, y)) ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double midpoint(double x1) {
		return midpointSequence(x1).richardson4().evaluate(100) ;
	}

	public double[] midpoint(double[] x1) {
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		double[] y = new double[x1.length] ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = midpoint(x1[i]) ;
			x0 = x1[i] ;
			y0 = y[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}

	public Sequence rungeKuttaSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				double y = y0 ;
				double h = (x1-x0)/(double)n ;
				double k1=0.0, k2=0.0, k3=0.0, k4=0.0 ;
				for(int i=0; i<n; i++) {
					k1 = h*func.value(x, y) ;
					k2 = h*func.value(x+0.5*h, y+0.5*k1) ;
					k3 = h*func.value(x+0.5*h, y+0.5*k2) ;
					k4 = h*func.value(x+h, y+k3) ;
					y = y + (k1 + 2.0*k2 + 2.0*k3 + k4)/6.0 ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double rungeKutta(double x1) {
		return rungeKuttaSequence(x1).evaluate(20) ;
	}

	public double[] rungeKutta(double[] x1) {
		double[] y = new double[x1.length] ;
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = rungeKutta(x1[i]) ;
			x0 = x1[i] ;
			y0 = y[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}

}

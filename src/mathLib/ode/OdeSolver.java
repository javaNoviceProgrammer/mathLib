package mathLib.ode;

import mathLib.ode.intf.DerivFunction1D;
import mathLib.sequence.Sequence;

public class OdeSolver {

	DerivFunction1D func ;
	double x0 ;
	double y0 ;

	public OdeSolver(DerivFunction1D func, double x0, double y0) {
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
		double[] y = new double[x1.length] ;
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = euler(x1[i]) ;
			y0 = y[i] ;
			x0 = x1[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}



}

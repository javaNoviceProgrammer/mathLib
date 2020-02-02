package mathLib.ode.solvers;

import mathLib.root.RealRoot;
import mathLib.root.RealRootFunction;
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

	public void setX0(double x0) {
		this.x0 = x0 ;
	}

	public void setY0(double y0) {
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

	public Sequence fehlbergSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				double y = y0 ;
				double h = (x1-x0)/(double)n ;
				double k1=0.0, k2=0.0, k3=0.0, k4=0.0, k5=0.0, k6=0.0 ;
				for(int i=0; i<n; i++) {
					k1 = func.value(x, y) ;
					k2 = func.value(x+1.0/4.0*h, y+h*(1.0/4.0*k1)) ;
					k3 = func.value(x+3.0/8.0*h, y+h*(3.0/32.0*k1+9.0/32.0*k2)) ;
					k4 = func.value(x+12.0/13.0*h, y+h*(1932.0/2197.0*k1-7200.0/2197.0*k2+7296.0/2197.0*k3)) ;
					k5 = func.value(x+1.0*h, y+h*(439.0/216.0*k1-8.0*k2+3680.0/513.0*k3-845.0/4104.0*k4)) ;
					k6 = func.value(x+1.0/2.0*h, y+h*(-8.0/27.0*k1+2.0*k2-3544.0/2565.0*k3+1859.0/4104.0*k4-11.0/40.0*k5)) ;
					// 5th-order
					y = y + h*(16.0/135.0*k1 + 0.0*k2 + 6656.0/12825.0*k3 +
							28561.0/56430.0*k4 - 9.0/50.0*k5 + 2.0/55.0*k6) ;
					// 4th-order
	//					y = y + h*(25.0/216.0*k1 + 0.0*k2 + 1408.0/2565.0*k3 +
	//							2197.0/4104.0*k4 - 1.0/5.0*k5 + 0.0*k6) ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double fehlberg(double x1) {
		return fehlbergSequence(x1).evaluate(20) ;
	}

	public double[] fehlberg(double[] x1) {
		double[] y = new double[x1.length] ;
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = fehlberg(x1[i]) ;
			x0 = x1[i] ;
			y0 = y[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}

	//********* implicit methods ****************

	public Sequence eulerImplicitSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				double y = y0 ;
				double h = (x1-x0)/(double)n ;
				RealRootFunction rootEquation = null ;
				RealRoot rootSolver = null ;
				for(int i=0; i<n; i++) {
					// solve for z : z - h f(x,z) = y
					double xcopy = x ;
					double ycopy = y ;
					rootEquation = z -> z-h*func.value(xcopy, z)-ycopy ;
					rootSolver = new RealRoot(rootEquation) ;
					y = rootSolver.newton(y, 100) ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double eulerImplicit(double x1) {
		return eulerImplicitSequence(x1).richardson4().evaluate(100) ;
	}

	public double[] eulerImplicit(double[] x1) {
		double x0Copy = x0 ;
		double y0Copy = y0 ;
		double[] y = new double[x1.length] ;
		for(int i=0, len=x1.length; i<len; i++) {
			y[i] = eulerImplicit(x1[i]) ;
			x0 = x1[i] ;
			y0 = y[i] ;
		}
		x0 = x0Copy ;
		y0 = y0Copy ;
		return y ;
	}


}

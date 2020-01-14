package mathLib.ode.solvers;

import static mathLib.numbers.Complex.j;
import mathLib.numbers.Complex;
import mathLib.sequence.ArraySequence;

public class OdeSolverComplex {

	DerivFunctionComplex funcComplex ;
	DerivnFunction funcSystem ;
	OdeSystemSolver odeSystemSolver ;
	double x0 ;
	Complex y0 ;

	public OdeSolverComplex(DerivFunctionComplex func, double x0, Complex y0) {
		this.funcComplex = func ;
		this.x0 = x0 ;
		this.y0 = y0 ;
		// z[0] = realpart, z[1] = imaginary part
		this.funcSystem = (x, z) -> {
			Complex val = func.value(x, z[0]+j*z[1]) ;
			return new double[]{val.re(), val.im()} ;
		} ;
		this.odeSystemSolver = new OdeSystemSolver(funcSystem, x0, y0.re(), y0.im()) ;
	}

	//*********** Euler method ****************

	public ArraySequence eulerSequence(double x1) {
		return odeSystemSolver.eulerSequence(x1) ;
	}

	public double[] euler(double x1) {
		return odeSystemSolver.euler(x1) ;
	}

	public double[][] euler(double[] x1) {
		return odeSystemSolver.euler(x1) ;
	}

	public Complex eulerComplex(double x1) {
		double[] z = odeSystemSolver.euler(x1) ;
		return z[0]+j*z[1] ;
	}

	public Complex[] eulerComplex(double[] x1) {
		double[][] z = odeSystemSolver.euler(x1) ;
		Complex[] y = new Complex[z[0].length] ;
		for(int i=0, len=y.length; i<len; i++)
			y[i] = z[0][i]+j*z[1][i] ;
		return y ;
	}

	//*********** Runge-Kutta (RK4) ***********

	public ArraySequence rungeKuttaSequence(double x1) {
		return odeSystemSolver.rungeKuttaSequence(x1) ;
	}

	public double[] rungeKutta(double x1) {
		return odeSystemSolver.rungeKutta(x1) ;
	}

	public double[][] rungeKutta(double[] x1) {
		return odeSystemSolver.rungeKutta(x1) ;
	}

	public Complex rungeKuttaComplex(double x1) {
		double[] z = odeSystemSolver.rungeKutta(x1) ;
		return z[0]+j*z[1] ;
	}

	public Complex[] rungeKuttaComplex(double[] x1) {
		double[][] z = odeSystemSolver.rungeKutta(x1) ;
		Complex[] y = new Complex[z[0].length] ;
		for(int i=0, len=y.length; i<len; i++)
			y[i] = z[0][i]+j*z[1][i] ;
		return y ;
	}





}

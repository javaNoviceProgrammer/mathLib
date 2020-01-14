package mathLib.ode.solvers;

import static mathLib.numbers.Complex.j;
import mathLib.numbers.Complex;
import mathLib.sequence.ArraySequence;

public class OdeSystemSolverComplex {

	DerivnFunctionComplex funcComplex ;
	DerivnFunction funcSystem ;
	OdeSystemSolver odeSystemSolver ;
	double x0 ;
	Complex[] y0 ;
	double[] y0ReIm ;
	int numEquations ;

	public OdeSystemSolverComplex(DerivnFunctionComplex func, double x0, Complex... y0) {
		this.funcComplex = func ;
		this.x0 = x0 ;
		this.y0 = y0 ;
		this.numEquations = y0.length ;
		this.y0ReIm = new double[2*numEquations] ;
		// initial conditions
		for(int i=0; i<numEquations; i++){
			y0ReIm[2*i] = y0[i].re() ;
			y0ReIm[2*i+1] = y0[i].im() ;
		}
		// z[2*k] = realpart, z[2*k+1] = imaginary part
		// re(y[k]) = ...
		// im(y[k]) = ...
		this.funcSystem = (x, z) -> {
			Complex[] vals = new Complex[numEquations] ;
			Complex[] eqnsComplex = new Complex[numEquations] ;
			double[] eqnsReIm = new double[2*numEquations] ;
			for(int i=0; i<numEquations; i++) {
				// create complex variables
				vals[i] = z[2*i] + j*z[2*i+1] ;
			}
			// create complex equations
			eqnsComplex = func.value(x0, vals) ;
			// decompose into real/imag equations
			for(int i=0; i<numEquations; i++) {
				eqnsReIm[2*i] = eqnsComplex[i].re() ;
				eqnsReIm[2*i+1] = eqnsComplex[i].im() ;
			}
			return eqnsReIm ;
		} ;
		this.odeSystemSolver = new OdeSystemSolver(funcSystem, x0, y0ReIm) ;
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

	public Complex[] eulerComplex(double x1) {
		double[] z = odeSystemSolver.euler(x1) ;
		Complex[] y = new Complex[numEquations] ;
		for(int i=0; i<numEquations; i++)
			y[i] = z[2*i]+j*z[2*i+1] ;
		return y ;
	}

	public Complex[][] eulerComplex(double[] x1) {
		double[][] z = odeSystemSolver.euler(x1) ; // 2*N real equations
		Complex[][] y = new Complex[numEquations][x1.length] ; // N complex equations
		for(int i=0; i<numEquations; i++)
			for(int k=0, len=x1.length; k<len; k++) {
				y[i][k] = z[2*i][k]+j*z[2*i+1][k] ;
			}
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
		return odeSystemSolver.rungeKutta(x1) ; // number of rows = 2N (real, imag)
	}

	public Complex[] rungeKuttaComplex(double x1) {
		double[] z = odeSystemSolver.rungeKutta(x1) ;
		Complex[] y = new Complex[numEquations] ;
		for(int i=0; i<numEquations; i++)
			y[i] = z[2*i]+j*z[2*i+1] ;
		return y ;
	}

	public Complex[][] rungeKuttaComplex(double[] x1) {
		double[][] z = odeSystemSolver.rungeKutta(x1) ; // 2*N real equations
		Complex[][] y = new Complex[numEquations][x1.length] ; // N complex equations
		for(int i=0; i<numEquations; i++)
			for(int k=0, len=x1.length; k<len; k++) {
				y[i][k] = z[2*i][k]+j*z[2*i+1][k] ;
			}
		return y ; // number of rows = N (number of equations)
	}

}

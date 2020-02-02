package mathLib.ode.solvers;

import mathLib.numbers.Complex;

// helper class
public class Ode {

	private Ode() {

	}

	public static OdeSolver getSolver(DerivFunction func, double x0, double y0) {
		return new OdeSolver(func, x0, y0) ;
	}

	public static OdeSystemSolver getSolver(DerivnFunction func, double x0, double... y0) {
		return new OdeSystemSolver(func, x0, y0) ;
	}

	public static OdeSolverComplex getSolver(DerivFunctionComplex func, double x0, Complex y0) {
		return new OdeSolverComplex(func, x0, y0) ;
	}

	public static OdeSystemSolverComplex getSolver(DerivnFunctionComplex func, double x0, Complex... y0) {
		return new OdeSystemSolverComplex(func, x0, y0) ;
	}

}

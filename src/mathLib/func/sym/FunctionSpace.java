package mathLib.func.sym;

/**
 * Utility class for symbolic functions.
 * 	+ define variable as a function object (identity function)
 *  + define composition of functions: sin(f(x)), cos(f(x)), abs(f(x)) ...
 *
 * @author meisam
 *
 */

public final class FunctionSpace {

	// x : variable & constants
	public static final Ax x = new Ax(1.0) ;
	public static final FC PI = new FC(Math.PI) ;

	// sin(f(x))
	public static Function sin(Function func) {
		return new Sin(func) ;
	}

	// abs(f(x))
	public static Function abs(Function func) {
		return new Abs(func) ;
	}

	// cos(f(x))
	public static Function cos(Function func) {
		return new Cos(func) ;
	}

	// FC: constant function
	public static Function fc(double x) {
		return new FC(x) ;
	}

	// cot(f(x))
	public static Function cot(Function func) {
		return new Cot(func) ;
	}

	// exp(f(x))
	public static Function exp(Function func) {
		return new Exp(func) ;
	}

}

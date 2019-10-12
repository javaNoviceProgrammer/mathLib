package mathLib.func.sym;

/**
 * Trigonometric function: f(x) = Sin(x)
 *  + operator overloading is supported
 *
 * @author meisam
 *
 */

public class Sin extends Function {

	// no-arg constructor
	public Sin() {
		compositeFunc = null ;
	}

	// composite constructor
	public Sin(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.sin(x) ; // no arg
		else
			return Math.sin(compositeFunc.getValue(x)) ; // composite constructor
	}

	@Override
	public Function diff() {
		if(compositeFunc == null) // no arg
			return new Cos();
		else
			// implement chain rule
			return compositeFunc.diff() * (new Cos(compositeFunc)) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "sin(x)" ;
		else
			return "sin(" + compositeFunc.toString() + ")";
	}

}

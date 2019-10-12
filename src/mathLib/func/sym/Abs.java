package mathLib.func.sym;

/**
 * f(x) = |x| : absolute value function
 *  + operator overloading is supported
 *  + diff() is defined as signum(x) function
 *
 * @author meisam
 *
 */

public final class Abs extends Function {

	public Abs() {
		compositeFunc = null ;
	}

	public Abs(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.abs(x);
		else
			return Math.abs(compositeFunc.getValue(x)) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "|x|" ;
		else if(compositeFunc instanceof Abs)
			return "|x|" ;
		else if(compositeFunc instanceof FC)
			return Math.abs(compositeFunc.getValue(0.0))+"" ;
		else if(compositeFunc instanceof Ax) {
			double a = ((Ax)compositeFunc).a ;
			return Math.abs(a)+ "*" + "|x|" ;
		}
		else
			return "|" + compositeFunc.toString() + "|" ;

	}

	@Override
	public Function diff() { // discontinuous function: signum(x)
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null)
			return new Signum() ;
		else
			return new Signum(compositeFunc) ;
	}

}

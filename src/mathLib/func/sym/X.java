package mathLib.func.sym;

/**
 * Identity function: f(x) = x
 *
 * @author meisam
 *
 */

public class X extends Function {

	public X() {
		compositeFunc = null ;
	}

	public X(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return x ;
		else
			return compositeFunc.getValue(x) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "x" ;
		else
			return compositeFunc.toString() ;
	}

	@Override
	public Function diff() {
		if(compositeFunc == null)
			return new FC(1.0) ;
		else
			return compositeFunc.diff() ;
	}

}

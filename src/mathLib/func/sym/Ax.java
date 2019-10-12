package mathLib.func.sym;

import mathLib.func.sym.operation.Multiplication;

/**
 * f(x) = a*x
 * 	+ No composition allowed. Use
 * 	+ Operator overloading supported
 *
 * @author meisam
 *
 */

public final class Ax extends Function {

	double a ;

	public Ax(double a) {
		compositeFunc = null ;
		this.a = a ;
	}

	public Ax(double a, Function f){
		compositeFunc = f ;
		this.a = a ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return a*x ;
		else
			return a*compositeFunc.getValue(x) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc == null)
			return new FC(a) ;
		else
			return a*compositeFunc.diff() ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null){
			if(Math.abs(a-1)<1e-10)
				return "x" ;
			else
				return a + "*x" ;
		}
		else {
			return (new Multiplication(compositeFunc, a)).toString() ;
		}
	}

}

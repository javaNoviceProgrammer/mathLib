package mathLib.func.sym;

import static mathLib.func.sym.FunctionSpace.* ;

public final class Log extends Function {

	// no arg constructor
	public Log() {
		compositeFunc = null ;
	}

	// composite constructor
	public Log(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.log(x);
		else
			return Math.log(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null){
			return 1.0/x ;
		}
		else
			return compositeFunc.diff() * 1.0/x ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "log(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "log(" + compositeFunc.toString() + ")";
	}

}

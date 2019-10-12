package mathLib.func.sym;

public final class Log10 extends Function {

	// no arg constructor
	public Log10() {
		compositeFunc = null ;
	}

	// composite constructor
	public Log10(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.log10(x);
		else
			return Math.log10(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null){
			return (new Log()).diff()/Math.log(10.0)  ;
		}
		else
			return (new Log(compositeFunc)).diff()/Math.log(10.0) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "log10(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "log10(" + compositeFunc.toString() + ")";
	}

}

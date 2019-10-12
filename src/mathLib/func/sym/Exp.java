package mathLib.func.sym;

public final class Exp extends Function {

	// no arg constructor
	public Exp() {
		compositeFunc = null ;
	}

	// composite constructor
	public Exp(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.exp(x);
		else
			return Math.exp(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null){
			return this ;
		}
		else
			return compositeFunc.diff() * this ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "exp(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "exp(" + compositeFunc.toString() + ")";
	}

}

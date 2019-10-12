package mathLib.func.sym;

public final class Cot extends Function {

	// no arg constructor
	public Cot() {
		compositeFunc = null ;
	}

	// composite constructor
	public Cot(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return 1.0/Math.tan(x);
		else
			return 1.0/Math.tan(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null){
			return -(1+this*this) ;
		}
		else
			return -compositeFunc.diff() * (1+this*this) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "cot(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "cot(" + compositeFunc.toString() + ")";
	}

}

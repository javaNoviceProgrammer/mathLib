package mathLib.func.sym;

public final class Cos extends Function {

	// no arg constructor
	public Cos() {
		compositeFunc = null ;
	}

	// composite constructor
	public Cos(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.cos(x);
		else
			return Math.cos(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null)
			return -(new Sin()) ;
		else
			return -compositeFunc.diff() * (new Sin(compositeFunc)) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "cos(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "cos(" + compositeFunc.toString() + ")";
	}

}

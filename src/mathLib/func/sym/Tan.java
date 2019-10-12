package mathLib.func.sym;

public final class Tan extends Function {

	// no arg constructor
	public Tan() {
		compositeFunc = null ;
	}

	// composite constructor
	public Tan(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.tan(x);
		else
			return Math.tan(compositeFunc.getValue(x)) ;
	}

	@Override
	public Function diff() {
		if(compositeFunc instanceof FC)
			return new FC(0.0) ;
		if(compositeFunc == null){
			return (1+this*this) ;
		}
		else
			return compositeFunc.diff() * (1+this*this) ;
	}

	@Override
	public String toString() {
		if(compositeFunc == null)
			return "tan(x)" ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return "tan(" + compositeFunc.toString() + ")";
	}

}

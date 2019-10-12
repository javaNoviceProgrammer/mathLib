package mathLib.func.sym;

public final class Pow extends Function {

	double p ;
	Function base ;

	// no arg constructor
	public Pow(Function base, double p) {
		compositeFunc = null ;
		this.p = p ;
		this.base = base ;
	}

	// composite constructor
	public Pow(Function base, double p, Function f) {
		compositeFunc = f ;
		this.p = p ;
		this.base = base ;
	}

	@Override
	public double getValue(double x) {
		if(compositeFunc == null)
			return Math.pow(base.getValue(x), p) ;
		else
			return Math.pow(compositeFunc.getValue(x), p) ;
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
			return String.format("pow(x,%f)", p) ;
		else if(compositeFunc instanceof FC)
			return this.getValue(0.0) + "" ;
		else
			return String.format("pow(%s,%f)", compositeFunc.toString(), p) ;
	}

}

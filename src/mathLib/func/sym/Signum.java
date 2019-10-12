package mathLib.func.sym;

public final class Signum extends Function {

	public Signum() {
		compositeFunc = null ;
	}

	public Signum(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		if(x < 0.0)
			return -1.0 ;
		else if(x > 0.0)
			return 1.0 ;
		else
			return 0.0 ;
	}

	@Override
	public Function diff() {
		if(compositeFunc == null)
			return 2.0*(new Delta()) ;
		else
			return 2.0*(new Delta(compositeFunc)) ;
 	}

}

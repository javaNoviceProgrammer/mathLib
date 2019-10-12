package mathLib.func.sym.operation;

import mathLib.func.sym.FC;
import mathLib.func.sym.Function;

public final class Negation extends Function {

	private Function f ;

	public Negation(Function f) {
		this.f = f ;
	}

	@Override
	public double getValue(double x) {
		return -f.getValue(x) ;
	}

	@Override
	public Function diff() {
		return -f.diff() ;
	}

	@Override
	public String toString() {
		if(f instanceof FC)
			return new FC(-f.getValue(0.0)) ;
		return "(" + "-" + f.toString() + ")" ;
	}

}

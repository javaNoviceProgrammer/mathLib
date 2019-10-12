package mathLib.func.sym.operation;

import mathLib.func.sym.FC;
import mathLib.func.sym.Function;

public final class Subtraction extends Function {

	private Function f1 ,f2 ;

	public Subtraction(Function f1, Function f2) {
		this.f1 = f1 ;
		this.f2 = f2 ;
	}

	public Subtraction(Function f1, double v) {
		this.f1 = f1 ;
		this.f2 = new FC(v) ;
	}

	public Subtraction(double v, Function f2) {
		this.f1 = new FC(v) ;
		this.f2 = f2 ;
	}

	@Override
	public double getValue(double x) {
		return f1.getValue(x) - f2.getValue(x) ;
	}

	@Override
	public Function diff() {
		return f1.diff() - f2.diff() ;
	}

	@Override
	public String toString() {
		// two constant functions
		if(f1 instanceof FC && f2 instanceof FC)
			return new FC(f1.getValue(0.0) - f2.getValue(0.0)) ;
		// 2 + f(x) --> f(x) + 2
		if(f1 instanceof FC && !(f2 instanceof FC))
			return "(" + f2.toString() + "-" + f1.toString() + ")" ;
		// general case
		return "(" + f1.toString() + "-" + f2.toString() + ")" ;
	}

}

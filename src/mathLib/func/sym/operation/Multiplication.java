package mathLib.func.sym.operation;

import mathLib.func.sym.FC;
import mathLib.func.sym.Function;

public final class Multiplication extends Function {

	private Function f1, f2 ;

	public Multiplication(Function f1, Function f2) {
		this.f1 = f1 ;
		this.f2 = f2 ;
	}

	public Multiplication(Function f, double v) { // 2*f(x) --> don't want to have f(x)*2
		this.f1 = f ;
		this.f2 = new FC(v) ;
	}

	public Multiplication(double v, Function f) { // 2*f(x) --> don't want to have f(x)*2
		this.f1 = new FC(v) ;
		this.f2 = f ;
	}

	@Override
	public double getValue(double x) {
		return f1.getValue(x) * f2.getValue(x) ;
	}

	@Override
	public Function diff() {
		return f1.diff() * f2 + f1 * f2.diff() ;
	}

	@Override
	public String toString() {
		if(f1 instanceof FC)
			if(Math.abs(((FC)f1).getValue(0.0)-0.0)<1e-10)
				return "0" ;
		if(f2 instanceof FC)
			if(Math.abs(((FC)f2).getValue(0.0)-0.0)<1e-10)
				return "0" ;
			else
				return "("+ f2.toString() + "*" + f1.toString() + ")" ;
		return "("+f1.toString() + "*" + f2.toString() + ")" ;
	}

}

package mathLib.func.sym.operation;

import mathLib.func.sym.FC;
import mathLib.func.sym.Function;

public final class Division extends Function {

	private Function f1, f2 ;

	public Division(Function f1, Function f2) {
		this.f1 = f1 ;
		this.f2 = f2 ;
	}

	public Division(Function f, double v) { // f(x)/v --> we want: (1/v)*f(x)
		this.f1 = f ;
		this.f2 = new FC(v) ;
	}

	public Division(double v, Function f) { // v/f(x)
		this.f1 = new FC(v) ;
		this.f2 = f ;
	}

	@Override
	public double getValue(double x) {
		return f1.getValue(x) / f2.getValue(x) ;
	}

	@Override
	public Function diff() {
		return (f1.diff() * f2 - f1 * f2.diff())/(f2*f2) ;
	}

	@Override
	public String toString() {
		if(f1 instanceof FC)
			if(Math.abs(((FC)f1).getValue(0.0)-0.0)<1e-10)
				return "0.0" ;
		if(f2 instanceof FC)
				return (new Multiplication(f1, 1.0/f2.getValue(0.0))).toString() ;
		return "("+f1.toString() + "/" + f2.toString() + ")" ;
	}

}

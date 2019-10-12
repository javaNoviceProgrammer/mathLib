package mathLib.func.sym.operation;

import mathLib.func.sym.Ax;
import mathLib.func.sym.FC;
import mathLib.func.sym.Function;
import mathLib.func.sym.Utils;

public final class Addition extends Function {

	private Function f1 ,f2 ;

	public Addition(Function f1, Function f2) {
		this.f1 = f1 ;
		this.f2 = f2 ;
	}

	public Addition(Function f1, double v) {
		this.f1 = f1 ;
		this.f2 = new FC(v) ;
	}

	public Addition(double v, Function f2) {
		this.f1 = new FC(v) ;
		this.f2 = f2 ;
	}

	@Override
	public double getValue(double x) {
		return f1.getValue(x) + f2.getValue(x) ;
	}

	@Override
	public Function diff() {
		return f1.diff() + f2.diff() ;
	}

	@Override
	public String toString() {
		/* first take care of simplifying cases */
		if(f1 instanceof FC && f2 instanceof FC)
			return new FC(f1.getValue(0.0) + f2.getValue(0.0)) ;
		if(f1 instanceof Ax && f2 instanceof Ax){
			double a1 = ((Ax)f1).getValue(1.0) ;
			double a2 = ((Ax)f2).getValue(1.0) ;
			return (new Ax(a1+a2)).toString() ;
		}
		if(f1 instanceof Ax && f2 instanceof FC){
			double c = ((FC)f2).getValue(0.0) ;
			double a = ((Ax)f1).getValue(1.0) ;
			return a + "*x" + Utils.getSign(c) + Math.abs(c) ;
		}
		if(f1 instanceof FC && f2 instanceof Ax){
			double c = ((FC)f1).getValue(0.0) ;
			double a = ((Ax)f2).getValue(1.0) ;
			return a + "*x" + Utils.getSign(c) + Math.abs(c) ;
		}
		// general case
		return "(" + f1.toString() + "+" + f2.toString() + ")" ;
	}


	// test
	public static void main(String[] args) {
		Function f1 = new Ax(2.1) ;
		System.out.println(f1);
		Function f2 = 3*f1 + 2 ;
		System.out.println(f2);
		Function f3 = f1 + f2 ;
		System.out.println(f3);
	}


}

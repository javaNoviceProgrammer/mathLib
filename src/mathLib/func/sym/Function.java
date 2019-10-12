package mathLib.func.sym;

import mathLib.func.sym.operation.Addition;
import mathLib.func.sym.operation.Division;
import mathLib.func.sym.operation.Multiplication;
import mathLib.func.sym.operation.Negation;
import mathLib.func.sym.operation.Subtraction;

/**
 * base function: f(x) [abstract]
 * operator overloading supported
 *
 * @author meisam
 *
 */
public abstract class Function {

	protected Function compositeFunc = null ;

	public abstract double getValue(double x) ;
	public abstract Function diff() ;

	public double[] getValues(double[] x) {
		int n = x.length ;
		double[] values = new double[n] ;
		for(int i=0; i<n; i++)
			values[i] = this.getValue(x[i]) ;
		return values ;
	}

//	public Function compose(Function f) {
//		compositeFunc = f ;
//		return this ;
//	}

	public boolean isConstant() {
		if(this instanceof FC)
			return true ;
		else if(this.diff() instanceof FC){
			double value = ((FC)this).getValue(0.0) ;
			if(Math.abs(value)<1e-10)
				return true ;
		}
		return false ;
	}

	@Override
	public String toString() {
		return "not implemented!" ;
	}

	//********** operator overloading ***************

	// addition
	public Function add(double v) { // this function + v
		return new Addition(this, v) ;
	}

	public Function addRev(double v) {
		return new Addition(v, this) ;
	}

	public Function add(Function v) {
		return new Addition(this, v) ;
	}

	public Function addRev(Function v) {
		return new Addition(v, this) ;
	}

	// subtraction
	public Function subtract(double v) { // this - v
		return new Subtraction(this, v) ;
	}

	public Function subtractRev(double v) {
		return new Subtraction(v, this) ;
	}

	public Function subtract(Function v) {
		return new Subtraction(this, v) ;
	}

	public Function subtractRev(Function v) {
		return new Subtraction(v, this) ;
	}

	// multiplication
	public Function multiply(double v) {
		if(this instanceof Ax) { // a*x
			double a = ((Ax)this).a ;
			return new Ax(a*v) ; // a*x*v --> (a*v)*x
		}
		return new Multiplication(this, v) ;
	}

	public Function multiplyRev(double v) {
		if(this instanceof Ax) {
			double a = ((Ax)this).a ;
			return new Ax(a*v) ;
		}
		return new Multiplication(this, v) ;
	}

	public Function multiply(Function v) {
		return new Multiplication(this, v) ;
	}

	public Function multiplyRev(Function v) {
		return new Multiplication(v, this) ;
	}

	// division
	public Function divide(double v) {
		return new Division(this, v) ;
	}

	public Function divideRev(double v) {
		return new Division(v, this) ;
	}

	public Function divide(Function v) {
		return new Division(this, v) ;
	}

	public Function divideRev(Function v) {
		return new Division(v, this) ;
	}

	// negate
	public Function negate() {
		return new Negation(this) ;
	}

}

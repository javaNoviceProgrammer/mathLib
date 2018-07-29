package mathLib.func;

import mathLib.numbers.Complex;
import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public abstract class AbstractFunc1D {

	public abstract double evaluate(double x) ;
	public abstract Complex evaluate(Complex x) ;

	public abstract AbstractFunc1D diff() ;
	public abstract AbstractFunc1D antiDerive() ;

	public abstract AbstractFunc1D diff(int m) ;
	public abstract AbstractFunc1D antiDerive(int m) ;

	public abstract Polynomial taylor(int degree) ;
	public abstract Polynomial taylor(double x0, int degree) ;

	public abstract ComplexPolynomial taylor(Complex z0, int degree) ;

}

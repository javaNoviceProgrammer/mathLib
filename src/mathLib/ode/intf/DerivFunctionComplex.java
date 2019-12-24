package mathLib.ode.intf;

import mathLib.numbers.Complex;

@FunctionalInterface
public interface DerivFunctionComplex {
	Complex value(double x, Complex y) ; // y' = f(x,y) --> use as lambda expression
}

package mathLib.ode.solvers;

@FunctionalInterface
public interface DerivFunction {
	double value(double x, double y) ; // y' = f(x,y) --> use as lambda expression
}

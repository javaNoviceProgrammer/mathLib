package mathLib.root;

@FunctionalInterface
public interface RealRootFunction {
	// returns f(x)
	double value(double x) ; // use can use it as lambda expression
}

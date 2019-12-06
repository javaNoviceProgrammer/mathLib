package mathLib.root;

@FunctionalInterface
public interface RealRootDerivFunction {
	// values[0] = f(x), values[1] = f'(x), values[2] = f''(x), ...
	double[] values(double x) ;
}

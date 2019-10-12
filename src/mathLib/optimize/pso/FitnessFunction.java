package mathLib.optimize.pso;

@FunctionalInterface
public interface FitnessFunction {

	double value(double... args) ; // method: public + abstract --> vararg: value(double[] args) --> value(x1, x2, ...)

}

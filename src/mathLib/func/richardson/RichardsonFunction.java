package mathLib.func.richardson;

@FunctionalInterface
public interface RichardsonFunction {
	double value(double var1, double var2) ; // var1 -> t , var2 -> h
}

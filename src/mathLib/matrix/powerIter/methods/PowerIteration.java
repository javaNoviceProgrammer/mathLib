package mathLib.matrix.powerIter.methods;

import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.numbers.Complex;
/*
 * Interface to use in PowerIteration methods
 *  */
public interface PowerIteration {
	public EigenValueVector solve(PowerIterationMatrix matrix, Complex[] initialEigenVector, double error);
	public void printCommandLine(EigenValueVector ev);
}

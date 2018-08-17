package mathLib.matrix.powerIter.methods;

import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
/*
 * Interface to use in PowerIteration methods
 *  */
public interface PowerIteration {
	public EigenValueVector solve(PowerIterationMatrix matrix, double error);
	public void printCommandLine(EigenValueVector ev);
}

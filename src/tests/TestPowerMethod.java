package tests;


import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.matrix.powerIter.methods.InverseIteration;
import mathLib.matrix.powerIter.methods.PowerIteration;
import mathLib.matrix.powerIter.methods.RegularIteration;
import mathLib.numbers.Complex;
import static mathLib.numbers.Complex.*;

public class TestPowerMethod {
	
	public static void main(String[] args) {
		double[][] d = new double[][] {{1,2}, {-2, 1}} ;
		PowerIterationMatrix mat = new PowerIterationMatrix(d) ;
		PowerIteration iteration = new InverseIteration() ;
		EigenValueVector v = iteration.solve(mat, new Complex[] {1,1}, 1e-15) ;
		iteration.printCommandLine(v);
	}

}

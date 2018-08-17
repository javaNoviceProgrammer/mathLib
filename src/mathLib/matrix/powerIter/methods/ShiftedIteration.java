package mathLib.matrix.powerIter.methods;

import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.numbers.Complex;

public class ShiftedIteration implements PowerIteration {
	
	public Complex estimatedValue ;
	
	public void setEstimatevalue(Complex s) {
		this.estimatedValue = s ;
	}

	public void setEstimatevalue(double s) {
		this.estimatedValue = s ;
	}
	
	@Override
	public EigenValueVector solve(PowerIterationMatrix matrix, double error) {
		/**
		 * Shifted Iteration Algorithm:
		 * 1) A' = A - u * I
		 * 2) Calculates li and Yi using the inverse iteration in A
		 * I is a identity matrix. u is a estimated value for the eigenvalue.
		 */
		
		PowerIterationMatrix identityMatrix = PowerIterationMatrix.identity(matrix.getRowDimension(), matrix.getColumnDimension());
		PowerIterationMatrix changedIdentityMatrix = identityMatrix.times(estimatedValue);
		PowerIterationMatrix displacementMatrix = matrix.minus(changedIdentityMatrix);
		PowerIteration inverse = new InverseIteration();
		EigenValueVector eigenValueVector = inverse.solve(displacementMatrix, error);
		eigenValueVector.eigenValue = eigenValueVector.eigenValue + estimatedValue;
		return eigenValueVector;
	}

	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Shifted Iteration.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");
		for(int i=0; i<ev.eigenVector.length; i++)
			System.out.print(ev.eigenVector[i] + "   ");
		
	}

}

package mathLib.matrix.powerIter.methods;

import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;

/**
 * Regular Iteration find the largest EigenValue.
 * 
 * Algorithm: 
 * 1) Normalize x0 = V/|V| 
 * 2) Yi = M*Xi-1 
 * 3) Xi = Yi/|Yi| 
 * 4) li = * (T(Xi) * M * Xi) / (T(Xi) * Xi Repeat 2 and 4 while li - li-1 < E 
 */
public class RegularIteration implements PowerIteration {

	@Override
	public EigenValueVector solve(PowerIterationMatrix matrix, double error) {

		Complex[] initialEigenVector = new Complex[] {1,1,1} ;
		Complex lastEigenValue;
		Complex newEigenValue = 0;
		int i = 0;

		// Step 1:
		
		PowerIterationMatrix initialMatrixVector = PowerIterationMatrix.vectorToMatrix(initialEigenVector);
		PowerIterationMatrix eigenVector = PowerIterationMatrix.normalize(initialMatrixVector);
		do {
			lastEigenValue = newEigenValue;
			// Step 2:
			PowerIterationMatrix vetorY = matrix.times(eigenVector);
			// Step 3:
			eigenVector = PowerIterationMatrix.normalize(vetorY);
			// Step 4:
			PowerIterationMatrix matrixTranspostaAvetor = eigenVector.transpose();
			Complex avalorNumerador = matrixTranspostaAvetor.times(matrix)
					.times(eigenVector).get(0, 0);
			Complex avalorDenominador = matrixTranspostaAvetor.times(eigenVector)
					.get(0, 0);
			newEigenValue = avalorNumerador / avalorDenominador;
			i++;
		} while (i == 1 || ComplexMath.abs(newEigenValue - lastEigenValue) > error);

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = newEigenValue;
		eigenValueVector.eigenVector = PowerIterationMatrix.matrixToVector(eigenVector);
		return eigenValueVector;
	}

	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Regular Iteration find the largest EigenValue.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");
		for(int i=0; i<ev.eigenVector.length; i++)
			System.out.print(ev.eigenVector[i] + "   ");
		
	}
}

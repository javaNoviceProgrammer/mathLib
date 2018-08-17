package mathLib.matrix.powerIter.methods;

import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.LUDecomposition;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;

/*
 * Returns the smallest Eigenvalue
 */
public class InverseIteration implements PowerIteration {

	@Override
	public EigenValueVector solve(PowerIterationMatrix matrix, double error) {
		/**
		 * Algorithm:
		 * 1) Normalize x0 = V/|V| 
		 * 2) Yi = M^(-1)*Xi-1 => M*yi = Xi-1
		 * 3) Xi = Yi/|Yi| 
		 * 4) li = (T(Xi) * M^(-1) * Xi) / (T(Xi) * Xi
		 * 
		 * Repeat 2 to 4 while li - li-1 < E 
		 * OBS.: Xi is an eigenvector from eigenVector li. Yi is a vector.
		 */
		Complex[] initialEigenVector = new Complex[matrix.getColumnDimension()] ;
		for(int i=0; i<initialEigenVector.length; i++) {
			initialEigenVector[i] = new Complex(1.0, 0.0) ;
		}
		Complex lastEigenValue;
		Complex newEigenValue = 0;
		int i = 0;

		// Step 1:
		PowerIterationMatrix matrizVetorInicial = PowerIterationMatrix.vectorToMatrix(initialEigenVector);
		PowerIterationMatrix evectorX = PowerIterationMatrix.normalize(matrizVetorInicial);
		//Prepare the matrices L and U to be used in 2th step
		LUDecomposition luMatrix = PowerIterationMatrix.getLU(matrix);
		PowerIterationMatrix lowerMatrix = luMatrix.getL();
		PowerIterationMatrix upperMatrix = luMatrix.getU();

		// Step 2:
		/*
		 * M*yi = xi-1 => L*U*yi = xi-1 a) Doing U*yi = Z, solving firstly
		 * L*Z = xi-1 b) with  Z vector, we can solve U*yi = Z
		 */
		PowerIterationMatrix vectorZ = PowerIterationMatrix.solveSubstitution(lowerMatrix, evectorX);
		PowerIterationMatrix vectorY = PowerIterationMatrix.solveRetrosubstitution(upperMatrix, vectorZ);
		do {
			lastEigenValue = newEigenValue;
			// Step 3:
			evectorX = PowerIterationMatrix.normalize(vectorY);

			// Calcule yi+1:
			/*
			 * M*yi = xi-1 => L*U*yi = xi-1 a) if we do U*yi = Z,
			 * we can solve firstly L*Z = xi-1 
			 * With the Z Vector, we can conclude that U*yi = Z
			 * 
			 */
			vectorZ = PowerIterationMatrix.solveSubstitution(lowerMatrix, evectorX);
			vectorY = PowerIterationMatrix.solveRetrosubstitution(upperMatrix, vectorZ);

			// Step 4:
			// li = (T(Xi) * M^(-1) * Xi) / (T(Xi) * Xi => li = (T(Xi) * yi+1) /
			// (T(Xi) * Xi)
			PowerIterationMatrix transposeMatrixEigenVector = evectorX.transpose();
			Complex evalueNumerator = transposeMatrixEigenVector.times(vectorY).get(
					0, 0);
			Complex avalorDenominador = transposeMatrixEigenVector.times(evectorX)
					.get(0, 0);
			newEigenValue = evalueNumerator / avalorDenominador;
			i++;
		} while (i == 1 || Math.abs(ComplexMath.abs(newEigenValue) - ComplexMath.abs(lastEigenValue)) > error);

		EigenValueVector eigenValueVector = new EigenValueVector();
		eigenValueVector.eigenValue = 1.0 / newEigenValue;
		eigenValueVector.eigenVector = PowerIterationMatrix.matrixToVector(evectorX);
		return eigenValueVector;
	}

	@Override
	public void printCommandLine(EigenValueVector ev) {
		System.out.println("Inverse Iteration find the smallest EigenValue.");
		System.out.println("Eigen Value:" + ev.eigenValue);
		System.out.println("Eigen Vector:");		
		for(int i=0; i<ev.eigenVector.length; i++)
			System.out.print(ev.eigenVector[i] + "   ");
	}

}

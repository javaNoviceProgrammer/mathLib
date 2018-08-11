package mathLib.util;

import mathLib.matrix.ComplexMatrix;
import mathLib.numbers.Complex;

public class ArrayUtils {

	public static Complex[][] toComplexArray(double[][] data){
		int M = data.length ;
		int N = data[0].length ;
		Complex[][] dataComplex = new Complex[M][N] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<N; i++) {
				dataComplex[i][j] = new Complex(data[i][j], 0.0) ;
			}
		}
		return dataComplex ;
	}

	// for test
	public static void main(String[] args) {
		double[][] d = {{1.0,2.0,3.0}, {-2.1, 7.0, 0.0}, {-11.0, -10.0, -9.0}} ;
		new ComplexMatrix(d).show();
	}

}

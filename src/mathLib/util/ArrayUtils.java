package mathLib.util;

import mathLib.matrix.Matrix;
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
	
	public static double[][] reShape(double[] arg, int row, int column) {
		int nx = row ;
		int ny = column ;
		if(nx*ny != arg.length)
			throw new IllegalArgumentException("Dimensions don't agree!") ;
		double[][] var0 = new double[nx][ny] ;
		for(int i=0; i<nx; i++) {
			for(int j=0; j<ny; j++) {
				var0[i][j] = arg[i*ny+j] ;
			}
		}
		return var0 ;
	}
	
	
	// for test
	public static void main(String[] args) {
		double[] arg = MathUtils.linspace(0, 1, 10) ;
		Matrix mat = reShape(arg, 10, 1) ;
		System.out.println(mat);
	}

}

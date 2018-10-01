package tests;

import mathLib.matrix.Matrix;

public class Piloss2x2 {

	int radix = 2 ;
	int k = 4 ;
	int configCount = 0 ;

	Matrix crossing ;
	Matrix[] columns ;
	Matrix cross, bar ;
	Matrix input ;
	Matrix[][] sw ;

	public void initialize() {
		double[][] dataCrossing = new double[k][k] ;
		for(int i=0; i<k; i++) {
			if(i%2==0) {
				int m = Math.max(i-1, 0) ;
				dataCrossing[i][m] = 1 ;
			}
			else {
				int m = Math.min(i+1, k-1) ;
				dataCrossing[i][m] = 1 ;
			}

		}

		crossing = new Matrix(dataCrossing) ;
		columns = new Matrix[radix] ;
		for(int i=0; i<radix; i++) {
			columns[i] = new Matrix(k, k) ;
		}
		cross = new Matrix(new double[][]{{0, 1}, {1, 0}}) ;
		bar = new Matrix(new double[][]{{1, 0}, {0, 1}}) ;
		input = new Matrix(k, 1) ;
		for(int i=0; i<k; i++) {
			if(i%4 == 0 || i%4 == 3)
				input.setElement(i, 0, i+1);
		}

		for(int i=0; i<radix; i++)
			for(int j=0; j<radix; j++)
				sw[i][j] = new Matrix(2, 2) ;


	}

	public Piloss2x2(int radix) {
		this.radix = radix ;
		this.k = 2*radix ;
		this.sw = new Matrix[radix][radix] ;

		initialize();
	}

	public void setRadix(int radix) {
		this.radix = radix ;
		this.k = 2*radix ;
	}

	public int getRadix() {
		return this.radix;
	}

	public void findMapping() {
		if(radix == 0)
			throw new IllegalArgumentException("Set the radix first!") ;
	}

	public boolean isValidOutput(Matrix mat) {
		for(int i=0; i<k; i++) {
			if(i%4 == 0 || i%4 == 3)
				if(mat.getElement(i, 0) != 0)
					return false ;
		}
		return true ;
	}

	public Matrix getColumn(int m) {
		Matrix[] states = new Matrix[radix] ;
		Matrix column = new Matrix(k, k) ;
		for(int i=0; i<radix; i++) {
			column.setBlock(2*i, 2*i, bar);
		}
		return column ;
	}

	public static void main(String[] args) {
		Piloss2x2 piloss = new Piloss2x2(2) ;
		System.out.println(piloss.crossing);
		System.out.println(piloss.input);
		System.out.println(piloss.isValidOutput(piloss.input));
		System.out.println(piloss.getColumn(0));

	}


}

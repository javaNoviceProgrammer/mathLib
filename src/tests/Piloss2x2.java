package tests;

import com.sun.org.apache.bcel.internal.generic.NEW;

import mathLib.matrix.Matrix;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;

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
	
	int c = 0 ;

	public void findMapping() {
		if(radix == 0)
			throw new IllegalArgumentException("Set the radix first!") ;
		
		int max = (int) Math.pow(2, radix*radix) ;
		for(int i=0; i<max; i++) {
			setSwitchConfig(i);
			System.out.println(getSwitchConfig());
			System.out.println(getRoutingMatrix()*input);
//			if(isValidOutput(getRoutingMatrix())) {
//				System.out.println(getSwitchConfig());
//				System.out.println(input);
//				System.out.println(getRoutingMatrix());
//			}
				
		}

	}
	
	public Matrix getRoutingMatrix() {
		Matrix mat = Matrix.identity(k) ;
		for(int i=0; i<radix-1; i++)
			mat = crossing*getColumn(i)*mat ;
//		return mat*input ;
		return mat ;
	}
	
	public void setSwitchConfig(int p) {
		String st = Integer.toBinaryString(p) ;
		int[] conf = new int[radix*radix] ;
		for(int i=0; i<st.length(); i++)
			if(st.charAt(st.length()-i-1) == '1')
				conf[i] = 1 ;

		int[][] reshape = ArrayUtils.reshapeRow(conf, radix, radix) ;
		for(int i=0; i<radix; i++)
			for(int j=0; j<radix; j++)
				if(reshape[i][j]==1)
					sw[i][j]= cross ;
				else
					sw[i][j] = bar ;
	}
	
	public String getSwitchConfig() {
		StringBuilder sb = new StringBuilder() ;
		for(int j=0; j<radix-1; j++) {
			for(int i=0; i<radix; i++) {
				if(sw[i][j].equals(cross))
					sb.append("C") ;
				else
					sb.append("B") ;
			}
			sb.append("|") ;
		}
		
		for(int i=0; i<radix; i++) {
			if(sw[i][radix-1].equals(cross))
				sb.append("C") ;
			else
				sb.append("B") ;
		}
		
		return sb ;

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
		Matrix column = new Matrix(k, k) ;
		for(int i=0; i<radix; i++) {
			column.setBlock(2*i, 2*i, sw[i][m]);
		}
		return column ;
	}

	public static void main(String[] args) {
		Piloss2x2 piloss = new Piloss2x2(2) ;
		piloss.findMapping();

	}


}

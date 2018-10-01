package tests;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import flanagan.io.FileOutput;
import mathLib.matrix.Matrix;
import mathLib.util.ArrayUtils;

public class Piloss {

	int radix ;
	int k ;
	int configCount = 0 ;

	Matrix crossing ;
	Matrix[] columns ;
	Matrix cross, bar ;
	Matrix input ;
	Matrix[][] sw ;
	
	JProgressBar progressBar ;

	ArrayList<String> allConfigs ;
	
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

	public Piloss(int radix) {
		this.radix = radix ;
		this.k = 2*radix ;
		this.sw = new Matrix[radix][radix] ;
		this.allConfigs = new ArrayList<>() ;
		initialize();
		
		this.progressBar = new JProgressBar() ;
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
		
		int max = (int) Math.pow(2, radix*radix) ;
		
		progressBar.setMaximum(max-1);
		progressBar.setMinimum(0);
//		showProgress();
		
		for(int i=0; i<max; i++) {
			
			setSwitchConfig(i);
			
			progressBar.setValue(i);

			if(isValidOutput(getRoutingMatrix())) {
				
				configCount++ ;
				
				allConfigs.add(getSwitchConfig()) ;
				
			}		
		}

	}
	
	public Matrix getRoutingMatrix() {
		Matrix mat = Matrix.identity(k) ;
		for(int i=0; i<radix-1; i++)
			mat = crossing*getColumn(i)*mat ;
		mat = getColumn(radix-1) * mat ;
		return mat*input ;
//		return mat ;
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
	
	public int getNumOfConfigs() {
		return configCount ;
	}
	
	public void saveToFile(String fileName) {
		FileOutput fo = new FileOutput(fileName, 'w') ;
		fo.println("Total number of valid configurations: " + getNumOfConfigs());
		Iterator<String> it = allConfigs.iterator() ;
		while(it.hasNext())
			fo.println(it.next());
		fo.close();
	}
	
	public void showProgress() {
		JFrame frame = new JFrame("Progress") ;
		frame.add(progressBar) ;
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		Piloss piloss = new Piloss(2) ;
		piloss.findMapping();
//		piloss.saveToFile("piloss8x8.txt");

	}


}

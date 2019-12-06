package mathLib.ode;

import java.util.Arrays;

import mathLib.arrays.NdArray;
import mathLib.ode.intf.DerivnFunction1D;
import mathLib.sequence.ArraySequence;


public class OdeSystemSolver {

	DerivnFunction1D func ;
	double x0 ;
	double[] y0 ;
	int numEquation ;

	public OdeSystemSolver(DerivnFunction1D func, double x0, double... y0) {
		this.func = func ;
		this.x0 = x0 ;
		this.y0 = y0 ;
		this.numEquation = y0.length ;
	}

	public ArraySequence eulerSequence(double x1) {
		return n -> {
			if(n==0)
				return new NdArray(y0) ;
			else {
				double x = x0 ;
				NdArray y = new NdArray(y0) ;
				NdArray funcVals ;
				double h = (x1-x0)/(double)n ;
				for(int i=0; i<n; i++) {
					funcVals = new NdArray(func.values(x, y.array())) ;
					y = y + h * funcVals ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double[] euler(double x1) {
		return eulerSequence(x1).richardson4().evaluate(10).array() ;
	}

	public double[][] euler(double[] x1) {
		double x0Copy = x0 ;
		double[] y0Copy = Arrays.copyOf(y0, numEquation) ;
		double[][] y = new double[numEquation][x1.length] ;
		double[] y0alis = new double[numEquation] ;
		for(int i=0, len=x1.length; i<len; i++) {
			y0alis = euler(x1[i]) ;
			for(int k=0; k<numEquation; k++)
				y[k][i] = y0alis[k] ;
			x0 = x1[i] ;
			y0 = Arrays.copyOf(y0alis, numEquation) ;
		}
		x0 = x0Copy ;
		y0 = Arrays.copyOf(y0Copy, numEquation) ;
		return y ;
	}

}

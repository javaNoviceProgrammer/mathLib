package mathLib.ode;

import java.util.Arrays;

import mathLib.arrays.NdArray;
import mathLib.ode.intf.DerivnFunction;
import mathLib.sequence.ArraySequence;


public class OdeSystemSolver {

	DerivnFunction func ;
	double x0 ;
	double[] y0 ;
	int numEquation ;

	public OdeSystemSolver(DerivnFunction func, double x0, double... y0) {
		this.func = func ;
		this.x0 = x0 ;
		this.y0 = y0 ;
		this.numEquation = y0.length ;
	}

	public ArraySequence eulerSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				NdArray y = y0 ;
				NdArray funcVals ;
				double h = (x1-x0)/(double)n ;
				for(int i=0; i<n; i++) {
					funcVals = func.values(x, y.array()) ;
					y = y + h * funcVals ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double[] euler(double x1) {
		return eulerSequence(x1).richardson4().evaluate(15).array() ;
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

	public ArraySequence rungeKuttaSequence(double x1) {
		return n -> {
			if(n==0)
				return y0 ;
			else {
				double x = x0 ;
				NdArray y = y0 ;
				double h = (x1-x0)/(double)n ;
				NdArray k1, k2, k3, k4 ;
				for(int i=0; i<n; i++) {
					k1 = h*new NdArray(func.values(x, y.array())) ;
					k2 = h*new NdArray(func.values(x+0.5*h, (y+0.5*k1).array())) ;
					k3 = h*new NdArray(func.values(x+0.5*h, (y+0.5*k2).array())) ;
					k4 = h*new NdArray(func.values(x+h, (y+k3).array())) ;
					y = y + (k1 + 2.0*k2 + 2.0*k3 + k4)/6.0 ;
					x = x + h ;
				}
				return y ;
			}
		} ;
	}

	public double[] rungeKutta(double x1) {
		return rungeKuttaSequence(x1).evaluate(20).array() ;
	}

	public double[][] rungeKutta(double[] x1) {
		double x0Copy = x0 ;
		double[] y0Copy = Arrays.copyOf(y0, numEquation) ;
		double[][] y = new double[numEquation][x1.length] ;
		double[] y0alis = new double[numEquation] ;
		for(int i=0, len=x1.length; i<len; i++) {
			y0alis = rungeKutta(x1[i]) ;
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

package mathLib.sequence.special;

import plotter.chart.MatlabChart;
import static mathLib.util.MathUtils.* ;

import java.awt.Color;

import mathLib.sequence.SumFunction;
import mathLib.sequence.Summation;

public class HarmonicNumber {

	public static double harmonic (int n) {
		if(n<1)
			throw new IllegalArgumentException("argument must be greater than or equal to 1") ;

		SumFunction func = new SumFunction() {
			@Override
			public double value(int k) {
				return 1.0/k;
			}
		};

		Summation sum = new Summation(func) ;
		return sum.evaluate(1, n) ;
	}

	public static double alternativeHamonic (int n) {
		if(n<1)
			throw new IllegalArgumentException("argument must be greater than or equal to 1") ;

		SumFunction func = new SumFunction() {
			@Override
			public double value(int k) {
				return (1.0/k)*minusOnePower(k+1);
			}
		};

		Summation sum = new Summation(func) ;
		return sum.evaluate(1, n) ;
	}

	// for test
	public static void main(String[] args) {
		MatlabChart fig = new MatlabChart() ;
		double[] x = linspace(1, 100, 1.0) ;
		double[] y = new double[x.length] ;
		for(int i=0; i<x.length; i++) {
			y[i] = alternativeHamonic((int) x[i]) ;
		}
		fig.plot(x, y);
		fig.RenderPlot();
		fig.setAllColors(Color.RED);
		fig.markerON();
		fig.run(true);
	}

}

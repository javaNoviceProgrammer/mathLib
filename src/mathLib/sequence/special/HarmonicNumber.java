package mathLib.sequence.special;

import static mathLib.util.MathUtils.linspace;
import java.awt.Color;
import mathLib.plot.MatlabChart;
import mathLib.sequence.Sequence;
import mathLib.sequence.Series;

public class HarmonicNumber {

	public static double harmonic (int n) {
		if(n<1)
			throw new IllegalArgumentException("argument must be greater than or equal to 1") ;

		Sequence func = k -> 1.0/k ;
		Series series = new Series(func) ;
		return series.sum(1, n) ;
	}

	public static double alternativeHamonic (int n) {
		if(n<1)
			throw new IllegalArgumentException("argument must be greater than or equal to 1") ;

		Sequence func = k -> (1.0/k)*((k+1)%2==0 ? 1 : -1) ; // (-1)^(k+1)
		Series series = new Series(func) ;
		return series.sum(1, n) ;
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
		fig.renderPlot();
		fig.setAllColors(Color.RED);
		fig.markerON();
		fig.run(true);
	}

}

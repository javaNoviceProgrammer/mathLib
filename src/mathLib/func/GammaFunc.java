package mathLib.func;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.Gamma;

import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class GammaFunc  {

	public static double logGamma(double x) {
		return Gamma.logGamma(x) ;
	}

	public static double regularizedGammaP(double a, double x) {
		return Gamma.regularizedGammaP(a, x) ;
	}

	public static double regularizedGammaP(double a, double x, double epsilon, int maxIterations) {
		return Gamma.regularizedGammaP(a, x, epsilon, maxIterations) ;
	}

	public static double regularizedGammaQ(double a, double x) {
		return Gamma.regularizedGammaQ(a, x) ;
	}

	public static double regularizedGammaQ(double a, double x, double epsilon, int maxIterations) {
		return Gamma.regularizedGammaQ(a, x, epsilon, maxIterations) ;
	}

	public static double digamma(double x) {
		return Gamma.digamma(x) ;
	}

	public static double trigamma(double x) {
		return Gamma.trigamma(x) ;
	}

	public static double lanczos(double x) {
		return Gamma.lanczos(x) ;
	}

	public static double invGamma1pm1(double x) {
		return Gamma.invGamma1pm1(x) ;
	}

	public static double logGamma1p(double x) throws NumberIsTooSmallException, NumberIsTooLargeException {
		return Gamma.logGamma1p(x) ;
	}

	public static double gamma(double x) {
		return Gamma.gamma(x) ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(-5, 5, 1000) ;
		double[] y = new double[x.length] ;
		for(int i=0; i<x.length; i++)
			y[i] = gamma(x[i]) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y);
		fig.RenderPlot();
		fig.markerON();
		fig.run(true);
	}

}

package mathLib.integral;

/**
 * This is a class designed to calculate the numerical
 * integration of a function (called integral function)
 * with adaptive accuracy.
 *
 * The default error bound is 1% of the convergence, but
 * can be set in the constructor as well.
 */

import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import mathLib.util.MathUtils;

public class Integral1D {

	int numIntervals = 10 ; // the entire integral region is divided into this number of subintervals
	int numPoints = 5 ; // each sub interval is divided into this number of pieces
	double x_start, x_end ;
	double errorBound = 1e-12 ; // accuracy to the number of digits
	IntegralFunction func ;
	int numIterations = 0 ;
	int maxNumIterations = 50 ; // maximum number of iterations

	public void setNumIntervals(int M){
		numIntervals = M ;
	}

	public void setNumPoints(int N){
		numPoints = N ;
	}

	public void setErrorBound(double error){
		errorBound = error ;
	}

	public void setMaximumNumberOfIterations(int maxNumIterations){
		this.maxNumIterations = maxNumIterations ;
	}

	// constructor with integral function and start and end of integral
	public Integral1D(
			IntegralFunction func,
			double x_start,
			double x_end
			){
		this.func = func ;
		this.x_start = x_start ;
		this.x_end = x_end ;
	}

	// we set the integration adaptive by setting an upper bound on the error
	public double getIntegral(){
		double result_final = 0 ;
		double result_temp = getFirstGuess() ;
		double[] X ;
		while(Math.abs(result_final-result_temp) >= Math.abs(errorBound*result_final) && numIntervals < 10*maxNumIterations+1){
			result_final = result_temp ;
			result_temp = 0 ;
			X = MathUtils.linspace(x_start, x_end, numIntervals+1) ;
			for(int i=0; i<numIntervals; i++){
				result_temp += getInvervalIntegral(X[i], X[i+1]) ;
			}
			numIntervals += 10 ;
			numIterations++ ;
		}

		return result_final ;
	}

	private double getFirstGuess(){
		double firstGuess = 0 ;
		int numIntervalsCoarse = 5 ;
		double[] X = MathUtils.linspace(x_start, x_end, numIntervalsCoarse+1) ;
		for(int i=0; i<numIntervalsCoarse; i++){
			firstGuess += getInvervalIntegral(X[i], X[i+1]) ;
		}
		return firstGuess ;
	}

	private double getInvervalIntegral(double x_start, double x_end){

		Integration intervalIntegral = new Integration() ;
		intervalIntegral.setIntegrationFunction(func);
		intervalIntegral.setLimits(x_start, x_end);

		return intervalIntegral.gaussQuad(numPoints) ;
	}

//	private double getInvervalIntegral(double x_start, double x_end){
//
//		GaussLobattoQuadrature intervalIntegral = new GaussLobattoQuadrature(func, x_start, x_end) ;
//		return intervalIntegral.getIntegral() ;
//	}

	public int getNumberOfIterations(){
		return numIterations ;
	}

}

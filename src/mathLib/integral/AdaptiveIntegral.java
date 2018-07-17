package mathLib.integral;

/*************************************************
 * This is a class designed to calculate the numerical
 * integration of a function (called integral function)
 * with adaptive accuracy.
 *
 * The default error bound is 1% of the convergence, but
 * can be set in the constructor as well.
 *************************************************/

import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import mathLib.utils.MoreMath;

public class AdaptiveIntegral {

	int numIntervals = 10 ; // the entire integral region is divided into this number of subintervals
	int numPoints = 5 ; // each sub interval is divided into this number of pieces
	double x_start, x_end ;
	double errorBound = 1e-9 ; // accuracy to the number of digits
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
	public AdaptiveIntegral(
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
			X = MoreMath.linspace(x_start, x_end, numIntervals+1) ;
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
		double[] X = MoreMath.linspace(x_start, x_end, numIntervalsCoarse+1) ;
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

	public int getNumberOfIterations(){
		return numIterations ;
	}


	// *************** For test
//	public static void main(String[] args) {
//		IntegralFunction func = new IntegralFunction(){
//			@Override
//			public double function(double x) {
//				double y = x*x/Math.cosh(x*x) ;
//				return y;
//			}
//		} ;
//
//		double x0 = -1 ;
//		double x1 = 1 ;
//		AdaptiveIntegral integral = new AdaptiveIntegral(func, x0, x1) ;
//		System.out.println(integral.getIntegral());
//		System.out.println("number of iterations: " + integral.getNumberOfIterations());
//
//		double[] x = MoreMath.linspace(0, 2*Math.PI, 1000) ;
//		IntegralFunction func1 = new IntegralFunction(){
//			@Override
//			public double function(double r) {
//				return Math.cos(r*r*r);
//			}
//	} ;
//	double[] y = new double[x.length] ;
//	for(int i=0; i<y.length; i++){
//		AdaptiveIntegral aint = new AdaptiveIntegral(func1, 0, x[i]) ;
//		y[i] = aint.getIntegral() ;
//		System.out.println(aint.numIterations);
//	}
//	MatlabChart fig = new MatlabChart() ;
//	fig.plot(x, MoreMath.Arrays.Functions.cos(MoreMath.Arrays.Functions.pow(x, 3)), "r",1f, "func(x)");
//	fig.plot(x, y, "b", 1f, "int(func(x))");
//	fig.RenderPlot();
//	fig.legendON();
//	fig.legendOFF();
//	fig.run(true);
//
//	}
	// ***************

}

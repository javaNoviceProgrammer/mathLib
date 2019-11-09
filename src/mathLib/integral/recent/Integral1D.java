package mathLib.integral.recent;

import mathLib.integral.intf.IntegralFunction1D;
import mathLib.polynom.Polynomial;
import mathLib.polynom.special.LegendrePolynom;

public class Integral1D {

	IntegralFunction1D func ;

	public Integral1D(IntegralFunction1D func) {
		this.func = func ; // avoid shadowing of class variable
	}

//	public Integral1D(Function<Double, Double> func1d) {
//		// auto boxing & auto unboxing: double Double --> compiler is smart enough to handle it
//		this.func = t -> func1d.apply(t) ;
//	}

	// method1: Rectangle + uniform partitioning + forward summation
	public double forwardRectangle(double start, double end, int terms) {
		double delta = (end-start)/terms ;
		double sum = 0.0 ;
		for(int i=0; i<terms; i++) {
			sum += delta * func.function(start + i*delta) ; // evaluation at the beginning of the interval
		}
		return sum ;
	}

	public double backwardRectangle(double start, double end, int terms) {
		double delta = (end-start)/terms ;
		double sum = 0.0 ;
		for(int i=0; i<terms; i++) {
			sum += delta * func.function(start + (i+1)*delta) ; // evaluation at the end of interval
		}
		return sum ;
	}

	public double centerRectangle(double start, double end, int terms) {
		double delta = (end-start)/terms ;
		double sum = 0.0 ;
		for(int i=0; i<terms; i++) {
			sum += delta * func.function(start + (i+0.5)*delta) ; // evaluation at the end of interval
		}
		return sum ;
	}

	public double trapezoid(double start, double end, int terms) {
		double delta = (end-start)/terms ; // uniform partitioning
		double sum = 0.0 ;
		double[] funcVals = new double[terms+1] ;
		for(int i=0; i<terms+1; i++) {
			funcVals[i] = func.function(start+i*delta) ;
		}
		for(int i=0; i<terms; i++) {
			sum += delta * (funcVals[i]+funcVals[i+1])/2.0 ;
		}
		return sum ;
	}

	public double simpson(double start, double end, int terms) {
		double delta = (end-start)/terms ; // uniform partitioning
		double sum = 0.0 ;
		double[] funcVals = new double[terms+1] ;
		double[] funcValsMidPoint = new double[terms] ;
		for(int i=0; i<terms+1; i++) {
			funcVals[i] = func.function(start+i*delta) ;
			if(i<terms)
				funcValsMidPoint[i] = func.function(start+(i+0.5)*delta) ;
		}
		for(int i=0; i<terms; i++) {
			sum += delta/6.0 * (funcVals[i]+4*funcValsMidPoint[i]+funcVals[i+1]) ;
		}
		return sum ;
	}

	public double forwardRectangleRecursive(double start, double end) {
		// stop condition
		if(Math.abs(end-start)<1e-6 || Math.abs((end-start)/end)<1e-7 || Math.abs((end-start)/start)<1e-7)
			return func.function(start) * (end-start) ; // forward approximation
		// return condition
		double midPoint = 0.5*(start+end) ;
		// breaking into two smaller integrals
		return forwardRectangleRecursive(start, midPoint) + forwardRectangleRecursive(midPoint, end) ;
	}

	public double backwardRectangleRecursive(double start, double end) {
		// stop condition
		if(Math.abs(end-start)<1e-6 || Math.abs((end-start)/end)<1e-7 || Math.abs((end-start)/start)<1e-7)
			return func.function(end) * (end-start) ;
		// return condition
		double midPoint = 0.5*(start+end) ;
		// breaking into two smaller integrals
		return backwardRectangleRecursive(start, midPoint) + backwardRectangleRecursive(midPoint, end) ;
	}

	public double centerRectangleRecursive(double start, double end) {
		double midPoint = 0.5*(start+end) ;
		// stop condition
		if(Math.abs(end-start)<1e-5 || Math.abs((end-start)/end)<1e-7 || Math.abs((end-start)/start)<1e-7)
			return func.function(midPoint) * (end-start) ;
		// breaking into two smaller integrals
		return centerRectangleRecursive(start, midPoint) + centerRectangleRecursive(midPoint, end) ;
	}

	public double trapezoidRecursive(double start, double end) {
		double midPoint = 0.5*(start+end) ;
		// stop condition
		if(Math.abs(end-start)<1e-5 || Math.abs((end-start)/end)<1e-7 || Math.abs((end-start)/start)<1e-7)
			return (func.function(start)+func.function(end))/2.0 * (end-start) ;
		// breaking into two smaller integrals
		return trapezoidRecursive(start, midPoint) + trapezoidRecursive(midPoint, end) ;
	}

	public double simpsonRecursive(double start, double end) {
		double midPoint = 0.5*(start+end) ;
		// stop condition
		if(Math.abs(end-start)<1e-5 || Math.abs((end-start)/end)<1e-7 || Math.abs((end-start)/start)<1e-7)
			return (func.function(start)+4.0*func.function(midPoint)+func.function(end)) * (end-start)/6.0 ;
		// breaking into two smaller integrals
		return simpsonRecursive(start, midPoint) + simpsonRecursive(midPoint, end) ;
	}

	private double gaussLegendreIntervalMapping(double start, double end, double x) {
		return (end-start)/2.0 * x + (end+start)/2.0 ; // maps from [-1,1] to [start,end]
	}

	private double gaussLegendreInverseIntervalMapping(double start, double end, double x) {
		return 2.0/(end-start) * (x - (end+start)/2.0) ; // maps from [start,end] to [-1,1]
	}

	private IntegralFunction1D gaussLegendreFunctionMapping(double start, double end) {
		return t -> func.function(gaussLegendreIntervalMapping(start, end, t)) ; // t is in [-1,1]
	}

	public double gaussLegendreThreePoints(double start, double end) {
		IntegralFunction1D mappedFunc = gaussLegendreFunctionMapping(start, end) ; // t is in [-1,1]
		// three point Gauss-Legendre quadrature rule
		double[] weights = {8.0/9.0, 5.0/9.0, 5.0/9.0} ;
		double[] points = {0.0, Math.sqrt(3.0/5.0), -Math.sqrt(3.0/5.0)} ;
		double sum = 0.0 ;
		for(int i=0; i<points.length; i++)
			sum += weights[i] * mappedFunc.function(points[i]) ; // integral over [-1,1]
		return sum * (end-start)/2.0 ;
	}

	public double gaussLegendreFivePoints(double start, double end) {
		IntegralFunction1D mappedFunc = gaussLegendreFunctionMapping(start, end) ; // t is in [-1,1]
		// five point Gauss-Legendre quadrature rule
		double[] weights = {128.0/225.0, (322.0+13.0*Math.sqrt(70.0))/900.0, (322.0+13.0*Math.sqrt(70.0))/900.0,
							(322.0-13.0*Math.sqrt(70.0))/900.0, (322.0-13.0*Math.sqrt(70.0))/900.0} ;
		double[] points = {0.0, 1.0/3.0*Math.sqrt(5.0-2.0*Math.sqrt(10.0/7.0)), -1.0/3.0*Math.sqrt(5.0-2.0*Math.sqrt(10.0/7.0)),
							1.0/3.0*Math.sqrt(5.0+2.0*Math.sqrt(10.0/7.0)), -1.0/3.0*Math.sqrt(5.0+2.0*Math.sqrt(10.0/7.0))} ;
		double sum = 0.0 ;
		for(int i=0; i<points.length; i++)
			sum += weights[i] * mappedFunc.function(points[i]) ; // integral over [-1,1]
		return sum * (end-start)/2.0 ;
	}

	public double gaussLegendre(double start, double end, int quadraturePoints) {
		IntegralFunction1D mappedFunc = gaussLegendreFunctionMapping(start, end) ; // t is in [-1,1]
		// create weights and points
		int n = quadraturePoints ;
		double[] weights = new double[n] ;
		double[] points = new double[n] ;
		Polynomial legendre = LegendrePolynom.fromBonnet(n) ;
		legendre = legendre/legendre.evaluate(1.0) ;
		// get the roots
		flanagan.complex.Complex[] croots = legendre.roots() ; // --> all the roots are real
		for(int i=0; i<croots.length; i++)
			points[i] = croots[i].getReal() ;
		// get the weights
		for(int i=0; i<n; i++) {
			double c1 = 1.0-points[i]*points[i] ; // 1-x_i^2
			double c2 = legendre.diff().evaluate(points[i]) ; // P'(x_i)
			double c3 = c2*c2 ;
			weights[i] = 2.0/(c1*c3) ;
		}
		// print weights and points
//		System.out.println("points = " + Arrays.toString(points));
//		System.out.println("weights = " + Arrays.toString(weights));
		// integrate over [-1,1]
		double sum = 0.0 ;
		for(int i=0; i<points.length; i++)
			sum += weights[i] * mappedFunc.function(points[i]) ; // integral over [-1,1]
		// scale to [start,end]
		return sum * (end-start)/2.0 ;
	}

	public double gaussLegenre(double start, double end) {
		// break the interval into smaller sub intervals
		double sum = 0.0 ;
		int numIntervals = 100 ;
		double delta = (end-start)/numIntervals ;
		// in each subinterval --> 5-point gauss-quad method
		for(int i=0; i<numIntervals; i++)
			sum += gaussLegendreFivePoints(start+i*delta, start+(i+1)*delta) ;
		return sum ;
	}

}

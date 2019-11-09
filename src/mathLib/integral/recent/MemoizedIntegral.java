package mathLib.integral.recent;

import java.util.HashMap;
import java.util.Map;

import mathLib.integral.intf.IntegralFunction1D;

public class MemoizedIntegral {

	IntegralFunction1D func1d ;
	double start, end ;
	int numberOfPoints = 100 ;
	Map<Integer, Double> cache ;

	// f = f(x) --> No dependence on t
	public MemoizedIntegral(IntegralFunction1D func, double start, double end) {
		this.func1d = func ;
		this.start = start ;
		this.end = end ;
		cache = new HashMap<Integer, Double>() ; // empty cache
	}

	public void setStart(double start) {
		this.start = start ;
	}

	public void setEnd(double end) {
		this.end = end ;
	}

	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints ;
	}

	public void calculate() {
		// implementing memoization
		double delta = (end-start)/(numberOfPoints-1.0) ;
		for(int i=0; i<numberOfPoints+1; i++) { // step: integrate [start, t2]
			double t1 = start + (i-1)*delta ;
			double t2 = start + i*delta ;
			// result = result[start, t1] + integrate[t1,t2]
			if(cache.containsKey(i-1)){
				double result = cache.get(i-1) + (new Integral1D(func1d)).gaussLegendreFivePoints(t1, t2) ;
				cache.put(i, result) ;
			}
			else{
				double result = 0.0 ;
				cache.put(i, result) ;
			}
		}
	}

	public double[] getResults() {
		double[] results = new double[numberOfPoints] ;
		for(int i=0; i<numberOfPoints; i++)
			results[i] = cache.get(i) ;
		return results ;
	}

	public double[] getPoints() {
		double[] points = new double[numberOfPoints] ;
		double delta = (end-start)/(numberOfPoints-1.0) ;
		for(int i=0; i<numberOfPoints; i++)
			points[i] = start + i*delta ;
		return points ;
	}

}

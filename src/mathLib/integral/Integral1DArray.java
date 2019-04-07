package mathLib.integral;

import java.util.HashMap;
import java.util.Map;

import mathLib.integral.intf.IntegralFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

import static java.lang.Math.*;

public class Integral1DArray {
	
	IntegralFunction1D func ;
	double start ;
	Map<Integer, Double> cache ;
	
	/*
	 * Integrating over an ordered array with cache enables for ultimate speed
	 * 				g(x) = integral from a to x of f(x)dx
	 * 				"a" is the start point
	 */
	
	public Integral1DArray(
			IntegralFunction1D func,
			double start
			) {
		this.func = func ;
		cache = new HashMap<>() ;
		this.start = start ;
	}
	
	public double[] getIntegral(double[] values) {
		int m = values.length ;
		double[] results = new double[m] ;
		for(int i=0; i<m; i++) {
			if(cache.containsKey(i-1)) {
				Integral1D integral1d = new Integral1D(func, values[i-1], values[i]) ;
				double a = integral1d.getIntegral() ;
				results[i] = cache.get(i-1) + a ;
				cache.put(i, results[i]) ;
			}
			else {
				Integral1D integral1d = new Integral1D(func, start, values[i]) ;
				double a = integral1d.getIntegral() ;
				results[i] = a ;
				cache.put(i, a) ;
			}
		}
		return results ;
	}
	
	@Override
	public String toString() {
		return "Integral1DArray cached for " + cache.size() + " terms" ;
	}

	// for test
	public static void main(String[] args) {
		double[] theta = MathUtils.linspace(0, PI*20, 10000) ;
		IntegralFunction1D func = s -> cos(s) ;
		
		Timer timer = new Timer() ;
		timer.start();
		
		Integral1DArray integral = new Integral1DArray(func, 0.0) ;
		double[] results = integral.getIntegral(theta) ;
		
		timer.stop();
		System.out.println(timer);
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(theta, results);
		fig.renderPlot();
		fig.run(true);
		
		System.out.println(integral);
	}

}

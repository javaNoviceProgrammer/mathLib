package mathLib.sequence;

import java.util.HashMap;
import java.util.Map;

import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class Summation {

	SumFunction func ;
	Map<Integer, Double> cache ;
	
	/*
	 * cache is enabled for storing terms of sequence
	 * and avoiding recalculation
	 */

	public Summation(SumFunction func) {
		this.func = func ;
		cache = new HashMap<>() ;
	}
	
	public void resetCatch() {
		cache.clear() ;
	}

	public double evaluate(int start, int end) {
		if(start == end) {
			if(cache.containsKey(start))
				return cache.get(start) ;
			else {
				double a = func.value(start) ;
				cache.put(start, a) ;
				return a ;
			}
		}
		double result = 0.0 ;
		for(int i=start; i<= end; i++){
			if(cache.containsKey(i))
				result += cache.get(i) ;
			else {
				double a = func.value(i) ;
				cache.put(i, a) ;
				result += a ;
			}
		}
		return result ;
	}
	
	@Override
	public String toString() {
		return "Summation cached for " + cache.size() + " terms" ;
	}

	// for test
	public static void main(String[] args) {
		Summation sum = new Summation(k -> 1.0/(k*k)) ;
		System.out.println(sum.evaluate(1, 200));
		System.out.println(sum.evaluate(10, 100));
		System.out.println(sum);
		
		double[] x = MathUtils.linspace(1.0, 5000.0, 1.0) ;
		double[] y = new double[x.length] ;
		for(int i=0; i<x.length; i++)
			y[i] = sum.evaluate(1, (int)x[i]) ;
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.markerON();
		fig.show(true);
		
		System.out.println(y[y.length-1]);
		System.out.println(Math.PI*Math.PI/6.0);
		
	}

}

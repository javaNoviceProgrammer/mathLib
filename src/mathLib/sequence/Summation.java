package mathLib.sequence;

import java.util.HashMap;
import java.util.Map;

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
		Summation sum = new Summation(k -> k*k) ;
		System.out.println(sum.evaluate(1, 200));
		System.out.println(sum.evaluate(10, 100));
		System.out.println(sum);
	}

}

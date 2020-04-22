package mathLib.sequence;

import java.util.HashMap;
import java.util.Map;

import mathLib.numbers.Complex;


public class ComplexSeries {

	ComplexSequence seq = null ;

	Map<Long, Complex> cache ;
	boolean doCache = false ;
	double convergenceTol = 1e-10 ;
	long maxIterations = 10000L ;

	public ComplexSeries(ComplexSequence seq) {
		this.seq = seq ;
		this.cache = new HashMap<>() ;
	}

	public void setCachingFlag(boolean doCaching){
		this.doCache = doCaching ;
	}

	// finite series
	public Complex sum(long initial, long last){
		Complex result = 0 ;
		for(long k=initial; k<last+1; k++)
			if(doCache) {
				// here is where caching happens
				if(cache.containsKey(k)) {
					result = result + cache.get(k) ; // if a[k] has been evaluated before, we just retrieve it
				}
				else{
					Complex x = seq.evaluate(k) ; // if first time a[k] evaluated, put it in the map
					cache.put(k, x) ;
					result = result + x ;
				}
			}
			else{
				result = result + seq.evaluate(k) ;
			}

		return result ;
	}


	// finite series
	public static Complex sum(ComplexSequence seq, long initial, long last){
		Complex result = 0.0 ;
		long init, lst ;
		// check for proper index values
		if(initial > last) {
			init = last ;
			lst = init ;
		}
		else{
			init = initial ;
			lst = last ;
		}

		// sum evaluation
		for(long k=init; k<lst+1; k++)
			result = result + seq.evaluate(k) ;
		return result ;
	}


}

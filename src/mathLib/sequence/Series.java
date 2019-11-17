package mathLib.sequence;

import java.util.HashMap;
import java.util.Map;

public class Series {

	Sequence seq = null ;

	Map<Long, Double> cache ;
	boolean doCache = false ;
	double convergenceTol = 1e-10 ;
	long maxIterations = 10000L ;

	public Series(Sequence seq) {
		this.seq = seq ;
		this.cache = new HashMap<>() ;
	}

	public void setCachingFlag(boolean doCaching){
		this.doCache = doCaching ;
	}

	// finite series
	public double sum(long initial, long last){
		double result = 0 ;
		for(long k=initial; k<last+1; k++)
			if(doCache) {
				// here is where caching happens
				if(cache.containsKey(k)) {
					result += cache.get(k) ; // if a[k] has been evaluated before, we just retrieve it
				}
				else{
					double x = seq.evaluate(k) ; // if first time a[k] evaluated, put it in the map
					cache.put(k, x) ;
					result += x ;
				}
			}
			else{
				result += seq.evaluate(k) ;
			}

		return result ;
	}

	// only with initial index --> implies infinite sum
	public double sum(long initial){
		double result = 0.0 ;
		double temp = 0.0 ;
		long k = initial ;
		while(k<maxIterations) { // first termination criterion
			temp = seq.evaluate(k) ; // just the value of a_n = S_n - S_(n-1)
			result += temp ; // accumulating S_n
			if(converged(temp, result)) {
				System.out.println("converged");
				break ;
			}
			k++ ;
		}
		if(k==maxIterations)
			System.out.println("Max Iterations reached") ;
		if(Math.abs(temp)<1e-5)
			System.out.println("Convergence");
		else if(Math.abs(temp)>1e-2)
			System.out.println("Divergence");
		else
			System.out.println("Undetermiend status of series");

		return result ;
	}

	private boolean converged(double temp, double result) {
		double absError = Math.abs(temp) ;
		double relError = Math.abs(temp/result) ;
		System.out.println("AbsError = " + absError + " ; " + "Tol = " + 1e-8);
		System.out.println("RelError = " + relError +" ; " + "Tol = " + convergenceTol);
		if(relError<convergenceTol && absError<1e-8)
			return true ;
		else
			return false ;
	}

	// finite series
	public static double sum(Sequence seq, long initial, long last){
		double result = 0 ;
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
			result += seq.evaluate(k) ;
		return result ;
	}


}

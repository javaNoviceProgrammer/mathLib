package mathLib.sequence.special;

import java.util.HashMap;
import java.util.Map;

public class FibonacciNumber {

	public static double fibonacci(int n) {
		if(n<0)
			throw new IllegalArgumentException("argument must be greater than or equal to 0") ;
		if(n==0)
			return 0 ;
		if(n==1)
			return 1 ;
		if(n==2)
			return 1 ;
		double phi = (1+Math.sqrt(5))/2 ;
		double fn = Math.floor(Math.pow(phi, n)/Math.sqrt(5) + 0.5) ;
		return fn ;
	}

	public static double fibonacciRecursive(int n) {
		if(n<0)
			throw new IllegalArgumentException("argument must be greater than or equal to 0") ;
		if(n==0)
			return 0 ;
		if(n==1)
			return 1 ;
		if(n==2)
			return 1 ;
		return fibonacciRecursive(n-1)+fibonacciRecursive(n-2) ;
	}

	public static double fibonacciRecursive(int n, Map<Integer, Double> cache) {
		if(n<0)
			throw new IllegalArgumentException("argument must be greater than or equal to 0") ;
		if(n==0) {
			if(!(cache == null) && cache.containsKey(n))
				return cache.get(n) ;
			else if(!(cache==null)) {
				cache.put(n, 0.0) ;
				return 0.0 ;
			}
			else
				return 0.0 ;
		}

		if(n==1) {
			if(!(cache == null) && cache.containsKey(n))
				return cache.get(n) ;
			else if(!(cache==null)) {
				cache.put(n, 1.0) ;
				return 1.0 ;
			}
			else {
				return 1.0 ;
			}
		}

		if(n==2) {
			if(!(cache == null) && cache.containsKey(n))
				return cache.get(n) ;
			else if(!(cache==null)) {
				cache.put(n, 1.0) ;
				return 1.0 ;
			}
			else
				return 1.0 ;
		}


		double fibNminus1 = 0.0 ;
		double fibNminus2 = 0.0 ;

		if(!(cache == null) && cache.containsKey(n-1))
			fibNminus1 = cache.get(n-1) ;
		else if(!(cache==null)) {
			fibNminus1 = fibonacciRecursive(n-1, cache) ;
			cache.put(n-1, fibNminus1) ;
		}
		else {
			fibNminus1 = fibonacciRecursive(n-1, cache) ;
		}

		if(!(cache == null) && cache.containsKey(n-2))
			fibNminus2 = cache.get(n-2) ;
		else if(!(cache==null)) {
			fibNminus2 = fibonacciRecursive(n-2, cache) ;
			cache.put(n-2, fibNminus2) ;
		}
		else {
			fibNminus2 = fibonacciRecursive(n-2, cache) ;
		}

		return fibNminus1 + fibNminus2 ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(fibonacci(1));
		System.out.println(fibonacci(2));
		System.out.println(fibonacci(3));
		System.out.println(fibonacci(4));
		System.out.println(fibonacci(5));
		System.out.println(fibonacci(6));
		System.out.println(fibonacci(7));
		System.out.println(fibonacci(8));
		System.out.println(fibonacci(9));
		System.out.println(fibonacci(10));
		System.out.println(fibonacci(11));
		System.out.println(fibonacci(12));
		System.out.println(fibonacci(13));
		System.out.println(fibonacci(14));
		System.out.println(fibonacciRecursive(14, new HashMap<>()));
	}

}

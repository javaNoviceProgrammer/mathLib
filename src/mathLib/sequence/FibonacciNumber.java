package mathLib.sequence;

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
		return fibonacci(n-1)+fibonacci(n-2) ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(fibonacci(0));
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
	}

}

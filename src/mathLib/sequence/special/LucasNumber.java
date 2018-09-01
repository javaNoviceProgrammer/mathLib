package mathLib.sequence.special;

public class LucasNumber {

	public static double lucas(int n) {
		if (n<0)
			throw new IllegalArgumentException("argument must be greater than or equal to 0") ;
		if(n==0)
			return 2 ;
		if(n==1)
			return 1 ;
		double p = (1+Math.sqrt(5))/2 ;
		double q = (1-Math.sqrt(5))/2 ;
		return (int) (Math.pow(p, n) + Math.pow(q, n)) ;
	}

	// for test
	public static void main(String[] args) {
		System.out.println(lucas(0));
		System.out.println(lucas(1));
		System.out.println(lucas(2));
		System.out.println(lucas(3));
		System.out.println(lucas(4));
		System.out.println(lucas(5));
		System.out.println(lucas(6));
		System.out.println(lucas(7));
		System.out.println(lucas(8));
		System.out.println(lucas(9));
		System.out.println(lucas(10));
	}

}

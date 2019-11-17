package mathLib.sequence;

public class Fibonacci implements Sequence {

	private int f0 = 1 ;
	private int f1 = 1 ;

	// when we implement recursion --> start with initial conditions
	// stop conditions for the recursion

	@Override
	public double evaluate(long k) {
		// initial
		if(k==0)
			return f0 ;
		else if(k==1)
			return f1 ;
		else
			// implement recursion
			return evaluate(k-1) + evaluate(k-2) ;
	}





}

package mathLib.sequence;

public class SeqExample implements Sequence {

	@Override
	public double evaluate(long k) {
		return k*k;
	}

	@Override
	public String toString() {
		return "a[k] = k^2" ;
	}



}

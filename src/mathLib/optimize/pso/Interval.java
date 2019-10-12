package mathLib.optimize.pso;

// default access modifier --> visible in the package
public class Interval {

	private double start ;
	private double end ;

	Interval(double start, double end) {
		this.start = start ;
		this.end = end ;
	}

	double getStart() {
		return this.start ;
	}

	double getEnd() {
		return this.end ;
	}

	// the useful method for uniform random choice
	double getRandom() {
		double r = Math.random() ; // r in [0,1) --> [start,end]
		return start + (end-start)*r ;
	}

	@Override
	public String toString() {
		return "[" + start + "," + end + "]";
	}



}

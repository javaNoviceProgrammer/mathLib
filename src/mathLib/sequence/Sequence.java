package mathLib.sequence;

/**
 * interface representing a real-valued sequence
 * @author Meisam
 *
 */
@FunctionalInterface
public interface Sequence {


	/**
	 * evaluates a sequence at a given index
	 * @param k index of the term
	 * @return returns a {@code double} value
	 */
	double evaluate(long k) ; // defining the function of the sequence k --> a_k

	/**
	 * forward difference of a sequence
	 * @return returns a {@code Sequence} object
	 */
	default Sequence diff() {
		return n -> evaluate(n+1)-evaluate(n) ;
	}

	/**
	 * Aitken transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code Sequence} object
	 */
	default Sequence aitken() {
		return n -> evaluate(n+2) - (evaluate(n+2)-evaluate(n+1))*(evaluate(n+2)-evaluate(n+1))
					/(evaluate(n+2)-2.0*evaluate(n+1)+evaluate(n)) ;
	}

	/**
	 * Shanks transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code Sequence} object
	 */
	default Sequence shanks() {
		return n -> evaluate(n+1) - (evaluate(n+1)-evaluate(n))*(evaluate(n+1)-evaluate(n))
				/(evaluate(n+1)-2.0*evaluate(n)+evaluate(n-1)) ;
	}

	/**
	 * Richardson extrapolation for a given order
	 * @param order order of initial convergence error
	 * @return a {@code Sequence} object
	 */
	default Sequence richardson(double order) {
		return n -> {
			double c1 = Math.pow(n+1, order) ;
			double c2 = Math.pow(n, order) ;
			return (c1*evaluate(n+1)-c2*evaluate(n))/(c1-c2) ;
		} ;
	}

	/**
	 * Richardson acceleration of a sequence. Mostly used for non-oscillatory sequences.
	 * @return returns a {@code Sequence} object
	 */
	default Sequence richardson() {
		return n -> (n+1)*evaluate(n+1)-n*evaluate(n) ;
	}

	/**
	 * Richardson acceleration (quadratic).
	 * @return a {@code Sequence} object.
	 */
	default Sequence richardson2() {
		Sequence seq1 = n -> 0.5*(n+2)*(n+2)*evaluate(n+2) ;
		Sequence seq2 = n -> -(n+1)*(n+1)*evaluate(n+1) ;
		Sequence seq3 = n -> 0.5*n*n*evaluate(n) ;
		return seq1+seq2+seq3 ;
	}

	/**
	 * Richardson acceleration (cubic)
	 * @return a {@code Sequence} object.
	 */
	default Sequence richardson3() {
		Sequence seq1 = n -> (n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		Sequence seq2 = n -> -3.0*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		Sequence seq3 = n -> 3.0*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		Sequence seq4 = n -> -n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4)/6.0 ;
	}

	/**
	 * Richardson acceleration (4th-order)
	 * @return a {@code Sequence} object.
	 */
	default Sequence richardson4() {
		Sequence seq1 = n -> (n+4)*(n+4)*(n+4)*(n+4)*evaluate(n+4) ;
		Sequence seq2 = n -> -4.0*(n+3)*(n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		Sequence seq3 = n -> 6.0*(n+2)*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		Sequence seq4 = n -> -4.0*(n+1)*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		Sequence seq5 = n -> n*n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4+seq5)/24.0 ;
	}

	//*************** operator overloading ****************

	default Sequence add(int v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence add(long v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence add(float v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence add(double v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence add(Sequence v) {
		return n -> this.evaluate(n) + v.evaluate(n) ;
	}

	default Sequence addRev(int v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence addRev(long v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence addRev(float v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence addRev(double v) {
		return n -> evaluate(n) + v ;
	}

	default Sequence addRev(Sequence v) {
		return n -> v.evaluate(n) + this.evaluate(n) ;
	}

	default Sequence subtract(int v) {
		return n -> evaluate(n) - v ;
	}

	default Sequence subtract(long v) {
		return n -> evaluate(n) - v ;
	}

	default Sequence subtract(float v) {
		return n -> evaluate(n) - v ;
	}

	default Sequence subtract(double v) {
		return n -> evaluate(n) - v ;
	}

	default Sequence subtract(Sequence v) {
		return n -> this.evaluate(n) - v.evaluate(n) ;
	}

	default Sequence subtractRev(int v) {
		return n -> v - evaluate(n) ;
	}

	default Sequence subtractRev(long v) {
		return n -> v - evaluate(n) ;
	}

	default Sequence subtractRev(float v) {
		return n -> v - evaluate(n) ;
	}

	default Sequence subtractRev(double v) {
		return n -> v - evaluate(n) ;
	}

	default Sequence subtractRev(Sequence v) {
		return n -> v.evaluate(n) - this.evaluate(n) ;
	}

	default Sequence multiply(int v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiply(long v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiply(float v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiply(double v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiply(Sequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default Sequence multiplyRev(int v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiplyRev(long v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiplyRev(float v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiplyRev(double v) {
		return n -> evaluate(n)*v ;
	}

	default Sequence multiplyRev(Sequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default Sequence divide(int v) {
		return n -> evaluate(n)/v ;
	}

	default Sequence divide(long v) {
		return n -> evaluate(n)/v ;
	}

	default Sequence divide(float v) {
		return n -> evaluate(n)/v ;
	}

	default Sequence divide(double v) {
		return n -> evaluate(n)/v ;
	}

	default Sequence divide(Sequence v) {
		return n -> this.evaluate(n)/v.evaluate(n) ;
	}

	default Sequence divideRev(int v) {
		return n -> v/evaluate(n) ;
	}

	default Sequence divideRev(long v) {
		return n -> v/evaluate(n) ;
	}

	default Sequence divideRev(float v) {
		return n -> v/evaluate(n) ;
	}

	default Sequence divideRev(double v) {
		return n -> v/evaluate(n) ;
	}

	default Sequence divideRev(Sequence v) {
		return n -> v.evaluate(n)/this.evaluate(n) ;
	}

	default Sequence negate() {
		return n -> -evaluate(n) ;
	}


}

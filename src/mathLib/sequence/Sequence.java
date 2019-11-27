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

	//*************** operations *******************

	/**
	 * Discrete unit delta sequence: delta[n] = 1 if n=0, else delta[n]=0
	 * @return a {@code Sequence} object.
	 */
	static Sequence delta() {
		return n -> (n==0) ? 1.0 : 0.0 ; // returning double value
	}

	/**
	 * Discrete unit delta sequence (shifted): delta[n-m] = 1 if n=m, else delta[n-m]=0
	 * @param m delay (shift) of the unit impulse
	 * @return a {@code Sequence} object.
	 */
	static Sequence delta(int m) {
		return n -> (n==m) ? 1.0 : 0.0 ; // returning double value
	}

	/**
	 * Discrete unit step sequence: u[n] = 1 for n>=0, else u[n] = 0
	 * @return a {@code Sequence} object.
	 */
	static Sequence unitStep() {
		return n -> (n>=0) ? 1.0 : 0.0 ; // returning double value
	}

	/**
	 * Discrete unit step sequence (shifted): u[n-m] = 1 for n>=m, else u[n] = 0
	 * @param m delay (shift) of the unit step
	 * @return a {@code Sequence} object.
	 */
	static Sequence unitStep(int m) {
		return n -> (n>=m) ? 1.0 : 0.0 ; // returning double value
	}

	default Sequence shift(int m) {
		return n -> evaluate(n-m) ;
	}

	/**
	 * Discrete convolution sum of two sequences. a[n]=0 and b[n]=0 for n<0. 
	 * @param b the other sequence to be convolved with.
	 * @return a {@code Sequence} object.
	 */
	default Sequence convolve(Sequence b) {
		Sequence a = this ;
		// returns conv(a,b)
		return n -> Series.sum(m -> a.evaluate(m)*b.evaluate(n-m), 0, n) ;
	}

	static Sequence forArray(double... array) {
		return n -> {
			if(n>=0.0 && n<array.length)
				return array[(int) n] ;
			else
				return 0.0 ;
		} ;
	}

	static Sequence forArray(int shift, double... array) {
		return n -> {
			if(n>=shift && n<array.length+shift)
				return array[(int) (n-shift)] ;
			else
				return 0.0 ;
		} ;
	}

	static Sequence runningAverage(Sequence seq, int order) {
		return n -> 1.0/order * Series.sum(seq, n, n+order-1) ;
	}

	default Sequence runningAverage(int order) {
		return n -> 1.0/order * Series.sum(this, n, n+order-1) ;
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

package mathLib.sequence;

import mathLib.numbers.Complex;
import static mathLib.numbers.Complex.* ;

/**
 * interface representing a complex-valued sequence
 * @author Meisam
 *
 */
@FunctionalInterface
public interface ComplexSequence {

	/**
	 * evaluates a sequence at a given index
	 * @param k index of the term
	 * @return returns a {@code Complex} value
	 */
	Complex evaluate(long k) ; // defining the function of the sequence k --> a_k

	/**
	 * forward difference of a sequence
	 * @return returns a {@code ComplexSequence} object
	 */
	default ComplexSequence diff() {
		return n -> evaluate(n+1)-evaluate(n) ;
	}

	/**
	 * Aitken transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code ComplexSequence} object
	 */
	default ComplexSequence aitken() {
		return n -> evaluate(n+2) - (evaluate(n+2)-evaluate(n+1))*(evaluate(n+2)-evaluate(n+1))
					/(evaluate(n+2)-2.0*evaluate(n+1)+evaluate(n)) ;
	}

	/**
	 * Shanks transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code ComplexSequence} object
	 */
	default ComplexSequence shanks() {
		return n -> evaluate(n+1) - (evaluate(n+1)-evaluate(n))*(evaluate(n+1)-evaluate(n))
				/(evaluate(n+1)-2.0*evaluate(n)+evaluate(n-1)) ;
	}

	/**
	 * Richardson extrapolation for a given order
	 * @param order order of initial convergence error
	 * @return a {@code ComplexSequence} object
	 */
	default ComplexSequence richardson(double order) {
		return n -> {
			double c1 = Math.pow(n+1, order) ;
			double c2 = Math.pow(n, order) ;
			return (c1*evaluate(n+1)-c2*evaluate(n))/(c1-c2) ;
		} ;
	}

	/**
	 * Richardson acceleration of a sequence. Mostly used for non-oscillatory sequences.
	 * @return returns a {@code ComplexSequence} object
	 */
	default ComplexSequence richardson() {
		return n -> (n+1)*evaluate(n+1)-n*evaluate(n) ;
	}

	/**
	 * Richardson acceleration (quadratic).
	 * @return a {@code ComplexSequence} object.
	 */
	default ComplexSequence richardson2() {
		ComplexSequence seq1 = n -> 0.5*(n+2)*(n+2)*evaluate(n+2) ;
		ComplexSequence seq2 = n -> -(n+1)*(n+1)*evaluate(n+1) ;
		ComplexSequence seq3 = n -> 0.5*n*n*evaluate(n) ;
		return seq1+seq2+seq3 ;
	}

	/**
	 * Richardson acceleration (cubic)
	 * @return a {@code Sequence} object.
	 */
	default ComplexSequence richardson3() {
		ComplexSequence seq1 = n -> (n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		ComplexSequence seq2 = n -> -3.0*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		ComplexSequence seq3 = n -> 3.0*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		ComplexSequence seq4 = n -> -n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4)/6.0 ;
	}

	/**
	 * Richardson acceleration (4th-order)
	 * @return a {@code Sequence} object.
	 */
	default ComplexSequence richardson4() {
		ComplexSequence seq1 = n -> (n+4)*(n+4)*(n+4)*(n+4)*evaluate(n+4) ;
		ComplexSequence seq2 = n -> -4.0*(n+3)*(n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		ComplexSequence seq3 = n -> 6.0*(n+2)*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		ComplexSequence seq4 = n -> -4.0*(n+1)*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		ComplexSequence seq5 = n -> n*n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4+seq5)/24.0 ;
	}

	//*************** operations ***********

	static ComplexSequence delta() {
		return n -> (n==0) ? 1.0+0.0*j : 0.0*j ;
	}

	static ComplexSequence delta(int m) {
		return n -> (n==m) ? 1.0+0.0*j : 0.0*j ;
	}

	static ComplexSequence unitStep() {
		return n -> (n>=0) ? 1.0+0.0*j : 0.0*j ;
	}

	static ComplexSequence unitStep(int m) {
		return n -> (n>=m) ? 1.0+0.0*j : 0.0*j ;
	}

	default ComplexSequence shift(int m) {
		return n -> evaluate(n-m) ;
	}

	default ComplexSequence convolve(ComplexSequence b) {
		ComplexSequence a = this ;
		// returns conv(a,b)
		return n -> ComplexSeries.sum(m -> a.evaluate(m)*b.evaluate(n-m), 0, n) ;
	}

	static ComplexSequence forArray(double... array) {
		return n -> {
			if(n>=0.0 && n<array.length)
				return array[(int) n]+0.0*j ;
			else
				return 0.0*j ;
		} ;
	}

	static ComplexSequence forArray(Complex... array) {
		return n -> {
			if(n>=0.0 && n<array.length)
				return array[(int) n]+0.0*j ;
			else
				return 0.0*j ;
		} ;
	}

	static ComplexSequence forArray(int shift, double... array) {
		return n -> {
			if(n>=shift && n<array.length+shift)
				return array[(int) (n-shift)]+0.0*j ;
			else
				return 0.0*j ;
		} ;
	}

	static ComplexSequence forArray(int shift, Complex... array) {
		return n -> {
			if(n>=shift && n<array.length+shift)
				return array[(int) (n-shift)]+0.0*j ;
			else
				return 0.0*j ;
		} ;
	}

	static ComplexSequence runningAverage(ComplexSequence seq, int order) {
		return n -> 1.0/order * ComplexSeries.sum(seq, n, n+order-1) ;
	}

	default ComplexSequence runningAverage(int order) {
		return n -> 1.0/order * ComplexSeries.sum(this, n, n+order-1) ;
	}

	default ComplexSequence conjugate() {
		return n -> evaluate(n).conjugate() ;
	}

	//*************** operator overloading ****************

	default ComplexSequence add(int v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence add(long v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence add(float v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence add(double v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence add(ComplexSequence v) {
		return n -> this.evaluate(n) + v.evaluate(n) ;
	}

	default ComplexSequence addRev(int v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence addRev(long v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence addRev(float v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence addRev(double v) {
		return n -> evaluate(n) + v ;
	}

	default ComplexSequence addRev(ComplexSequence v) {
		return n -> v.evaluate(n) + this.evaluate(n) ;
	}

	default ComplexSequence subtract(int v) {
		return n -> evaluate(n) - v ;
	}

	default ComplexSequence subtract(long v) {
		return n -> evaluate(n) - v ;
	}

	default ComplexSequence subtract(float v) {
		return n -> evaluate(n) - v ;
	}

	default ComplexSequence subtract(double v) {
		return n -> evaluate(n) - v ;
	}

	default ComplexSequence subtract(ComplexSequence v) {
		return n -> this.evaluate(n) - v.evaluate(n) ;
	}

	default ComplexSequence subtractRev(int v) {
		return n -> v - evaluate(n) ;
	}

	default ComplexSequence subtractRev(long v) {
		return n -> v - evaluate(n) ;
	}

	default ComplexSequence subtractRev(float v) {
		return n -> v - evaluate(n) ;
	}

	default ComplexSequence subtractRev(double v) {
		return n -> v - evaluate(n) ;
	}

	default ComplexSequence subtractRev(ComplexSequence v) {
		return n -> v.evaluate(n) - this.evaluate(n) ;
	}

	default ComplexSequence multiply(int v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiply(long v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiply(float v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiply(double v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiply(ComplexSequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default ComplexSequence multiplyRev(int v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiplyRev(long v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiplyRev(float v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiplyRev(double v) {
		return n -> evaluate(n)*v ;
	}

	default ComplexSequence multiplyRev(ComplexSequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default ComplexSequence divide(int v) {
		return n -> evaluate(n)/v ;
	}

	default ComplexSequence divide(long v) {
		return n -> evaluate(n)/v ;
	}

	default ComplexSequence divide(float v) {
		return n -> evaluate(n)/v ;
	}

	default ComplexSequence divide(double v) {
		return n -> evaluate(n)/v ;
	}

	default ComplexSequence divide(ComplexSequence v) {
		return n -> this.evaluate(n)/v.evaluate(n) ;
	}

	default ComplexSequence divideRev(int v) {
		return n -> v/evaluate(n) ;
	}

	default ComplexSequence divideRev(long v) {
		return n -> v/evaluate(n) ;
	}

	default ComplexSequence divideRev(float v) {
		return n -> v/evaluate(n) ;
	}

	default ComplexSequence divideRev(double v) {
		return n -> v/evaluate(n) ;
	}

	default ComplexSequence divideRev(ComplexSequence v) {
		return n -> v.evaluate(n)/this.evaluate(n) ;
	}

	default ComplexSequence negate() {
		return n -> -evaluate(n) ;
	}

}

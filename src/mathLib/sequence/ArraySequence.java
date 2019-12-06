package mathLib.sequence;

import mathLib.arrays.NdArray;

/**
 * interface representing an array sequence
 * @author Meisam
 *
 */
@FunctionalInterface
public interface ArraySequence {

	/**
	 * evaluates a sequence at a given index
	 * @param k index of the term
	 * @return returns a {@code double} value
	 */
	NdArray evaluate(long k) ; // defining the function of the sequence k --> a_k

	default Sequence at(int index) {
		return n -> evaluate(n).at(index) ;
	}

	/**
	 * forward difference of a sequence
	 * @return returns a {@code Sequence} object
	 */
	default ArraySequence diff() {
		return n -> evaluate(n+1)-evaluate(n) ;
	}

	/**
	 * Aitken transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code Sequence} object
	 */
	default ArraySequence aitken() {
		return n -> evaluate(n+2) - (evaluate(n+2)-evaluate(n+1))*(evaluate(n+2)-evaluate(n+1))
					/(evaluate(n+2)-2.0*evaluate(n+1)+evaluate(n)) ;
	}

	/**
	 * Shanks transformation of a sequence. Mostly used for oscillating sequences (-1)^n.
	 * @return returns a {@code Sequence} object
	 */
	default ArraySequence shanks() {
		return n -> evaluate(n+1) - (evaluate(n+1)-evaluate(n))*(evaluate(n+1)-evaluate(n))
				/(evaluate(n+1)-2.0*evaluate(n)+evaluate(n-1)) ;
	}

	/**
	 * Richardson extrapolation for a given order
	 * @param order order of initial convergence error
	 * @return a {@code Sequence} object
	 */
	default ArraySequence richardson(double order) {
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
	default ArraySequence richardson() {
		return n -> (n+1)*evaluate(n+1)-n*evaluate(n) ;
	}

	/**
	 * Richardson acceleration (quadratic).
	 * @return a {@code Sequence} object.
	 */
	default ArraySequence richardson2() {
		ArraySequence seq1 = n -> 0.5*(n+2)*(n+2)*evaluate(n+2) ;
		ArraySequence seq2 = n -> -(n+1)*(n+1)*evaluate(n+1) ;
		ArraySequence seq3 = n -> 0.5*n*n*evaluate(n) ;
		return seq1+seq2+seq3 ;
	}

	/**
	 * Richardson acceleration (cubic)
	 * @return a {@code Sequence} object.
	 */
	default ArraySequence richardson3() {
		ArraySequence seq1 = n -> (n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		ArraySequence seq2 = n -> -3.0*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		ArraySequence seq3 = n -> 3.0*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		ArraySequence seq4 = n -> -n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4)/6.0 ;
	}

	/**
	 * Richardson acceleration (4th-order)
	 * @return a {@code Sequence} object.
	 */
	default ArraySequence richardson4() {
		ArraySequence seq1 = n -> (n+4)*(n+4)*(n+4)*(n+4)*evaluate(n+4) ;
		ArraySequence seq2 = n -> -4.0*(n+3)*(n+3)*(n+3)*(n+3)*evaluate(n+3) ;
		ArraySequence seq3 = n -> 6.0*(n+2)*(n+2)*(n+2)*(n+2)*evaluate(n+2) ;
		ArraySequence seq4 = n -> -4.0*(n+1)*(n+1)*(n+1)*(n+1)*evaluate(n+1) ;
		ArraySequence seq5 = n -> n*n*n*n*evaluate(n) ;
		return (seq1+seq2+seq3+seq4+seq5)/24.0 ;
	}

	//*************** operations ***********

	default ArraySequence shift(int m) {
		return n -> evaluate(n-m) ;
	}

	//*************** operator overloading ****************

	default ArraySequence add(int v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence add(long v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence add(float v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence add(double v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence add(ArraySequence v) {
		return n -> this.evaluate(n) + v.evaluate(n) ;
	}

	default ArraySequence addRev(int v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence addRev(long v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence addRev(float v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence addRev(double v) {
		return n -> evaluate(n) + v ;
	}

	default ArraySequence addRev(ArraySequence v) {
		return n -> v.evaluate(n) + this.evaluate(n) ;
	}

	default ArraySequence subtract(int v) {
		return n -> evaluate(n) - v ;
	}

	default ArraySequence subtract(long v) {
		return n -> evaluate(n) - v ;
	}

	default ArraySequence subtract(float v) {
		return n -> evaluate(n) - v ;
	}

	default ArraySequence subtract(double v) {
		return n -> evaluate(n) - v ;
	}

	default ArraySequence subtract(ArraySequence v) {
		return n -> this.evaluate(n) - v.evaluate(n) ;
	}

	default ArraySequence subtractRev(int v) {
		return n -> v - evaluate(n) ;
	}

	default ArraySequence subtractRev(long v) {
		return n -> v - evaluate(n) ;
	}

	default ArraySequence subtractRev(float v) {
		return n -> v - evaluate(n) ;
	}

	default ArraySequence subtractRev(double v) {
		return n -> v - evaluate(n) ;
	}

	default ArraySequence subtractRev(ArraySequence v) {
		return n -> v.evaluate(n) - this.evaluate(n) ;
	}

	default ArraySequence multiply(int v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiply(long v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiply(float v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiply(double v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiply(ArraySequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default ArraySequence multiplyRev(int v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiplyRev(long v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiplyRev(float v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiplyRev(double v) {
		return n -> evaluate(n)*v ;
	}

	default ArraySequence multiplyRev(ArraySequence v) {
		return n -> this.evaluate(n)*v.evaluate(n) ;
	}

	default ArraySequence divide(int v) {
		return n -> evaluate(n)/v ;
	}

	default ArraySequence divide(long v) {
		return n -> evaluate(n)/v ;
	}

	default ArraySequence divide(float v) {
		return n -> evaluate(n)/v ;
	}

	default ArraySequence divide(double v) {
		return n -> evaluate(n)/v ;
	}

	default ArraySequence divide(ArraySequence v) {
		return n -> this.evaluate(n)/v.evaluate(n) ;
	}

	default ArraySequence divideRev(int v) {
		return n -> v/evaluate(n) ;
	}

	default ArraySequence divideRev(long v) {
		return n -> v/evaluate(n) ;
	}

	default ArraySequence divideRev(float v) {
		return n -> v/evaluate(n) ;
	}

	default ArraySequence divideRev(double v) {
		return n -> v/evaluate(n) ;
	}

	default ArraySequence divideRev(ArraySequence v) {
		return n -> v.evaluate(n)/this.evaluate(n) ;
	}

	default ArraySequence negate() {
		return n -> -evaluate(n) ;
	}

}

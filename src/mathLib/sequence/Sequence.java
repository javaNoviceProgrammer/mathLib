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
	 * @return returns a double value
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
	 * Aitken transformation of a sequence
	 * @return returns a {@code Sequence} object
	 */
	default Sequence aitken() {
		return n -> evaluate(n+2) - (evaluate(n+2)-evaluate(n+1))*(evaluate(n+2)-evaluate(n+1))
					/(evaluate(n+2)-2.0*evaluate(n+1)+evaluate(n)) ;
	}

	/**
	 * Shanks transformation of a sequence
	 * @return returns a {@code Sequence} object
	 */
	default Sequence shanks() {
		return n -> evaluate(n+1) - (evaluate(n+1)-evaluate(n))*(evaluate(n+1)-evaluate(n))
				/(evaluate(n+1)-2.0*evaluate(n)+evaluate(n-1)) ;
	}

}

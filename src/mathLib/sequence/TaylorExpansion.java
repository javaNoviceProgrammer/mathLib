package mathLib.sequence;

import mathLib.func.sym.*;
import mathLib.polynom.Polynomial;
import static mathLib.polynom.Polynomial.x ;

public class TaylorExpansion {

	Function func ;

	public TaylorExpansion(Function func) {
		this.func = func ;
	}

	// finding taylor series up to degree n
	/**
	 *
	 * @param x0 point of interest
	 * @param n	degree of polynomial
	 * @return Taylor series of the function
	 */
	public Polynomial getSeries(double x0, int n) {
		Polynomial result = new Polynomial(null) ; // p(x) = 0
		for(int i=0; i<n+1; i++){
			if(i==0)
				result = result + func.getValue(x0) ;
			else{
				Function funcCopy = func ;
				Polynomial p = new Polynomial(1.0) ; // p(x) = 1
				double factorial = 1 ;
				// differentiate i times
				for(int k=0; k<i; k++){
					funcCopy = funcCopy.diff() ;
					p = p*(x-x0) ;
					factorial *= (k+1) ; // 1*2*3*...*i = i!
				}
				// add to the result
				result = result + 1.0/factorial*funcCopy.getValue(x0)*p ;
			}
		}

		return result ;
	}



}

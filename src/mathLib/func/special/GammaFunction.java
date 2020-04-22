package mathLib.func.special;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.sequence.ComplexSequence;
import mathLib.sequence.Sequence;

/**
 * Implementation of Gamma function
 *
 * @author Meisam
 *
 */
public class GammaFunction {

	Sequence gammaSeq, gammaSeqProd ;
	ComplexSequence gammaSeqComplex ;
	int order = 1000 ;
	double g = 	0.5772156649 ; // Euler-Mascheroni number

	public GammaFunction() {
		gammaSeq = n -> Double.NaN ;
		gammaSeqProd = n -> Double.NaN ;
	}

	//********** fast implementation of Gamma function ***************

	public double gamma(double x) {
		return gamma3(x) ;
	}

	public Complex gamma(Complex z) {
		return gamma5(z) ;
	}

	public double gammaln(double x) {
		return abs(gamma(x)) ;
	}

	public double gammaln(Complex z) {
		return ComplexMath.abs(gamma(z)) ;
	}

	public double loggamma(double x) {
		return log(gamma(x)) ;
	}

	public Complex loggamma(Complex z) {
		return ComplexMath.log(gamma(z)) ;
	}

	public double gammasgn(double x) {
		double gamma = gamma(x) ;
		if(gamma>0.0)
			return 1.0 ;
		else if(gamma<0.0)
			return -1.0 ;
		else
			return 0.0 ;
	}

	public double rgamma(double x) {
		return 1.0/gamma(x) ;
	}

	public Complex rgamma(Complex z) {
		return 1.0/gamma(z) ;
	}

	public double poch(double x, int m) {
		return gamma(x+m)/gamma(x) ;
	}

	public Complex poch(Complex z, int m) {
		return gamma(z+m)/gamma(z) ;
	}

	//************* other implementations of Gamma(x) *****************

	// Original implementation of Euler's definition of Gamma function
	double gamma2(double x) {
		gammaSeq = n -> pow(1.0+1.0/n, x)/(1.0+x/n) ;
		gammaSeqProd = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++) {
				result *= gammaSeq.evaluate(i) ;
			}
			return result ;
		} ;
		return gammaSeqProd.richardson4().evaluate(300)/x ;
	}

	// enhanced version of Euler's definition
	double gamma2Modified(double x) {
		gammaSeq = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++)
				result *= 1.0 + 1.0/i ;
			return result ;
		} ;
		gammaSeqProd = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++) {
				result *= 1.0/(1.0+x/i) ;
			}
			return result ;
		} ;
		Sequence tot = n -> pow(gammaSeq.evaluate(n), x)*gammaSeqProd.evaluate(n) ;
		return tot.richardson4().evaluate(300)/x ;
	}

	// Weierstrass implementation for real numbers
	double gamma3(double x) {
		gammaSeqProd = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++) {
				result *= 1.0+x/i ;
			}
			return result ;
		} ;
		Sequence sum = n -> {
			double result = 0.0 ;
			for(int i=1; i<n+1; i++)
				result += 1.0/i ; // caution: don't use 1/i --> int/int = 0 --> not correct!!
			return result-g ;
		} ;
		Sequence tot = n -> exp(x*sum.evaluate(n))/gammaSeqProd.evaluate(n) ;
		return tot.richardson4().evaluate(300)/x ;
	}

	// most efficient implementation of Euler's definition for Gamma(z)
	double gamma4(double x) {
		gammaSeq = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++)
				result *= 1.0 + 1.0/i ;
			return result ;
		} ;
		gammaSeqProd = n -> {
			double result = 1.0 ;
			for(int i=1; i<n+1; i++) {
				result *= 1.0+x/i ;
			}
			return result ;
		} ;
		Sequence tot = n -> pow(gammaSeq.evaluate(n), x)/gammaSeqProd.evaluate(n) ;
		return tot.richardson4().evaluate(300)/x ;
	}

	// Weierstrass implementation for complex numbers
	Complex gamma5(Complex x) {
		gammaSeqComplex = n -> {
			Complex result = 1.0 ;
			for(int i=1; i<n+1; i++) {
				result = result * (1.0+x/i) ;
			}
			return result ;
		} ;
		Sequence sum = n -> {
			double result = 0.0 ;
			for(int i=1; i<n+1; i++)
				result += 1.0/i ; // caution: don't use 1/i --> int/int = 0 --> not correct!!
			return result-g ;
		} ;
		ComplexSequence tot = n -> ComplexMath.exp(x*sum.evaluate(n))/gammaSeqComplex.evaluate(n) ;
		return tot.richardson4().evaluate(300)/x ;
	}


}

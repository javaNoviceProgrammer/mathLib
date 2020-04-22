package mathLib.func.special;


import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.pow;

import java.util.function.Function;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.sequence.ComplexSequence;
import mathLib.sequence.Sequence;

public class DigammaFunction {

	double g = 	0.5772156649 ; // Euler-Mascheroni number
	Sequence seqSum ;
	ComplexSequence seqSumComplex ;

	public DigammaFunction() {

	}

	public double digamma(double x) {
		seqSum = n -> {
			double result = 0.0 ;
			for(int i=0; i<n; i++)
				result += 1.0/((i+1.0)*(i+x)) ;
			return result ;
		} ;

		if(abs(x)<=5.0)
			return -g + (x-1)*seqSum.richardson4().evaluate(100) ;
		else if(x>5.0) {
			return log(x)-1.0/(2.0*x)-1.0/(12.0*x*x)+1.0/(120.0*x*x*x*x)
					-1.0/(252.0*pow(x,6))+1.0/(240.0*pow(x,8))-5.0/(660.0*pow(x,10))+691.0/(32760.0*pow(x,12))-1.0/(12.0*pow(x,14)) ;
		}
		else {
			return -g + (x-1)*seqSum.richardson4().evaluate(300) ;
		}
	}

	public double polygamma(double x) {
		return digamma(x) ;
	}

	public double psi(double x) {
		return digamma(x) ;
	}

	public double polygamma(int n, double x) {
		if(n<1.0)
			return polygamma(x) ;
		// nth derivative of psi function
		Function<Double, Double> psi = t -> this.psi(t) ;
		for(int i=0; i<n; i++)
			psi = deriv(psi) ;
		return psi.apply(x) ;
	}

	private Function<Double, Double> deriv(Function<Double, Double> func) {
		double h = 1e-2 ;
		return t -> (func.apply(t+h)-func.apply(t-h))/(2.0*h) ;
	}

	public Complex digamma(Complex z) {
		seqSumComplex = n -> {
			Complex result = 0.0 ;
			for(int i=0; i<n; i++)
				result = result + 1.0/((i+1.0)*(i+z)) ;
			return result ;
		} ;

		if(ComplexMath.abs(z)<=5.0)
			return -g + (z-1)*seqSumComplex.richardson4().evaluate(100) ;
		else {
			return ComplexMath.log(z)-1.0/(2.0*z)-1.0/(12.0*z*z)+1.0/(120.0*z*z*z*z)
					-1.0/(252.0*ComplexMath.pow(z,6))+1.0/(240.0*ComplexMath.pow(z,8))-5.0/(660.0*ComplexMath.pow(z,10))+691.0/(32760.0*ComplexMath.pow(z,12))-1.0/(12.0*ComplexMath.pow(z,14)) ;
		}
	}

	public Complex polygamma(Complex z) {
		return digamma(z) ;
	}

	public Complex psi(Complex z) {
		return digamma(z) ;
	}

}

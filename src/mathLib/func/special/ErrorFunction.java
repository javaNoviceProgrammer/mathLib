package mathLib.func.special;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.exp;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;
import static mathLib.numbers.Complex.j;

import java.util.function.Function;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.root.RealRoot;
import mathLib.root.RealRootFunction;
import mathLib.sequence.Sequence;
import mathLib.sequence.Series;

public class ErrorFunction {

	Sequence erfSeq ;
	Sequence reSeq, imSeq, reSum, imSum ;
	double leadTerm ;
	double val ;
	RealRootFunction funcErfInv ;
	RealRoot rootFinderErfInv ;

	public ErrorFunction() {

	}

	private double erfSmallArgument(double x) {
		leadTerm = 2.0/sqrt(PI)*x ;
		val = x*x ;
		erfSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 - val*(2.0*i-1.0)/(i*(2.0*i+1.0))*result ;
			}
			return result ;
		} ;
		return erfSeq.evaluate(200)*leadTerm ;
	}

	private double erfLargeArgument(double x) {
		if(x<0.0)
			return -erfLargeArgument(-x) ;
		leadTerm = exp(-x*x)/(abs(x)*sqrt(PI)) ;
		val = x*x ;
		double a1 = 0.5/val ;
		double a2 = 0.75/(val*val) ;
		double a3 = 1.875/(val*val*val) ;
		return 1.0-leadTerm*(1.0-a1+a2-a3) ;
	}

	public double erf(double x) {
		if(abs(x)<4.0)
			return erfSmallArgument(x) ;
		else
			return erfLargeArgument(x) ;
	}

	public double erfc(double x) {
		return 1.0-erf(x) ;
	}

	public Complex erf(Complex z) {
		double x = z.re() ;
		double y = z.im() ;
		double coeff = exp(-x*x)/PI ;
		double term1 = erf(x) ;
		Complex term2 = coeff*y*sinc(x*y)*(sin(x*y)+j*cos(x*y)) ;
		reSeq = n -> 2.0*x-2.0*x*cosh(n*y)*cos(2.0*x*y)+n*sinh(n*y)*sin(2.0*x*y) ;
		imSeq = n -> 2.0*x*cosh(n*y)*sin(2.0*x*y)+n*sinh(n*y)*cos(2.0*x*y) ;
		reSum = n -> Series.sum(m -> exp(-0.25*m*m)/(m*m+4.0*x*x)*reSeq.evaluate(m), 1, n) ;
		imSum = n -> Series.sum(m -> exp(-0.25*m*m)/(m*m+4.0*x*x)*imSeq.evaluate(m), 1, n) ;
		Complex term3 = 2.0*coeff*(reSum.evaluate(50)+j*imSum.evaluate(50)) ;
		return term1 + term2 + term3 ;
	}

	public Complex erfc(Complex z) {
		return 1.0-erf(z) ;
	}

	// erfi(x) = -j*erf(j*x)
	public double erfi(double x) {
		double coeff = 1.0/PI ;
		double term2 = coeff*x ;
		imSeq = n -> n*sinh(n*x) ;
		imSum = n -> Series.sum(m -> exp(-0.25*m*m)/(m*m)*imSeq.evaluate(m), 1, n) ;
		double term3 = 2.0*coeff*imSum.evaluate(50) ;
		return term2 + term3 ;
	}

	public Complex erfi(Complex z) {
		return -j*erf(j*z);
	}

	public double erfcx(double x) {
		return exp(x*x)*erfc(x) ;
	}

	public Complex erfcx(Complex x) {
		return ComplexMath.exp(x*x)*erfc(x) ;
	}

	public Complex faddeeva(Complex z) {
		return ComplexMath.exp(-z*z)*erfc(-j*z) ;
	}

	// another name for faddeeva: w(z)
	public Complex wofz(Complex z) {
		return ComplexMath.exp(-z*z)*erfc(-j*z) ;
	}

	public Complex dawson(Complex z) {
		return sqrt(PI)/2.0 * ComplexMath.exp(-z*z) * erfi(z) ;
	}

	// does not work for |x|>14 ???
	public double dawson(double x) {
		return sqrt(PI)/2.0 * exp(-x*x) * erfi(x) ;
	}

	//************ derivatives **************

	public double erfDeriv(double x) {
		return 2.0/sqrt(PI)*exp(-x*x) ;
	}

	public Complex erfDeriv(Complex z) {
		return 2.0/sqrt(PI)*ComplexMath.exp(-z*z) ;
	}

	public Function<Double, Double> erfDeriv() {
		return x -> 2.0/sqrt(PI)*exp(-x*x) ;
	}

	public double erfIntegral(double a, double b) {
		return (b*erf(b)-a*erf(a))+(exp(-b*b)-exp(-a*a))/sqrt(PI) ;
	}

	public Function<Double, Double> erfIntegral() {
		return x -> x*erf(x)+exp(-x*x)/sqrt(PI) ;
	}

	public Function<Double, Double> erfIntegral(double c) {
		return x -> x*erf(x)+exp(-x*x)/sqrt(PI) + c ;
	}

	//******* inverse error function *******

	public double erfinv(double x) {
		if(abs(x)>=1.0)
			return Double.NaN ;
		funcErfInv = t -> erf(t)-x ;
		rootFinderErfInv = new RealRoot(funcErfInv) ;
		return rootFinderErfInv.newton(0.0, 100) ;
	}

	public double erfcinv(double x) {
		if(abs(x)>=2.0 || abs(x)<1e-10)
			return Double.NaN ;
		funcErfInv = t -> erfc(t)-x ;
		rootFinderErfInv = new RealRoot(funcErfInv) ;
		return rootFinderErfInv.newton(0.0, 100) ;
	}

	// sinc(t) = sin(t)/t
	private static double sinc(double x) {
		if (abs(x)<1e-10) {
			return 1.0 ;
		} else {
			return sin(x)/(x);
		}
	}

}

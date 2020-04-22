package mathLib.func.special;

import static java.lang.Math.abs;
import static java.lang.Math.log;
import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;

public class BetaFunction {

	GammaFunction gammaFunc ;

	public BetaFunction() {
		gammaFunc = new GammaFunction() ;
	}

	public double beta(double x, double y) {
		return gammaFunc.gamma(x)*gammaFunc.gamma(y)/gammaFunc.gamma(x+y) ;
	}

	public double betaln(double x, double y) {
		return log(abs(beta(x, y))) ;
	}

	public Complex beta(Complex x, Complex y) {
		return gammaFunc.gamma(x)*gammaFunc.gamma(y)/gammaFunc.gamma(x+y) ;
	}

	public double betaln(Complex x, Complex y) {
		return log(ComplexMath.abs(beta(x, y))) ;
	}

}

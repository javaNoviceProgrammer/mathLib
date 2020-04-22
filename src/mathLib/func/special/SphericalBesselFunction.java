package mathLib.func.special;

import static java.lang.Math.* ;
import static mathLib.numbers.Complex.* ;
import mathLib.numbers.Complex;

public class SphericalBesselFunction {

	private BesselFunction besselFunc ;
	private GammaFunction gammaFunc ;
	private double coeff = sqrt(0.5*PI) ;

	public SphericalBesselFunction() {
		besselFunc = new BesselFunction() ;
		gammaFunc = new GammaFunction() ;
	}

	public double jn(int n, double x) {
		if(abs(x)<1e-4)
			return coeff/sqrt(2.0)*pow(0.5*x, n)/gammaFunc.gamma(n+1.5) ;
		return coeff/sqrt(x)*besselFunc.jv(n+0.5, x) ;
	}

	public double yn(int n, double x) {
		return (n%2==0 ? -1 : 1)*coeff/sqrt(x)*besselFunc.jv(-n-0.5, x) ;
	}

	public Complex hankel1(int n, double x) {
		return jn(n, x) + j*yn(n, x) ;
	}

	public Complex hankel2(int n, double x) {
		return jn(n, x) - j*yn(n, x) ;
	}

}

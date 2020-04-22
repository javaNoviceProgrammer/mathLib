package mathLib.func.special;

import static java.lang.Math.* ;

public class LambdaFunction {

	GammaFunction gammaFunc ;
	BesselFunction besselFunc ;
	double lastv = 0.0 ;
	double lastGamma = 1.0 ;

	public LambdaFunction() {
		gammaFunc = new GammaFunction() ;
		besselFunc = new BesselFunction() ;
	}

	public double lmbda(double v, double x) {
		if(abs(x)<1e-10)
			return 1.0 ;
		if(abs(v-lastv)>1e-5) {
			lastv = v ;
			lastGamma = gammaFunc.gamma(v+1.0) ;
		}
		return lastGamma * besselFunc.jv(v, x)/pow(0.5*x, v) ;
	}

	public double lambda(double v, double x) {
		return lmbda(v, x) ;
	}

	public double jinc(double x) {
		if(abs(x)<1e-10)
			return 1.0 ;
		else
			return besselFunc.jn(1, x)/x ;
	}

	public double jincDeriv(double x) {
		if(abs(x)<1e-10)
			return 0.0 ;
		else
			return -besselFunc.jn(2, x)/x ;
	}

}

package mathLib.func.special;

import static java.lang.Math.* ;

/**
 * Airy Functions
 * 		solutions of ODE: y'' = xy
 *
 * @author Meisam
 *
 */
public class AiryFunction {

	double[] ai0 = {0.3550280538878173, -0.2588194037928068} ; // Ai(0), Ai'(0)
	double[] bi0 = {0.614926627446, 0.448288357353826} ; // Bi(0), Bi'(0)

	BesselFunction bessel ;
	ModifiedBesselFunction modifiedBessel ;

	public AiryFunction() {
		bessel = new BesselFunction() ;
		modifiedBessel = new ModifiedBesselFunction() ;
	}

	//********** Ai(z) **********************

	private double aiNegativeDouble(double x) {
		x = -x ;
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return sqrt(x)/3.0 * (bessel.jv(1.0/3.0, zeta)+bessel.jv(-1.0/3.0, zeta)) ;
	}

	private double aiPositiveDouble(double x) {
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return sqrt(x/3.0)/PI * modifiedBessel.kv(1.0/3.0, zeta) ;
	}

	public double ai(double x) {
		if(x>0.0) {
			return aiPositiveDouble(x) ;
		}
		else if(x<0.0)
			return aiNegativeDouble(x) ;
		else
			return ai0[0] ;
	}

	//********** Bi(z) **********************

	private double biPositiveDouble(double x) {
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return sqrt(x/3.0) * (modifiedBessel.iv(1.0/3.0, zeta) + modifiedBessel.iv(-1.0/3.0, zeta)) ;
	}

	private double biNegativeDouble(double x) {
		x = -x ;
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return sqrt(x/3.0) * (bessel.jv(-1.0/3.0, zeta) - bessel.jv(1.0/3.0, zeta)) ;
	}

	public double bi(double x) {
		if(x>0.0) {
			return biPositiveDouble(x) ;
		}
		else if(x<0.0)
			return biNegativeDouble(x) ;
		else
			return bi0[0] ;
	}

	//********** Ai'(z) **********************

	private double aiDerivPositiveDouble(double x) {
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return -x/(sqrt(3.0)*PI) * modifiedBessel.kv(2.0/3.0, zeta) ;
	}


	private double aiDerivNegativeDouble(double x) {
		x = -x ;
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return x/3.0 * (bessel.jv(2.0/3.0, zeta) - bessel.jv(-2.0/3.0, zeta)) ;
	}

	public double aiDeriv(double x) {
		if(x>0.0) {
			return aiDerivPositiveDouble(x) ;
		}
		else if(x<0.0)
			return aiDerivNegativeDouble(x) ;
		else
			return ai0[1] ;
	}

	//********** Bi'(z) **********************

	private double biDerivPositiveDouble(double x) {
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return x/sqrt(3.0) * (modifiedBessel.iv(2.0/3.0, zeta) + modifiedBessel.iv(-2.0/3.0, zeta)) ;
	}

	private double biDerivNegativeDouble(double x) {
		x = -x ;
		double zeta = 2.0/3.0 * pow(x, 1.5) ;
		return x/sqrt(3.0) * (bessel.jv(-2.0/3.0, zeta) + bessel.jv(2.0/3.0, zeta)) ;
	}

	public double biDeriv(double x) {
		if(x>0.0) {
			return biDerivPositiveDouble(x) ;
		}
		else if(x<0.0)
			return biDerivNegativeDouble(x) ;
		else
			return bi0[1] ;
	}

	//*************** Complex Ai(z) ********************

}

package mathLib.func.special;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import mathLib.sequence.Sequence;

public class StruveFunction {

	private Sequence hvSeq ;
	private double leadTerm ;
	private double val ;

//	private ComplexSequence jvSeqComplex ;
//	private Complex leadTermComplex ;
//	private Complex valComplex ;

	private GammaFunction gammaFunc ;
	private double lastv = 0.0 ;
	private double lastGammaH = 0.5*sqrt(PI) ;
	private double lastGammaK = sqrt(PI) ;
	private double coeff = 0.5*sqrt(PI) ;

	private BesselFunction besselFunc ;
	private ModifiedBesselFunction modifiedBesselFunc ;

	public StruveFunction() {
		gammaFunc = new GammaFunction() ;
		besselFunc = new BesselFunction() ;
		modifiedBesselFunc = new ModifiedBesselFunction() ;
	}

	//********* power series for Hv(z) *********************

	// struve function Hv(x)
	private double hvSmallArgument(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = pow(x/2.0, v+1) / (coeff*lastGammaH) ;
		}
		else {
			lastv = v ;
			lastGammaH = gammaFunc.gamma(v+1.5) ;
			leadTerm = pow(x/2.0, v+1) / (coeff*lastGammaH) ;
		}
		val = (0.5*x)*(0.5*x) ;
		hvSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 - val/((i+0.5)*(i+0.5+v))*result ;
			}
			return result ;
		} ;
		return hvSeq.evaluate(200)*leadTerm ;
	}

	// modified struve function Lv(x)
	private double lvSmallArgument(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = pow(x/2.0, v+1) / (coeff*lastGammaH) ;
		}
		else {
			lastv = v ;
			lastGammaH = gammaFunc.gamma(v+1.5) ;
			leadTerm = pow(x/2.0, v+1) / (coeff*lastGammaH) ;
		}
		val = (0.5*x)*(0.5*x) ;
		hvSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 + val/((i+0.5)*(i+0.5+v))*result ;
			}
			return result ;
		} ;
		return hvSeq.evaluate(200)*leadTerm ;
	}

	private double kvLargeArgument(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = pow(x/2.0, v-1) / (2.0*coeff*lastGammaK) ;
		}
		else {
			lastv = v ;
			lastGammaK = gammaFunc.gamma(v+0.5) ;
			leadTerm = pow(x/2.0, v-1) / (2.0*coeff*lastGammaK) ;
		}
		val = (0.5*x)*(0.5*x) ;
		hvSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 + (i-0.5)*(v+0.5-i)/val*result ;
			}
			return result ;
		} ;
		return hvSeq.evaluate(25)*leadTerm ;
	}

	private double mvLargeArgument(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = -pow(x/2.0, v-1) / (2.0*coeff*lastGammaK) ;
		}
		else {
			lastv = v ;
			lastGammaK = gammaFunc.gamma(v+0.5) ;
			leadTerm = -pow(x/2.0, v-1) / (2.0*coeff*lastGammaK) ;
		}
		val = (0.5*x)*(0.5*x) ;
		hvSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 - (i-0.5)*(v+0.5-i)/val*result ;
			}
			return result ;
		} ;
		return hvSeq.evaluate(25)*leadTerm ;
	}

	public double hv(double v, double x) {
		if(abs(x)<30.0)
			return hvSmallArgument(v, x) ;
		else
			return kvLargeArgument(v, x) + besselFunc.yv(v, x) ;
	}

	public double struveH(double v, double x) {
		if(abs(x)<30.0)
			return hvSmallArgument(v, x) ;
		else
			return kvLargeArgument(v, x) + besselFunc.yv(v, x) ;
	}

	public double kv(double v, double x) {
		if(abs(x)<30.0)
			return hvSmallArgument(v, x)-besselFunc.yv(v, x) ;
		else
			return kvLargeArgument(v, x) ;
	}

	public double struveK(double v, double x) {
		if(abs(x)<30.0)
			return hvSmallArgument(v, x)-besselFunc.yv(v, x) ;
		else
			return kvLargeArgument(v, x) ;
	}

	public double lv(double v, double x) {
		if(abs(x)<30.0)
			return lvSmallArgument(v, x) ;
		else
			return mvLargeArgument(v, x) + modifiedBesselFunc.iv(v, x) ;
	}


}

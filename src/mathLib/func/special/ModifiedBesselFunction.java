package mathLib.func.special;

import static java.lang.Math.*;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.sequence.ComplexSequence;
import mathLib.sequence.Sequence;

import static mathLib.numbers.Complex.* ;


/**
 * Modified Bessel Functions: Iv(x), Kv(x)
 * <p> Solutions of ODE: x^2 y'' + x y' - (x^2 + v^2) y = 0 </p>
 *
 * @author Meisam
 *
 */
public class ModifiedBesselFunction {

	Sequence ivSeq ;
	double leadTerm ;
	double val ;

	ComplexSequence ivSeqComplex ;
	Complex leadTermComplex ;
	Complex valComplex ;

	GammaFunction gammaFunc ;
	double lastv = 0.0 ;
	double lastGamma = 1.0 ;

	BesselFunction bessel ;

	public ModifiedBesselFunction() {
		bessel = new BesselFunction() ;
		gammaFunc = new GammaFunction() ;
	}

	//********* Modified Bessel of First Kind: Iv(x) *********************

	private double ivSeries(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = pow(x/2.0, v) / lastGamma ;
		}
		else {
			lastv = v ;
			lastGamma = gammaFunc.gamma(v+1.0) ;
			leadTerm = pow(x/2.0, v) / lastGamma ;
		}
		val = (0.5*x)*(0.5*x) ;
		ivSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 + val/(i*(i+v))*result ;
			}
			return result ;
		} ;
		return ivSeq.evaluate(200)*leadTerm ;
	}

	public double iv(double v, double x) {
		if(abs(v-(int)v)>=1e-2 && x<0.0) {
			System.out.println("iv(v,x) cannot be calculated for x<0 and "
					+ "non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		else if(abs(v-(int)v)<1e-2) {
			return iv((int)v, x) ;
		}
		else {
			return ivSeries(v, x) ;
		}
	}

	public double iv(int v, double x) {
		if(v>0) {
			return ivSeries(v, x) ;
		}
		else {
			return ivSeries(-v, x) ;
		}
	}

	public double in(int n, double x) {
		return iv(n, x) ;
	}

	private Complex ivSeries(double v, Complex x) {
		if(x.re()<=0.0 && x.im()<0.0)
			return ivSeries(v, x.conjugate()).conjugate() ;
		else if(x.re()>=0.0 && x.im()<0.0)
			return ivSeries(v, x.conjugate()).conjugate() ;
		if(abs(v-lastv)<1e-2) {
			leadTermComplex = ComplexMath.pow(x/2.0, v) / lastGamma ;
		}
		else {
			lastv = v ;
			lastGamma = gammaFunc.gamma(v+1.0) ;
			leadTermComplex = ComplexMath.pow(x/2.0, v) / lastGamma ;
		}
		valComplex = (0.5*x)*(0.5*x) ;
		ivSeqComplex = n -> {
			Complex result = 1.0+0.0*j ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 + valComplex/(i*(i+v))*result ;
			}
			return result ;
		} ;
		return ivSeqComplex.evaluate(200)*leadTermComplex ;
	}

	public Complex iv(double v, Complex x) {
		return ivSeries(v, x) ;
	}

	public Complex iv(int v, Complex x) {
		return iv((double)v, x) ;
	}

	public Complex in(int n, Complex x) {
		return iv(n, x) ;
	}

	//********* Modified Bessel of Second Kind: Kv(x) *********************

	private double kvSmallArgument(double v, double x) {
		if(abs(v-(int)v)>1e-2 && x<0.0) {
			System.out.println("kv(v,x) cannot be calculated for x<0 "
					+ "and non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		if(abs(v-(int)v)>1e-2) {
			return PI/2.0*(iv(-v, x)-iv(v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return PI/2.0*(iv(-(v-eps), x)-iv(v-eps, x))/sin(PI*(v-eps)) ;
		}
	}

	private Complex kvSmallArgument(double v, Complex x) {
		if(abs(v-(int)v)>1e-2) {
			return PI/2.0*(iv(-v, x)-iv(v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return PI/2.0*(iv(-(v-eps), x)-iv(v-eps, x))/sin(PI*(v-eps)) ;
		}
	}

	private double kvLargeArgument(double v, double x) {
		if(abs(v-(int)v)>1e-2 && x<0.0) {
			System.out.println("kv(v,x) cannot be calculated for x<0 "
					+ "and non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		double lead = sqrt(0.5*PI/x)*exp(-x) ;
		double a0 = 1.0 ;
		double a1 = 4.0*v*v-1.0 ;
		double a2 = 4.0*v*v-9.0 ;
		double a3 = 4.0*v*v-25.0 ;
		double terms = a0 + a0*a1/(8.0*x) + a0*a1*a2/(2.0*64*x*x) + a0*a1*a2*a3/(6.0*512.0*x*x*x) ;
		return lead*terms ;

	}

	private Complex kvLargeArgument(double v, Complex x) {
		Complex lead = ComplexMath.sqrt(0.5*PI/x)*ComplexMath.exp(-x) ;
		double a0 = 1.0 ;
		double a1 = 4.0*v*v-1.0 ;
		double a2 = 4.0*v*v-9.0 ;
		double a3 = 4.0*v*v-25.0 ;
		Complex terms = a0 + a0*a1/(8.0*x) + a0*a1*a2/(2.0*64*x*x) + a0*a1*a2*a3/(6.0*512.0*x*x*x) ;
		if(x.re()>=0.0 && x.im()>0.0)
			return -lead*terms ;
		else if(x.re()<0.0 && x.im()>0.0)
			return -lead*terms ;
		return lead*terms ;
	}

	public double kv(double v, double x) {
		if(abs(v-(int)v)>1e-2 && x<0.0) {
			System.out.println("kv(v,x) cannot be calculated for x<0 "
					+ "and non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		if(abs(x)<2.5)
			return kvSmallArgument(v, x) ;
		else
			return kvLargeArgument(v, x) ;
	}

	public Complex kv(double v, Complex x) {
		if(ComplexMath.abs(x)<2.5)
			return kvSmallArgument(v, x) ;
		else
			return kvLargeArgument(v, x) ;
	}

	public double kn(int n, double x) {
		return kv((double)n, x) ;
	}

	public Complex kn(int n, Complex x) {
		return kv((double)n, x) ;
	}

	//********* Derivative of Modified Bessel Functions *********************

//	public double ivDeriv(double v, double x) {
//		return 0.5*(iv(v-1, x)-iv(v+1, x)) ;
//	}
//
//	public Complex ivDeriv(double v, Complex x) {
//		return 0.5*(iv(v-1, x)-iv(v+1, x)) ;
//	}
//
//	public double kvDeriv(double v, double x) {
//		return 0.5*(kv(v-1, x)-kv(v+1, x)) ;
//	}
//
//	public Complex kvDeriv(double v, Complex x) {
//		return 0.5*(kv(v-1, x)-kv(v+1, x)) ;
//	}

}

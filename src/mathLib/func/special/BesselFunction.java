package mathLib.func.special;

import static java.lang.Math.*;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.sequence.ComplexSequence;
import mathLib.sequence.Sequence;

import static mathLib.numbers.Complex.* ;


/**
 * Bessel Functions: Jv(x), Yv(x)
 * 		solutions of ODE: x^2 y'' + x y' + (x^2 - v^2) y = 0
 *
 * @author Meisam
 *
 */
public class BesselFunction {

	private Sequence jvSeq ;
	private double leadTerm ;
	private double val ;

	private ComplexSequence jvSeqComplex ;
	private Complex leadTermComplex ;
	private Complex valComplex ;

	GammaFunction gammaFunc = new GammaFunction() ;
	private double lastv = 0.0 ;
	private double lastGamma = 1.0 ;

	public BesselFunction() {

	}

	//********* power series for Jv(z) *********************

	private double jvSmallArgument(double v, double x) {
		if(abs(v-lastv)<1e-2) {
			leadTerm = pow(x/2.0, v) / lastGamma ;
		}
		else {
			lastv = v ;
			lastGamma = gammaFunc.gamma(v+1.0) ;
			leadTerm = pow(x/2.0, v) / lastGamma ;
		}
		val = (0.5*x)*(0.5*x) ;
		jvSeq = n -> {
			double result = 1.0 ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 - val/(i*(i+v))*result ;
			}
			return result ;
		} ;
		return jvSeq.evaluate(200)*leadTerm ;
	}

	private double jvLargeArgument(double v, double x) {
		double t = 1.0/x ;
		double p = v*v-0.25 ;
		double a2 = 1.0/4.0*p ;
		double a4 = 5.0/32.0*p*p-3.0/8.0*p ;
		double a6 = 15.0/128.0*p*p*p-37.0/32.0*p*p+15.0/8.0*p ;
		double a8 = 195.0/2048.0*p*p*p*p-611.0/256.0*p*p*p+1821.0/128.0*p*p-315.0/16.0*p ;
		double a10 = 663.0/8192.0*p*p*p*p*p-4199.0/1024.0*p*p*p*p+29811.0/512.0*p*p*p-2223.0/8.0*p*p+2835.0/8.0*p ;
		double b2 = 1.0/2.0*p ;
		double b4 = 1.0/24.0*p*p ;
		double b6 = 1.0/80.0*p*p*p-7.0/20.0*p*p+3.0/4.0*p ;
		double b8 = 1.0/1792.0*p*p*p*p-95.0/224.0*p*p*p+807.0/224.0*p*p-315.0/56.0*p ;
		double b10 = 7.0/2304.0*p*p*p*p*p-35.0/72.0*p*p*p*p+1975.0/192.0*p*p*p-58.0*p*p+315.0/4.0*p ;
		double polyAmp = 1.0+a2*t*t+a4*pow(t,4.0)+a6*pow(t,6.0)+a8*pow(t,8.0)+a10*pow(t,10.0) ;
		double polyPhi = 1.0+b2*t*t+b4*pow(t,4.0)+b6*pow(t,6.0)+b8*pow(t,8.0)+b10*pow(t,10.0) ;
		if(t>0.0){
			return sqrt(2.0*t/PI)*polyAmp*cos(polyPhi/t-(v+0.5)*PI/2.0) ;
		}
		else {
			// only for v = n (integer v)
			return sqrt(-2.0*t/PI)*polyAmp*cos(polyPhi/t-(v+0.5)*PI/2.0+0.5*PI) ;
		}
	}

	public double jv(double v, double x) {
		if(abs(v-(int)v)>=1e-2 && x<0.0) {
			System.out.println("jv(v,x) cannot be calculated for x<0 and non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		else if(abs(v-(int)v)<1e-2) {
			return jv((int)v, x) ;
		}
		else {
			if(abs(x)<30.0)
				return jvSmallArgument(v, x) ;
			else
				return jvLargeArgument(v, x) ;
		}
	}

	public double jv(int v, double x) {
		if(v>0) {
			if(abs(x)<30.0)
				return jvSmallArgument(v, x) ;
			else
				return jvLargeArgument(v, x) ;
		}
		else {
			if(abs(x)<30.0)
				return ((-v)%2==0? 1 : -1) * jvSmallArgument(-v, x) ;
			else
				return ((-v)%2==0? 1 : -1) * jvLargeArgument(-v, x) ;
		}
	}

	public double jn(int n, double x) {
		return jv(n, x) ;
	}

	private Complex jvSmallArgument(double v, Complex x) {
		if(x.re()<0.0 && x.im()<0.0)
			return jvSmallArgument(v, x.conjugate()).conjugate() ;
		else if(x.re()>0.0 && x.im()<0.0)
			return jvSmallArgument(v, x.conjugate()).conjugate() ;
		if(abs(v-lastv)<1e-2) {
			leadTermComplex = ComplexMath.pow(x/2.0, v) / lastGamma ;
		}
		else {
			lastv = v ;
			lastGamma = gammaFunc.gamma(v+1.0) ;
			leadTermComplex = ComplexMath.pow(x/2.0, v) / lastGamma ;
		}
		valComplex = (0.5*x)*(0.5*x) ;
		jvSeqComplex = n -> {
			Complex result = 1.0+0.0*j ;
			for(int i=(int)n; i>0; i--) {
				result = 1.0 - valComplex/(i*(i+v))*result ;
			}
			return result ;
		} ;
		return jvSeqComplex.evaluate(200)*leadTermComplex ;
	}

	private Complex jvLargeArgument(double v, Complex x) {
		Complex t = 1.0/x ;
		double p = v*v-0.25 ;
		double a2 = 1.0/4.0*p ;
		double a4 = 5.0/32.0*p*p-3.0/8.0*p ;
		double a6 = 15.0/128.0*p*p*p-37.0/32.0*p*p+15.0/8.0*p ;
		double a8 = 195.0/2048.0*p*p*p*p-611.0/256.0*p*p*p+1821.0/128.0*p*p-315.0/16.0*p ;
		double a10 = 663.0/8192.0*p*p*p*p*p-4199.0/1024.0*p*p*p*p+29811.0/512.0*p*p*p-2223.0/8.0*p*p+2835.0/8.0*p ;
		Complex polyAmp = 1.0+a2*t*t+a4*ComplexMath.pow(t,4.0)+a6*ComplexMath.pow(t,6.0)+a8*ComplexMath.pow(t,8.0)+a10*ComplexMath.pow(t,10.0) ;

		double b2 = 1.0/2.0*p ;
		double b4 = 1.0/24.0*p*p ;
		double b6 = 1.0/80.0*p*p*p-7.0/20.0*p*p+3.0/4.0*p ;
		double b8 = 1.0/1792.0*p*p*p*p-95.0/224.0*p*p*p+807.0/224.0*p*p-315.0/56.0*p ;
		double b10 = 7.0/2304.0*p*p*p*p*p-35.0/72.0*p*p*p*p+1975.0/192.0*p*p*p-58.0*p*p+315.0/4.0*p ;
		Complex polyPhi = 1.0+b2*t*t+b4*ComplexMath.pow(t,4.0)+b6*ComplexMath.pow(t,6.0)+b8*ComplexMath.pow(t,8.0)+b10*ComplexMath.pow(t,10.0) ;

		Complex result = ComplexMath.sqrt(2.0*t/PI)*polyAmp*ComplexMath.cos(polyPhi/t-(v+0.5)*PI/2.0) ;

		if(x.re()>=0.0 && x.im()>0.0)
			return -result ;
		else if(x.re()>=0.0 && x.im()<0.0)
			return result ;
		else if(x.re()<0.0 && x.im()>0.0)
			return ComplexMath.exp(j*PI*v)*jvLargeArgument(v, -x) ;
		else if(x.re()<0.0 && x.im()<0.0)
			return jvLargeArgument(v, x.conjugate()).conjugate() ;
		else if(x.re()>=0.0 && abs(x.im())<1e-10)
			return result ;
		else
			return ComplexMath.exp(j*PI*v)*jvLargeArgument(v, -x) ;

	}

	public Complex jv(double v, Complex x) {
		if(ComplexMath.abs(x)<30.0)
			return jvSmallArgument(v, x) ;
		else
			return jvLargeArgument(v, x) ;
	}

	public Complex jv(int v, Complex x) {
		return jv((double)v, x) ;
	}

	public Complex jn(int n, Complex x) {
		return jv(n, x) ;
	}

	public double jve(double v, double x) {
		return jv(v, x) ;
	}

	public Complex jve(double v, Complex x) {
		return jv(v, x) * exp(-abs(x.im())) ;
	}


	//********* Neumann function: Yv(z) *********************

	public double yv(double v, double x) {
		if(abs(v-(int)v)>1e-2 && x<0.0) {
			System.out.println("yv(v,x) cannot be calculated for x<0 "
					+ "and non-integer v. Pass x as a complex number."); // x -> x+0*j
			return Double.NaN ;
		}
		if(abs(v-(int)v)>1e-2) {
			return (jv(v, x)*cos(PI*v)-jv(-v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (jv(v-eps, x)*cos(PI*(v-eps))-jv(-(v-eps), x))/sin(PI*(v-eps)) ;
		}
	}

	public double yn(int n, double x) {
		return yv((double)n, x) ;
	}

	public Complex yv(double v, Complex x) {
		if(abs(v-(int)v)>1e-2) {
			return (jv(v, x)*cos(PI*v)-jv(-v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (jv(v-eps, x)*cos(PI*(v-eps))-jv(-(v-eps), x))/sin(PI*(v-eps)) ;
		}
	}

	public Complex yn(int n, Complex x) {
		return yv((double)n, x) ;
	}

	public double yve(double v, double x) {
		return yv(v, x) ;
	}

	public Complex yve(double v, Complex x) {
		return yv(v, x) * exp(-abs(x.im())) ;
	}

	//********* Hankel functions: Hv^(1) and Hv^(2) *********************

	public Complex hankel1(double v, double x) {
		if(abs(v-(int)v)>1e-2) {
			return (-j)*(jv(-v, x+0.0*j)-ComplexMath.exp(-j*PI*v)*jv(v, x+0.0*j))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (-j)*(jv(-(v-eps), x+0.0*j)-ComplexMath.exp(-j*PI*(v-eps))*
												jv(v-eps, x+0.0*j))/sin(PI*(v-eps)) ;
		}
	}

	public Complex hankel1(double v, Complex x) {
		if(abs(v-(int)v)>1e-2) {
			return (-j)*(jv(-v, x)-ComplexMath.exp(-j*PI*v)*jv(v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (-j)*(jv(-(v-eps), x)-ComplexMath.exp(-j*PI*(v-eps))*jv(v-eps, x))/sin(PI*(v-eps)) ;
		}
	}

	public Complex hankel1e(double v, double x) {
		return hankel1(v, x)*ComplexMath.exp(-j*x) ;
	}

	public Complex hankel1e(double v, Complex x) {
		return hankel1(v, x)*ComplexMath.exp(-j*x) ;
	}

	public Complex hankel2(double v, double x) {
		if(abs(v-(int)v)>1e-2) {
			return (j)*(jv(-v, x+0.0*j)-ComplexMath.exp(j*PI*v)*jv(v, x+0.0*j))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (j)*(jv(-(v-eps), x+0.0*j)-ComplexMath.exp(j*PI*(v-eps))*jv(v-eps, x+0.0*j))/sin(PI*(v-eps)) ;
		}
	}

	public Complex hankel2(double v, Complex x) {
		if(abs(v-(int)v)>1e-2) {
			return (j)*(jv(-v, x)-ComplexMath.exp(j*PI*v)*jv(v, x))/sin(PI*v) ;
		}
		else {
			double eps = 1e-2 ;
			return (j)*(jv(-(v-eps), x)-ComplexMath.exp(j*PI*(v-eps))*jv(v-eps, x))/sin(PI*(v-eps)) ;
		}
	}

	public Complex hankel2e(double v, double x) {
		return hankel2(v, x)*ComplexMath.exp(j*x) ;
	}

	public Complex hankel2e(double v, Complex x) {
		return hankel2(v, x)*ComplexMath.exp(j*x) ;
	}

	//********* Derivative of Bessel Functions *********************

	public double jvDeriv(double v, double x) { // Jv'(x)
		return 0.5*(jv(v-1, x)-jv(v+1, x)) ;
	}

	public Complex jvDeriv(double v, Complex x) { // Jv'(x)
		return 0.5*(jv(v-1, x)-jv(v+1, x)) ;
	}

	public double yvDeriv(double v, double x) { // yv'(x)
		return 0.5*(yv(v-1, x)-yv(v+1, x)) ;
	}

	public Complex yvDeriv(double v, Complex x) { // yv'(x)
		return 0.5*(yv(v-1, x)-yv(v+1, x)) ;
	}

	public Complex hankel1Deriv(double v, double x) { // Hv(1)' (x)
		return 0.5*(hankel1(v-1, x)-hankel1(v+1, x)) ;
	}

	public Complex hankel1Deriv(double v, Complex x) {
		return 0.5*(hankel1(v-1, x)-hankel1(v+1, x)) ;
	}

	public Complex hankel2Deriv(double v, double x) { // Hv(2)' (x)
		return 0.5*(hankel2(v-1, x)-hankel2(v+1, x)) ;
	}

	public Complex hankel2Deriv(double v, Complex x) {
		return 0.5*(hankel2(v-1, x)-hankel2(v+1, x)) ;
	}

}

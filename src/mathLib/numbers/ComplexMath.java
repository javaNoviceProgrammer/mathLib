package mathLib.numbers;

import static mathLib.numbers.Complex.j;

public final class ComplexMath {

	public static final Complex PI = new Complex(Math.PI, 0.0) ;
	public static final Complex E = new Complex(Math.E, 0.0) ;

	private ComplexMath() {
		// making the class private to prevent instantiation
	}
	
	public static Complex sin(double x) {
		return Math.sin(x) + 0.0 * j ;
	}
	
	public static Complex sin(Complex u) {
		return new Complex(Math.sin(u.re()) * Math.cosh(u.im()), Math.cos(u.re()) * Math.sinh(u.im()));
	}
	
	public static Complex cos(double x) {
		return Math.cos(x) + 0.0 * j ;
	}
	
	public static Complex cos(Complex u) {
		return new Complex(Math.cos(u.re()) * Math.cosh(u.im()), -Math.sin(u.re()) * Math.sinh(u.im()));
	}
	
	public static Complex tan(double x) {
		return Math.tan(x) + 0.0 * j ;
	}
	
	public static Complex tan(Complex u) {
		return sin(u).divides(cos(u));
	}
	
	public static Complex cot(double x) {
		return 1.0/Math.tan(x) + 0.0 * j ;
	}

	public static Complex cot(Complex u) {
		return tan(u).reciprocal() ;
	}
	
	public static Complex asin(double x) {
		return Math.abs(x) + 0.0 * j ;
	}
	
	public static Complex asin(Complex u) {
		Complex a = sqrt(u.times(u).times(-1).plus(1)) ;
		Complex b = u.times(Complex.plusJ) ;
		return Complex.minusJ.times(log(a.plus(b))) ;
	}
	
	public static Complex acos(double x) {
		return Math.acos(x) + 0.0 * j ;
	}
	
	public static Complex acos(Complex u) {
		return PI.divides(2).minus(asin(u)) ;
	}
	
	public static Complex atan(double x) {
		return Math.atan(x) + 0.0 * j ;
	}
	
	public static Complex atan(Complex u) {
		Complex a = u.times(Complex.minusJ).plus(1) ;
		Complex b = u.times(Complex.plusJ).plus(1) ;
		Complex c = Complex.plusJ.times(0.5) ;
		return log(a).minus(log(b)).times(c) ;
	}
	
	public static Complex acot(double x) {
		return Math.atan(1.0/x) + 0.0 * j ;
	}
	
	public static Complex acot(Complex u) {
		return atan(u.reciprocal()) ;
	}
	
	public static Complex toRadians(double angdeg) {
		return Math.toRadians(angdeg) + 0.0 * j ;
	}
	
	public static Complex toRadians(Complex angdeg) {
		return angdeg / 180.0 * PI ;
	}
	
	public static Complex toDegrees(double angrad) {
		return Math.toDegrees(angrad) + 0.0 * j ;
	}
	
	public static Complex toDegrees(Complex angrad) {
		return angrad * 180.0 / PI ;
	}
	
	public static Complex exp(double x) {
		return Math.exp(x) + 0.0 * j ;
	}
	
	public static Complex exp(Complex u) {
		return new Complex(Math.exp(u.re()) * Math.cos(u.im()), Math.exp(u.re()) * Math.sin(u.im()));
	}
	
	public static Complex log(double x) {
		return Math.log(x) + 0.0 * j ;
	}
	
	public static Complex log(Complex u) {
		double re = Math.log(abs(u)) ;
		double im = phaseMinusPiToPi(u) ;
		return new Complex(re, im) ;
	}
	
	public static Complex log10(double x) {
		return Math.log10(x) + 0.0 * j ;
	}
	
	public static Complex log10(Complex u) {
		return log(u)/Math.log(10.0) ;
	}
	
	public static Complex sqrt(double x) {
		if(x >= 0.0)
			return Math.sqrt(x) + 0.0 * j ;
		else
			return Math.sqrt(-x) * j ;
	}
	
	public static Complex sqrt(Complex u) {
		double real = Math.sqrt(abs(u)) * Math.cos(phase(u) / 2.0);
		double imag = Math.sqrt(abs(u)) * Math.sin(phase(u) / 2.0);
		return new Complex(real, imag);
	}
	
	public static Complex cbrt(double x) {
		return Math.cbrt(x) + 0.0 * j ;
	}
	
	public static Complex cbrt(Complex u) {
		double real = Math.cbrt(abs(u)) * Math.cos(phase(u) / 3.0);
		double imag = Math.cbrt(abs(u)) * Math.sin(phase(u) / 3.0);
		return new Complex(real, imag);
	}
	
	public static Complex ceil(double x) {
		return Math.ceil(x) + 0.0 * j ;
	}
	
	public static Complex ceil(Complex u) {
		return Math.ceil(u.re()) + j * Math.ceil(u.im()) ;
	}
	
	public static Complex floor(double x) {
		return Math.floor(x) + 0.0 * j ;
	}
	
	public static Complex floor (Complex u) {
		return Math.floor(u.re()) + j * Math.floor(u.im()) ;
	}
	
	public static Complex atan2(double y, double x) {
		return Math.atan2(y, x) + 0.0 * j ;
	}
	
	public static Complex pow(double a, double b) {
		return Math.pow(a, b) + 0.0 * j ;
	}
	
	public static Complex pow(Complex u, double p){
		return Math.pow(abs(u), p) * exp(j*phase(u)*p) ;
	}
	
	public static Complex pow(double u, Complex p) {
		return exp(p*log(u)) ;
	}
	
	public static Complex pow(Complex u, Complex p) {
		return exp(p*log(u)) ;
	}
	
	public static Complex round(double x) {
		return Math.round(x) + 0.0 * j ;
	}
	
	public static Complex round(Complex u) {
		return Math.round(u.re()) + j * Math.round(u.im()) ;
	}
	
	public static Complex random() {
		double real = Math.random() ;
		double imag = Math.random() ;
		double magnitude = Math.sqrt(real*real + imag*imag) ;
		return real/magnitude + j * imag/magnitude ;
	}

	public static double abs(Complex z) {
		return Math.hypot(z.re(), z.im()) ;
	}

	public static double absSquared(Complex z) {
		return (z.re() * z.re() + z.im() * z.im()) ;
	}
	
	public static Complex signum(double x) {
		return Math.signum(x) + 0.0 * j ;
	}
	
	public static Complex signum(Complex u) {
		return Math.signum(u.re()) + j * Math.signum(u.im()) ;
	}
	
	public static Complex sinh(double x) {
		return Math.sinh(x) + 0.0 * j ;
	}
	
	public static Complex sinh(Complex u) {
		return 0.5 * (exp(u) - exp(-u)) ;
	}
	
	public static Complex cosh(double x) {
		return Math.cosh(x) + 0.0 * j ;
	}
	
	public static Complex cosh(Complex u) {
		return 0.5 * (exp(u) + exp(-u)) ;
	}
	
	public static Complex tanh(double x) {
		return Math.tanh(x) + 0.0 * j ;
	}
	
	public static Complex tanh(Complex u) {
		return sinh(u)/cosh(u) ;
	}

	public static Complex conjugate(Complex u) {
		return new Complex(u.re(), -u.im());
	}

	public static double phase(Complex u) {
		double restrictedPhase = Math.atan2(u.im(), u.re());
		if (restrictedPhase >= 0) {
			return restrictedPhase;
		} else {
			return (restrictedPhase + 2 * Math.PI);
		}
	}

	public static double phaseMinusPiToPi(Complex u) {
		return Math.atan2(u.im(), u.re());
	}

	public static double phaseDegree(Complex u) {
		return phase(u) * 180 / Math.PI;
	}
	
	public static Complex secant(double x) {
		return 1.0/Math.cos(x) + 0.0 * j ;
	}

	public static Complex secant(Complex u) {
		return 1.0/cos(u) ;
	}
	
	public static Complex asec(double x) {
		return Math.acos(1.0/x) + 0.0 * j ;
	}

	public static Complex asec(Complex u) {
		return acos(u.reciprocal()) ;
	}
	
	public static Complex csc(double x) {
		return 1.0/Math.sin(x) + 0.0 * j ;
	}

	public static Complex csc(Complex u) {
		return sin(u).reciprocal() ;
	}
	
	public static Complex acsc(double x) {
		return Math.asin(1.0/x) + 0.0 * j ;
	}

	public static Complex acsc(Complex u) {
		return asin(u.reciprocal()) ;
	}

	// for test
	public static void main(String[] args) {
		Complex u = 10+Complex.j ;
		System.out.println(log(u));
		System.out.println(acos(u));
		System.out.println(atan(u/2));
		System.out.println(cot(u));
		System.out.println(acot(u/2));
		System.out.println(secant(u));
		System.out.println(csc(u));
		System.out.println(acsc(u+1));
		System.out.println(asin(u));
		System.out.println(acsc(u+1));
		System.out.println(u/2);
		System.out.println(abs(exp(u-1)));
		System.out.println(cos(2.3));
		System.out.println(pow(j, j));
	}

}

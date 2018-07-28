package mathLib.numbers;

public class ComplexMath {

	public static final Complex PI = new Complex(Math.PI, 0) ;

	private ComplexMath() {
		// making the class private to prevent instantiation
	}

	public static double abs(Complex z) {
		return Math.hypot(z.re(), z.im());
	}

	public static double absSquared(Complex z) {
		return (z.re() * z.re() + z.im() * z.im());
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

	public double phaseDegree(Complex u) {
		return phase(u) * 180 / Math.PI;
	}

	public static Complex exp(Complex u) {
		return new Complex(Math.exp(u.re()) * Math.cos(u.im()), Math.exp(u.re()) * Math.sin(u.im()));
	}

	public static Complex log(Complex u) {
		double re = Math.log(abs(u)) ;
		double im = phaseMinusPiToPi(u) ;
		return new Complex(re, im) ;
	}

	public static Complex sin(Complex u) {
		return new Complex(Math.sin(u.re()) * Math.cosh(u.im()), Math.cos(u.re()) * Math.sinh(u.im()));
	}

	public static Complex asin(Complex u) {
		Complex a = sqrt(u.times(u).times(-1).plus(1)) ;
		Complex b = u.times(Complex.plusJ) ;
		return Complex.minusJ.times(log(a.plus(b))) ;
	}

	public static Complex cos(Complex u) {
		return new Complex(Math.cos(u.re()) * Math.cosh(u.im()), -Math.sin(u.re()) * Math.sinh(u.im()));
	}

	public static Complex acos(Complex u) {
		return PI.divides(2).minus(asin(u)) ;
	}

	public static Complex tan(Complex u) {
		return sin(u).divides(cos(u));
	}

	public static Complex atan(Complex u) {
		Complex a = u.times(Complex.minusJ).plus(1) ;
		Complex b = u.times(Complex.plusJ).plus(1) ;
		Complex c = Complex.plusJ.times(0.5) ;
		return log(a).minus(log(b)).times(c) ;
	}

	public static Complex cot(Complex u) {
		return tan(u).reciprocal() ;
	}

	public static Complex acot(Complex u) {
		return atan(u.reciprocal()) ;

	}

	public static Complex secant(Complex u) {
		return cos(u).reciprocal() ;
	}

	public static Complex asec(Complex u) {
		return acos(u.reciprocal()) ;
	}

	public static Complex csc(Complex u) {
		return sin(u).reciprocal() ;
	}

	public static Complex acsc(Complex u) {
		return asin(u.reciprocal()) ;
	}

	public static Complex pow(Complex u, double p){
		return Math.pow(abs(u), p)*exp(Complex.j*phase(u)*p) ;
	}

	public static Complex sqrt(Complex u) {
		double real = Math.sqrt(abs(u)) * Math.cos(phase(u) / 2);
		double imag = Math.sqrt(abs(u)) * Math.sin(phase(u) / 2);
		return new Complex(real, imag);
	}

	// for test
/*	public static void main(String[] args) {
		Complex u = Complex.j ;
//		System.out.println(log(u));
//		System.out.println(acos(u));
//		System.out.println(atan(u/2));
//		System.out.println(cot(u));
//		System.out.println(acot(u/2));
//		System.out.println(secant(u));
//		System.out.println(csc(u));
		System.out.println(acsc(u+1));
//		System.out.println(asin(u));
//		System.out.println(acsc(u+1));
//		System.out.println(u/2);
//		System.out.println(abs(exp(u-1)));
	}*/

}

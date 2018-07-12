package utils;

public class ComplexMath {

	public static final Complex PI = new Complex(Math.PI, 0) ;

	public static double abs(Complex u) {
		return Math.hypot(u.re(), u.im());
	}

	public static double absSquared(Complex u) {
		return (u.re() * u.re() + u.im() * u.im());
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

	public static Complex sin(Complex u) {
		return new Complex(Math.sin(u.re()) * Math.cosh(u.im()), Math.cos(u.re()) * Math.sinh(u.im()));
	}

	public static Complex cos(Complex u) {
		return new Complex(Math.cos(u.re()) * Math.cosh(u.im()), -Math.sin(u.re()) * Math.sinh(u.im()));
	}

	public static Complex tan(Complex u) {
		return sin(u)/cos(u); // using operator overloading
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
	public static void main(String[] args) {
		Complex u = Complex.j ;
		System.out.println(u/2);
		System.out.println(abs(exp(u-1)));
	}

}

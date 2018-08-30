package mathLib.func;

import org.apache.commons.math3.special.Erf;

public class ErrorFunc {

	public static double erf(double x) {
		return Erf.erf(x) ;
	}

	public static double erfc(double x) {
		return Erf.erfc(x) ;
	}

	public static double erf(double x1, double x2) {
		return Erf.erf(x1, x2) ;
	}

	public static double erfInv(double x) {
		return Erf.erfInv(x) ;
	}

	public static double erfcInv(double x) {
		return Erf.erfcInv(x) ;
	}

}

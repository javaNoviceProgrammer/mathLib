package mathLib.polynom;

import mathLib.numbers.Complex;
import static mathLib.numbers.Complex.*;

public class ComplexPolynomial {

	private Complex[] coef; // coefficients (length = degree + 1)
	private int deg; // degree of polynomial (0 for the zero polynomial)

	// a0 + a1*x + a2*x^2 + ... an * x^n
	public ComplexPolynomial(Complex[] coeff) {
		coef = coeff;
		deg = degree();
	}

	// a * x^b
	public ComplexPolynomial(Complex a, int b) {
		coef = new Complex[b + 1];
		coef[b] = a;
		deg = degree();
	}

	// return the degree of this polynomial (0 for the zero polynomial)
	public int degree() {
		for (int i = coef.length - 1; i >= 0; i--)
			if (!coef[i].equals(0+j*0))
				return i;
		return 0;
	}

    // convert to string representation
    public String toString() {
        if (deg ==  0) return "" + coef[0];
        if (deg ==  1) return format(coef[1]) + " x + " + format(coef[0]);
        String s = format(coef[deg]) + " x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i].equals(ZERO)) continue;
            else s = s + " + " + format(coef[i]);
            if      (i == 1) s = s + " x";
            else if (i >  1) s = s + " x^" + i;
        }
        return s;
    }

    private String format(Complex c) {
    	return "(" + c + ")" ;
    }

	// for test
	public static void main(String[] args) {
		ComplexPolynomial p = new ComplexPolynomial(new Complex[]{j, -2+0*j, 3*(1+j)/2}) ;
		System.out.println(p);
	}

}

package utils;

public class Complex {

	private final double re; // the real part
	private final double im; // the imaginary part

	public static final Complex j = new Complex(0, 1);

	public Complex(double real, double imag) {
		re = real;
		im = imag;
	}

	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	public String toString() {
		if (im == 0)
			return re + "";
		if (re == 0)
			return "i" + im;
		if (im < 0)
			return re + "-" + "i" + (-im);
		return re + "+" + "i" + im;
	}

	public Complex plus(Complex b) {
		Complex a = this;
		double real = a.re + b.re;
		double imag = a.im + b.im;
		return new Complex(real, imag);
	}

	public Complex plus(double b) {
		Complex a = this;
		double real = a.re + b;
		double imag = a.im + 0;
		return new Complex(real, imag);
	}

	public Complex minus(double b) {
		Complex a = this;
		double real = a.re - b;
		double imag = a.im - 0;
		return new Complex(real, imag);
	}

	public Complex minus(Complex b) {
		Complex a = this;
		double real = a.re - b.re;
		double imag = a.im - b.im;
		return new Complex(real, imag);
	}

	public Complex times(Complex b) {
		Complex a = this;
		double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
		return new Complex(real, imag);
	}

	public Complex times(double alpha) {
		return new Complex(alpha * re, alpha * im);
	}

	public Complex reciprocal() {
		double scale = re * re + im * im;
		return new Complex(re / scale, -im / scale);
	}

	public Complex divides(Complex b) {
		Complex a = this;
		return a.times(b.reciprocal());
	}

	public Complex divides(double alpha) {
		Complex a = this;
		return a.times(1 / alpha);
	}

	public boolean equals(Complex b) {
		Complex a = this;
		if (a.re() == b.re() && a.im() == b.im())
			return true;
		else
			return false;
	}

	// ************ operator overloading **********************

	/**
	 * Operator overloading support:
	 *
	 * Object a = 5;
	 *
	 */
	public Complex valueOf(int v) {
		return new Complex(v, 0);
	}

	public Complex valueOf(long v) {
		return new Complex(v, 0);
	}

	public Complex valueOf(float v) {
		return new Complex(v, 0);
	}

	public Complex valueOf(double v) {
		return new Complex(v, 0);
	}

	public Complex valueOf(Complex v) {
		return new Complex(v.re, v.im);
	}

	/**
	 * Operator overload support: a+b
	 */
	public Complex add(Complex v) {
		return new Complex(this.re + v.re, this.im + v.im);
	}

	public Complex addRev(Complex v) {
		return new Complex(this.re + v.re, this.im + v.im);
	}

	public Complex add(int v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(int v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(long v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(long v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(float v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(float v) {
		return new Complex(v + this.re, this.im);
	}

	public Complex add(double v) {
		return new Complex(this.re + v, this.im);
	}

	public Complex addRev(double v) {
		return new Complex(v + this.re, this.im);
	}

	/**
	 * Operator overload support: a-b
	 */
	public Complex subtract(Complex v) {
		return new Complex(this.re - v.re, this.im - v.im);
	}

	public Complex subtractRev(Complex v) {
		return new Complex(v.re - this.re, v.im - this.im);
	}

	public Complex subtract(int v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(int v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(long v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(long v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(float v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(float v) {
		return new Complex(v - this.re, -this.im);
	}

	public Complex subtract(double v) {
		return new Complex(this.re - v, this.im);
	}

	public Complex subtractRev(double v) {
		return new Complex(v - this.re, -this.im);
	}

	/**
	 * Operator overload support: a*b
	 */
	public Complex multiply(Complex v) {
		return this.times(v);
	}

	public Complex multiplyRev(Complex v) {
		return v.times(this);
	}

	public Complex multiply(int v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(int v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(long v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(long v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(float v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(float v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiply(double v) {
		return new Complex(this.re * v, this.im * v);
	}

	public Complex multiplyRev(double v) {
		return new Complex(this.re * v, this.im * v);
	}

	/**
	 * Operator overload support: a/b
	 */
	public Complex divide(Complex v) {
		return this.divides(v);
	}

	public Complex divideRev(Complex v) {
		return v.divides(this);
	}

	public Complex divide(int v) {
		return this.divides(v);
	}

	public Complex divideRev(int v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(long v) {
		return this.divides(v);
	}

	public Complex divideRev(long v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(float v) {
		return this.divides(v);
	}

	public Complex divideRev(float v) {
		return this.reciprocal().times(v);
	}

	public Complex divide(double v) {
		return this.divides(v);
	}

	public Complex divideRev(double v) {
		return this.reciprocal().times(v);
	}

	/**
	 * Operator overload support: -a
	 */
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}

	public static void main(String[] args) {
		Complex u = 2.1 - j + 6;
		Complex v = -1 + u;
		Complex w = 1/ u ;
		System.out.println(u);
		System.out.println(v);
		System.out.println(w);
	}

}

package mathLib.polynom;

import static mathLib.numbers.Complex.ZERO;
import static mathLib.numbers.Complex.j;

import java.util.ArrayList;

import flanagan.complex.ComplexPoly;
import flanagan.math.Fmath;
import mathLib.numbers.Complex;
import mathLib.util.MathUtils;

public class ComplexPolynomial {

	public static final ComplexPolynomial Xc = new ComplexPolynomial(new Complex[] {0, 1}) ;

	Complex[] coef; // coefficients (length = degree + 1)
	int deg; // degree of polynomial (0 for the zero polynomial)

	// a0 + a1*x + a2*x^2 + ... an * x^n
	public ComplexPolynomial(Complex[] coeff) {
		coef = coeff;
		deg = degree();
	}
	
	public ComplexPolynomial(double[] coeff) {
		coef = new Complex[coeff.length];
		for(int i=0; i<coeff.length; i++)
			coef[i] = new Complex(coeff[i], 0.0) ;
		deg = degree();
	}

	// a * x^n
	public ComplexPolynomial(Complex a, int n) {
		coef = new Complex[n + 1];
		for(int i=0; i<coef.length; i++) {
			coef[i] = ZERO ;
		}
		coef[n] = a;
		deg = degree();
	}

	public ComplexPolynomial(double a, int b) {
		coef = new Complex[b + 1];
		for(int i=0; i<coef.length; i++) {
			coef[i] = ZERO ;
		}
		coef[b] = new Complex(a, 0.0);
		deg = degree();
	}

    public ComplexPolynomial() {
		this(new Complex[] {ZERO}) ;
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
        if (deg ==  1) {
        	if(!coef[0].equals(ZERO))
        		return format(coef[1]) + " x + " + format(coef[0]);
        	else
        		return format(coef[1]) + " x" ;
        }
//        if (deg ==  1) return format(coef[1]) + " x + " + format(coef[0]);
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

    // return c = a + b
    public ComplexPolynomial plus(ComplexPolynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i] + a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i] + b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial plus(Polynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i] + a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i] + b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial plus(Complex b) {
        Complex[] coeffSum = new Complex[coef.length] ;
        coeffSum[0] = coef[0] + b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSum[i] = coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    public ComplexPolynomial plus(double b) {
        Complex[] coeffSum = new Complex[coef.length] ;
        coeffSum[0] = coef[0] + b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSum[i] = coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    // return (a - b)
    public ComplexPolynomial minus(ComplexPolynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i] + a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i] - b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial minus(Polynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i] + a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i] - b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial minus(Complex b) {
        Complex[] coeffSum = new Complex[coef.length] ;
        coeffSum[0] = coef[0] - b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSum[i] = coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    public ComplexPolynomial minus(double b) {
        Complex[] coeffSum = new Complex[coef.length] ;
        coeffSum[0] = coef[0] - b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSum[i] = coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    // return (a * b)
    public ComplexPolynomial times(ComplexPolynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] = c.coef[i+j] + (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial times(Polynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial(ZERO, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] = c.coef[i+j] + (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        return c;
    }

    public ComplexPolynomial times(Complex b) {
    	Complex[] coeffSum = new Complex[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = b*coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    public ComplexPolynomial times(double b) {
    	Complex[] coeffSum = new Complex[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = b*coef[i] ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    // return (a / b)
    public ComplexPolynomial divides(ComplexPolynomial b) {
    	return null ;
    }

    public ComplexPolynomial divides(Complex b) {
    	if(b.equals(ZERO))
    		throw new IllegalArgumentException("cannot divide by zero!") ;
    	Complex[] coeffSum = new Complex[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = coef[i]/b ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    public ComplexPolynomial divides(double b) {
    	if(b==0)
    		throw new IllegalArgumentException("cannot divide by zero!") ;
    	Complex[] coeffSum = new Complex[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = coef[i]/b ;
        }
        return new ComplexPolynomial(coeffSum) ;
    }

    public ComplexPolynomial pow(int m) {
    	ComplexPolynomial a = this ;
    	ComplexPolynomial p = new ComplexPolynomial(new Complex[] {1.0}) ;
        for(int i=0; i<m; i++) {
        	p = p.times(a) ;
        }
        return p ;
    }

    // return a(b(x))  - compute using Horner's method
    public ComplexPolynomial compose(ComplexPolynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial();
        for (int i = a.deg; i >= 0; i--) {
        	ComplexPolynomial term = new ComplexPolynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }

    public ComplexPolynomial compose(Polynomial b) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial c = new ComplexPolynomial();
        for (int i = a.deg; i >= 0; i--) {
        	ComplexPolynomial term = new ComplexPolynomial(a.coef[i], 0);
            c = term.plus(c.times(b));
        }
        return c;
    }

    // do a and b represent the same polynomial?
    public boolean equals(ComplexPolynomial b) {
    	ComplexPolynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i].equals(b.coef[i])) return false;
        return true;
    }

    @Override
	public boolean equals(Object obj) {
    	ComplexPolynomial a = this;
    	ComplexPolynomial b = (ComplexPolynomial) obj ;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i].equals(b.coef[i])) return false;
        return true;
	}

	// use Horner's method to compute and return the polynomial evaluated at x
    public Complex evaluate(Complex x) {
    	Complex p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }

    public Complex evaluate(double x) {
    	Complex p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }

    // differentiate this polynomial and return it
    public ComplexPolynomial diff() {
        if (deg == 0) return new ComplexPolynomial(0, 0);
        ComplexPolynomial deriv = new ComplexPolynomial(0.0, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;
    }

    public ComplexPolynomial diff(int order) {
        if (deg == 0) return new ComplexPolynomial();
        ComplexPolynomial deriv = new ComplexPolynomial();
        deriv = deriv.plus(this) ;
        for (int i = 0; i < order; i++)
            deriv = deriv.diff() ;
        return deriv;
    }

    // integrate this polynomial and return it
    public ComplexPolynomial integrate() {
        if (deg == 0) return new ComplexPolynomial(new Complex[] {0.0, 1.0});
        ComplexPolynomial integral = new ComplexPolynomial(0, deg + 1);
        integral.deg = deg + 1;
        for (int i = 1; i < deg+2; i++)
        	integral.coef[i] = coef[i - 1] / i ;
        return integral;
    }

    public ComplexPolynomial integrate(int order) {
    	ComplexPolynomial integral = new ComplexPolynomial() ;
    	integral = integral.plus(this) ;
    	for(int i=0; i<order; i++) {
    		integral = integral.integrate() ;
    	}
    	return integral ;
    }

    public Complex integrate(Complex xStart, Complex xEnd) {
    	return integrate().evaluate(xEnd)-integrate().evaluate(xStart) ;
    }

    public Complex integrate(double xStart, double xEnd) {
    	return integrate().evaluate(xEnd)-integrate().evaluate(xStart) ;
    }

    public Complex[] getRoots() {
    	Complex[] roots = new Complex[degree()] ;
    	flanagan.complex.Complex[] rootsCalc = toFlanaganComplexPoly(this).rootsNoMessages() ;
    	for(int i=0; i<roots.length; i++)
    		roots[i] = new Complex(rootsCalc[i].getReal(), rootsCalc[i].getImag()) ;
    	return roots ;
    }

    public static ComplexPoly toFlanaganComplexPoly(ComplexPolynomial p) {
    	flanagan.complex.Complex[] coeffs = new flanagan.complex.Complex[p.degree()+1] ;
    	for(int i=0; i<coeffs.length; i++)
    		coeffs[i] = new flanagan.complex.Complex(p.coef[i].re(), p.coef[i].im()) ; ;
    	return new ComplexPoly(coeffs) ;
    }
    
    public ArrayList<ComplexPolynomial> getFactors() {
    	Complex[] roots = getRoots() ;
    	ArrayList<ComplexPolynomial> factors = new ArrayList<>() ;
    	for(Complex root : roots) {
    		if(Math.abs(root.im())<1e-10) {
    			ComplexPolynomial p = Xc - Fmath.truncate(root.re(), 5) ;
    			factors.add(p) ;
    		}
    		else {
    			ComplexPolynomial p = Xc - root ;
    			factors.add(p) ;
    		}
    	}
    	
    	return factors ;
    }

	// ************ operator overloading **********************

	/**
	 * Operator overloading support:
	 *
	 * Object a = 5;
	 *
	 */
	public static ComplexPolynomial valueOf(int v) {
		return new ComplexPolynomial(v, 0);
	}

	public static ComplexPolynomial valueOf(long v) {
		return new ComplexPolynomial(v, 0);
	}

	public static ComplexPolynomial valueOf(float v) {
		return new ComplexPolynomial(v, 0);
	}

	public static ComplexPolynomial valueOf(double v) {
		return new ComplexPolynomial(v, 0);
	}

	public static ComplexPolynomial valueOf(ComplexPolynomial v) {
		return new ComplexPolynomial(v.coef);
	}

	public static ComplexPolynomial valueOf(Polynomial v) {
		Complex[] coeffs = new Complex[v.coef.length] ;
		for(int i=0; i<coeffs.length; i++) {
			coeffs[i] = new Complex(v.coef[i], 0.0) ;
		}
		return new ComplexPolynomial(coeffs);
	}

	/**
	 * Operator overload support: a+b
	 */
	public ComplexPolynomial add(ComplexPolynomial v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(ComplexPolynomial v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(Polynomial v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(Polynomial v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(int v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(int v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(long v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(long v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(float v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(float v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(double v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(double v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial add(Complex v) {
		return this.plus(v) ;
	}

	public ComplexPolynomial addRev(Complex v) {
		return this.plus(v) ;
	}

	/**
	 * Operator overload support: a-b
	 */
	public ComplexPolynomial subtract(ComplexPolynomial v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(ComplexPolynomial v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(Polynomial v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(Polynomial v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(int v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(int v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(long v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(long v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(float v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(float v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(double v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(double v) {
		return this.times(-1).plus(v) ;
	}

	public ComplexPolynomial subtract(Complex v) {
		return this.minus(v) ;
	}

	public ComplexPolynomial subtractRev(Complex v) {
		return this.times(-1).plus(v) ;
	}

	/**
	 * Operator overload support: a*b
	 */
	public ComplexPolynomial multiply(ComplexPolynomial v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(ComplexPolynomial v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(Polynomial v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(Polynomial v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(int v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(int v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(long v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(long v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(float v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(float v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(double v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(double v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiply(Complex v) {
		return this.times(v) ;
	}

	public ComplexPolynomial multiplyRev(Complex v) {
		return this.times(v) ;
	}

	/**
	 * Operator overload support: a/b
	 */
	public ComplexPolynomial divide(ComplexPolynomial v) {
		return null ;
	}

	public ComplexPolynomial divideRev(ComplexPolynomial v) {
		return null ;
	}

	public ComplexPolynomial divide(Polynomial v) {
		return null ;
	}

	public ComplexPolynomial divideRev(Polynomial v) {
		return null ;
	}

	public ComplexPolynomial divide(int v) {
		return this.divides(v) ;
	}

	public ComplexPolynomial divideRev(int v) {
		return null ;
	}

	public ComplexPolynomial divide(long v) {
		return this.divides(v) ;
	}

	public ComplexPolynomial divideRev(long v) {
		return null ;
	}

	public ComplexPolynomial divide(float v) {
		return this.divides(v) ;
	}

	public ComplexPolynomial divideRev(float v) {
		return null ;
	}

	public ComplexPolynomial divide(double v) {
		return this.divides(v) ;
	}

	public ComplexPolynomial divideRev(double v) {
		return null ;
	}

	public ComplexPolynomial divide(Complex v) {
		return this.divides(v) ;
	}

	public ComplexPolynomial divideRev(Complex v) {
		return null ;
	}

	// for test
	public static void main(String[] args) {
		ComplexPolynomial p = new ComplexPolynomial(new Complex[]{1,1,1,1}) ;
		System.out.println(p);
//		System.out.println(p.evaluate(j));
//		System.out.println(p.diff());
//		System.out.println(p.diff(5));
//		System.out.println(p.integrate());
//		System.out.println(p.integrate(ZERO, 2+0*j));
//		ComplexPolynomial q = Polynomial.X ;
//		System.out.println(q+j);
//		System.out.println(q/2*j+j);
//		System.out.println((q/2*j+j).compose(q*q+j/2));

		MathUtils.Arrays.show(p.getRoots());

		Polynomial q = new Polynomial(new double[]{1,1,1,1}) ;
		MathUtils.Arrays.show(q.getRoots());
		
		System.out.println(p.getFactors());
		System.out.println(q.getFactors());
	}

}

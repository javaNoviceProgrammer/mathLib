package mathLib.polynom;

import mathLib.numbers.Complex;

/******************************************************************************
 *  Compilation:  javac Polynomial.java
 *  Execution:    java Polynomial
 *
 *  Polynomials with complex coefficients.
 *
 *  % java Polynomial
 *  zero(x) =     0
 *  p(x) =        4x^3 + 3x^2 + 2x + 1
 *  q(x) =        3x^2 + 5
 *  p(x) + q(x) = 4x^3 + 6x^2 + 2x + 6
 *  p(x) * q(x) = 12x^5 + 9x^4 + 26x^3 + 18x^2 + 10x + 5
 *  p(q(x))     = 108x^6 + 567x^4 + 996x^2 + 586
 *  0 - p(x)    = -4x^3 - 3x^2 - 2x - 1
 *  p(3)        = 142
 *  p'(x)       = 12x^2 + 6x + 2
 *  p''(x)      = 24x + 6
 *
 ******************************************************************************/

public class ComplexPolynomial {
    private Complex[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)

    // a * x^b
    public ComplexPolynomial(Complex a, int b) {
        coef = new Complex[b+1];
        for (int i=0; i<coef.length; i++){
        	coef[i] = new Complex(0,0) ; // initializing all the complex elements to zero
        }
        coef[b] = a;
        deg = degree();
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        int d = 0;
        Complex zero = new Complex(0,0) ;
        for (int i = 0; i < coef.length; i++){
            if (coef[i].equals(zero)) 
            	continue ;
            else
            	d = i ;
            }
        return d;
    }

    // return c = a + b
    public ComplexPolynomial plus(ComplexPolynomial b) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i].plus(a.coef[i]) ;
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i].plus(b.coef[i]) ;
        c.deg = c.degree();
        return c;
    }

    // return (a - b)
    public ComplexPolynomial minus(ComplexPolynomial b) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] = c.coef[i].plus(a.coef[i]) ;
        for (int i = 0; i <= b.deg; i++) c.coef[i] = c.coef[i].minus(b.coef[i]) ;
        c.deg = c.degree();
        return c;
    }

    // return (a * b)
    public ComplexPolynomial times(ComplexPolynomial b) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] = c.coef[i+j].plus(a.coef[i].times(b.coef[j]));
        c.deg = c.degree();
        return c;
    }
    
    // multiplication by complex number
    public ComplexPolynomial times(Complex alpha) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, a.deg);
        for (int i = 0; i <= a.deg; i++)
                c.coef[i] = a.coef[i].times(alpha);
        c.deg = c.degree();
        return c;
    }
    
    // multiplication by double number
    public ComplexPolynomial times(double alpha) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, a.deg);
        for (int i = 0; i <= a.deg; i++)
                c.coef[i] = a.coef[i].times(alpha);
        c.deg = c.degree();
        return c;
    }

    // return a(b(x))  - compute using Horner's method
    public ComplexPolynomial compose(ComplexPolynomial b) {
    	Complex zero = new Complex(0,0) ;
        ComplexPolynomial a = this;
        ComplexPolynomial c = new ComplexPolynomial(zero, 0);
        for (int i = a.deg; i >= 0; i--) {
            ComplexPolynomial term = new ComplexPolynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }


    // do a and b represent the same polynomial?
    public boolean eq(ComplexPolynomial b) {
        ComplexPolynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (!a.coef[i].equals(b.coef[i])) return false;
        return true;
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public Complex evaluate(Complex x) {
        Complex p = new Complex(0,0) ;
        for (int i = deg; i >= 0; i--)
            p = coef[i].plus(x.times(p));
        return p;
    }
    
    public Complex evaluate(double x) {
        Complex p = new Complex(0,0) ;
        Complex y = new Complex(x,0) ;
        for (int i = deg; i >= 0; i--)
            p = coef[i].plus(y.times(p));
        return p;
    }

    // differentiate this polynomial and return it
    public ComplexPolynomial differentiate() {
    	Complex zero = new Complex(0,0) ;
        if (deg == 0) {return new ComplexPolynomial(zero, 0);}
        ComplexPolynomial deriv = new ComplexPolynomial(zero, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = coef[i + 1].times(i+1);
        return deriv;
    }

    // convert to string representation
    public String toString() {
    	Complex zero = new Complex(0,0) ;
        if (deg ==  0) return "(" + coef[0] + ")";
        if (deg ==  1) return "("+coef[1]+")" + "x + " + "("+coef[0]+")";
        String s = "("+coef[deg]+")" + "x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i].equals(zero)) continue;
            s = s + " + " + "("+coef[i]+")" ;
            if      (i == 1) s = s + "x";
            else if (i >  1) s = s + "x^" + i;
        }
        return s;
    }

    //*******************************************************************
    // test client
/*    public static void main(String[] args) { 

    	Complex a3 = new Complex(2,-3), a2 = new Complex(0,3), a1 = new Complex(0,0), a0 = new Complex(-0.1, 10) ;
    	ComplexPolynomial p = new ComplexPolynomial(a3,3).plus(new ComplexPolynomial(a2,2)).plus(new ComplexPolynomial(a1, 1)).plus(new ComplexPolynomial(a0, 0)) ;
        
        System.out.println("p(x) = " + p);
        System.out.println("p(j) = "+ p.evaluate(new Complex(0,1)));
        System.out.println("p'''(x) = " + p.differentiate().differentiate().differentiate()); 
        System.out.println("p(x)*j = " + p.times(new Complex(0,1)));
   }*/
  //*******************************************************************
}


package mathLib.polynom;

public class Polynomial {
    private double[] coef;  // coefficients (length = degree + 1)
    private int deg;     // degree of polynomial (0 for the zero polynomial)

    // a0 + a1*x + a2*x^2 + ... an * x^n
    public Polynomial(double... coeff) {
    	this.coef = coeff ;
    	this.deg = degree() ;
    }
    
    // a * x^b
    public Polynomial(double a, int b) {
        coef = new double[b+1];
        coef[b] = a;
        deg = degree();
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        for (int i = coef.length-1; i >=0; i--)
            if (coef[i] != 0) return i;
        return 0;
    }

    // return c = a + b
    public Polynomial plus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] += b.coef[i];
        c.deg = c.degree();
        return c;
    }
    
    // return c = a + b
    public Polynomial plus(double b) {
        double[] coeffSum = coef ;
        coeffSum[0] += b ;
        return new Polynomial(coeffSum) ;
    }

    // return (a - b)
    public Polynomial minus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] -= b.coef[i];
        c.deg = c.degree();
        return c;
    }

    // return (a * b)
    public Polynomial times(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] += (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        return c;
    }

    // return a(b(x))  - compute using Horner's method
    public Polynomial compose(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, 0);
        for (int i = a.deg; i >= 0; i--) {
            Polynomial term = new Polynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }


    // do a and b represent the same polynomial?
    public boolean equals(Polynomial b) {
        Polynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i] != b.coef[i]) return false;
        return true;
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double x) {
        double p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (deg == 0) return new Polynomial(0, 0);
        Polynomial deriv = new Polynomial(0, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;
    }
    
    // integrate this polynomial and return it
    public Polynomial integrate() {
        if (deg == 0) return new Polynomial(1);
        Polynomial integral = new Polynomial(0, deg + 1);
        integral.deg = deg + 1;
        for (int i = 1; i < deg+2; i++)
        	integral.coef[i] = coef[i - 1] / i ;
        return integral;
    }

    // convert to string representation
    public String toString() {
        if (deg ==  0) return "" + coef[0];
        if (deg ==  1) return coef[1] + " x + " + coef[0];
        String s = coef[deg] + " x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i] == 0) continue;
            else if (coef[i]  > 0) s = s + " + " + ( coef[i]);
            else if (coef[i]  < 0) s = s + " - " + (-coef[i]);
            if      (i == 1) s = s + " x";
            else if (i >  1) s = s + " x^" + i;
        }
        return s;
    }

    
    // for test
    public static void main(String[] args) {
		Polynomial p = new Polynomial(-1, 1.2, 2.3) ;
		System.out.println(p);
		System.out.println(p.plus(2.1));
		System.out.println(p.degree());
		System.out.println(p.differentiate());
		System.out.println(p.integrate());
	}
    
}


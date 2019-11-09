package mathLib.polynom;

import java.util.ArrayList;

import mathLib.numbers.Complex;
import mathLib.plot.MatlabChart;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;

public class Polynomial {

	public static final Polynomial X = new Polynomial(new double[] {0.0, 1.0}) ;
	public static final Polynomial x = new Polynomial(new double[] {0.0, 1.0}) ;
	public static final Polynomial ZERO = new Polynomial() ;

    double[] coef;  // coefficients (length = degree + 1)
    int deg;     // degree of polynomial (0 for the zero polynomial)

    // a0 + a1*x + a2*x^2 + ... an * x^n
    public Polynomial(double[] coeff) {
    	this.coef = coeff ;
    	this.deg = degree() ;
    }

    // a * x^b
    public Polynomial(double a, int b) {
        coef = new double[b+1];
        coef[b] = a;
        deg = degree();
    }

    public Polynomial() {
    	this(new double[] {0.0}) ;
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        for (int i = coef.length-1; i >=0; i--)
            if (coef[i] != 0) return i;
        return 0;
    }

    public double limit(double a) {
    	return this.evaluate(a) ;
    }

	public Polynomial reduce() {
		// remove zeros from higher order terms
		int index = deg ;
		while(Math.abs(coef[index])<1e-10)
			index-- ;
		double[] reducedCoeffs = new double[index+1] ;
		for(int i=0; i<index+1; i++)
			reducedCoeffs[i] = coef[i] ;
		return new Polynomial(reducedCoeffs) ;
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

    public Polynomial plus(double b) {
        double[] coeffSum = new double[coef.length] ;
        coeffSum[0] = coef[0] + b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSum[i] = coef[i] ;
        }
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

    public Polynomial minus(double b) {
        double[] coeffSub = new double[coef.length] ;
        coeffSub[0] = coef[0] - b ;
        for(int i=1; i<coef.length; i++) {
        	coeffSub[i] = coef[i] ;
        }
        return new Polynomial(coeffSub) ;
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

    public Polynomial times(double b) {
        double[] coeffSum = new double[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = b*coef[i] ;
        }
        return new Polynomial(coeffSum) ;
    }

    public Polynomial divides(Polynomial b) {
    	return null ;
    }

    public Polynomial divides(double b) {
    	if(b == 0)
    		throw new IllegalArgumentException("cannot divide by zero!") ;
        double[] coeffSum = new double[coef.length] ;
        for(int i=0; i<coef.length; i++) {
        	coeffSum[i] = coef[i]/b ;
        }
        return new Polynomial(coeffSum) ;
    }

    public Polynomial pow(int m) {
        Polynomial a = this ;
        Polynomial p = new Polynomial(new double[] {1.0}) ;
        for(int i=0; i<m; i++) {
        	p = p.times(a) ;
        }
        return p ;
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

    public boolean equals(Polynomial b, double tol) {
        Polynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (Math.abs(a.coef[i] - b.coef[i])>tol) return false;
        return true;
    }

    @Override
	public boolean equals(Object obj) {
        Polynomial a = this;
        Polynomial b = (Polynomial) obj ;
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
    public Polynomial diff() {
        if (deg == 0) return new Polynomial(0, 0);
        Polynomial deriv = new Polynomial(0, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;
    }

    public Polynomial diff(int order) {
    	if (order == 0) return this ;
        if (deg == 0) return new Polynomial(0, 0);
        Polynomial deriv = new Polynomial();
        deriv = deriv.plus(this) ;
        for (int i = 0; i < order; i++)
            deriv = deriv.diff() ;
        return deriv;
    }

    // integrate this polynomial and return it
    public Polynomial integrate() {
        if (deg == 0) return new Polynomial(new double[] {0.0, 1.0});
        Polynomial integral = new Polynomial(0, deg + 1);
        integral.deg = deg + 1;
        for (int i = 1; i < deg+2; i++)
        	integral.coef[i] = coef[i - 1] / i ;
        return integral;
    }

    public Polynomial integrate(int order) {
    	Polynomial integral = new Polynomial() ;
    	integral = integral.plus(this) ;
    	for(int i=0; i<order; i++) {
    		integral = integral.integrate() ;
    	}
    	return integral ;
    }

    public double integrate(double xStart, double xEnd) {
    	return integrate().evaluate(xEnd)-integrate().evaluate(xStart) ;
    }

    public void plot(double start, double end) {
    	double[] x = MathUtils.linspace(start, end, 1000) ;
    	double[] y = new double[x.length] ;
    	for(int i=0; i<x.length; i++)
    		y[i] = this.evaluate(x[i]) ;
    	MatlabChart fig = new MatlabChart() ;
    	fig.plot(x, y);
    	fig.renderPlot();
    	fig.setFigLineWidth(0, 2);
    	fig.run(true);
    }

    public Complex[] getRoots() {
    	if(degree() < 1)
    		return null ;
    	Complex[] roots = new Complex[deg] ;
    	double[] normalizedCoeff = new double[coef.length] ;
    	for(int i=0; i<coef.length; i++) {
    		normalizedCoeff[i] = coef[i]/coef[coef.length-1] ;
    	}
    	double[][] data = new double[deg][deg] ;
    	for(int i=0; i<deg-1; i++) {
    		data[i][i+1] = 1 ;
    	}
    	for(int i=0; i<deg; i++) {
    		data[deg-1][i] = -1 * normalizedCoeff[i] ;
    	}
    	Jama.Matrix companionMatrix = new Jama.Matrix(data) ;
    	Jama.EigenvalueDecomposition eigens = companionMatrix.eig() ;
    	double[] rootsRealPart = eigens.getRealEigenvalues() ;
    	double[] rootsImagPart = eigens.getImagEigenvalues() ;
    	for(int i=0; i<deg; i++) {
    		roots[i] = new Complex(rootsRealPart[i], rootsImagPart[i]) ;
    	}
    	return roots ;
    }

	public flanagan.math.Polynomial toFlanaganPolynomial() {
		flanagan.math.Polynomial p = new flanagan.math.Polynomial(this.coef) ;
		return p ;
	}

	public flanagan.complex.Complex[] roots() {
		return toFlanaganPolynomial().reducePoly().roots() ;
	}

    public ArrayList<Complex> getRootsAsList() {
    	ArrayList<Complex> roots = new ArrayList<>() ;
    	if(getRoots() != null) {
        	Complex[] allRoots = getRoots() ;
        	for(Complex r : allRoots)
        		roots.add(r) ;
        	return roots ;
    	}
    	else
    		return null ;
    }

    public static Polynomial fromFactors(ArrayList<Polynomial> factors) {
    	Polynomial p = 0*X + 1 ;
    	for(int i=0; i<factors.size(); i++)
    		p = p*factors.get(i) ;

    	return p ;
    }

    public static Polynomial fromRoots(ArrayList<Complex> roots) {
    	Polynomial p = 0*X + 1 ;
    	for(Complex root : roots) {
    		if(Math.abs(root.im())<1e-5) {
    			p = p * (X - root.re())  ;
    		}
    	}
    	for(Complex root : roots) {
    		if(root.im() > 1e-5) {
    			p = p * (X*X - 2*root.re()*X + root.absSquared()) ;
    		}
    	}

    	return p ;
    }

    // convert to string representation
    public String toString() {
        if (deg ==  0) return "" + format(coef[0]);
        if (deg ==  1) {
        	if(coef[0] > 0.0)
        		return format(coef[1]) + " x + " + format(coef[0]);
        	else if (coef[0] < 0.0)
        		return format(coef[1]) + " x - " + format(Math.abs(coef[0]));
        	else
        		return format(coef[1]) + " x" ;
        }
        String s = format(coef[deg]) + " x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i] == 0) continue;
            else if (coef[i]  > 0) s = s + " + " + (format(coef[i]));
            else if (coef[i]  < 0) s = s + " - " + (format(-coef[i]));
            if      (i == 1) s = s + " x";
            else if (i >  1) s = s + " x^" + i;
        }
        return s;
    }

    private String format(double c) {
    	if(c>1e-3)
    		return String.format("%.4f", c) ;
    	else
    		return String.format("%.8f", c) ;
    }

    public Polynomial copy() {
    	return new Polynomial(coef) ;
    }

    public ArrayList<Polynomial> getFactors() {
    	Complex[] roots = getRoots() ;
    	ArrayList<Polynomial> factors = new ArrayList<>() ;
    	for(Complex root : roots) {
    		if(Math.abs(root.im())<1e-5) {
    			Polynomial p = X - root.re() ;
    			factors.add(p) ;
    		}
    	}
    	for(Complex root : roots) {
    		if(root.im() > 1e-5) {
    			Polynomial p = X*X - 2*root.re()*X + root.absSquared() ;
    			factors.add(p) ;
    		}
    	}
    	return factors ;
    }

    public static ArrayList<Complex> getCommonRoots(Polynomial p, Polynomial q) {
    	ArrayList<Polynomial> commonFactors = getCommonFactors(p, q) ;
    	ArrayList<Complex> commonRoots = new ArrayList<>() ;
    	for(Polynomial pp : commonFactors)
    		commonRoots.addAll(pp.getRootsAsList()) ;

    	return commonRoots ;
    }

    public static ArrayList<Polynomial> getCommonFactors(Polynomial p, Polynomial q) {
    	ArrayList<Polynomial> factorsOfP = p.getFactors() ;
    	ArrayList<Polynomial> factorsOfQ = q.getFactors() ;
    	ArrayList<Polynomial> factors = new ArrayList<>() ;

    	ArrayList<String> factorsP = new ArrayList<>() ;
    	for(Polynomial pp : factorsOfP)
    		factorsP.add(pp.toString()) ;
    	ArrayList<String> factorsQ = new ArrayList<>() ;
    	for(Polynomial qq : factorsOfQ)
    		factorsQ.add(qq.toString()) ;

    	ArrayList<String> commons = (ArrayList<String>) ArrayUtils.getCommonElements(factorsP, factorsQ) ;

    	for(String s : commons) {
    		for(Polynomial pp : factorsOfP){
    			if(s.equals(pp.toString())){
    				factors.add(pp) ;
    				break ;
    			}
    		}
    	}
    	return factors ;
    }

    public ArrayList<Polynomial> getCommonFactors(Polynomial p) {
    	return getCommonFactors(this, p) ;
    }

	// ************ operator overloading **********************

	/**
	 * Operator overloading support:
	 *
	 * Object a = 5;
	 *
	 */
	public static Polynomial valueOf(int v) {
		return new Polynomial(v, 0);
	}

	public static Polynomial valueOf(long v) {
		return new Polynomial(v, 0);
	}

	public static Polynomial valueOf(float v) {
		return new Polynomial(v, 0);
	}

	public static Polynomial valueOf(double v) {
		return new Polynomial(v, 0);
	}

	public static Polynomial valueOf(Polynomial v) {
		return new Polynomial(v.coef);
	}

	/**
	 * Operator overload support: a+b
	 */
	public Polynomial add(Polynomial v) {
		return this.plus(v) ;
	}

	public Polynomial addRev(Polynomial v) {
		return this.plus(v) ;
	}

	public Polynomial add(int v) {
		return this.plus(v) ;
	}

	public Polynomial addRev(int v) {
		return this.plus(v) ;
	}

	public Polynomial add(long v) {
		return this.plus(v) ;
	}

	public Polynomial addRev(long v) {
		return this.plus(v) ;
	}

	public Polynomial add(float v) {
		return this.plus(v) ;
	}

	public Polynomial addRev(float v) {
		return this.plus(v) ;
	}

	public Polynomial add(double v) {
		return this.plus(v) ;
	}

	public Polynomial addRev(double v) {
		return this.plus(v) ;
	}

	/**
	 * Operator overload support: a-b
	 */
	public Polynomial subtract(Polynomial v) {
		return this.minus(v) ;
	}

	public Polynomial subtractRev(Polynomial v) {
		return this.times(-1).plus(v) ;
	}

	public Polynomial subtract(int v) {
		return this.minus(v) ;
	}

	public Polynomial subtractRev(int v) {
		return this.times(-1).plus(v) ;
	}

	public Polynomial subtract(long v) {
		return this.minus(v) ;
	}

	public Polynomial subtractRev(long v) {
		return this.times(-1).plus(v) ;
	}

	public Polynomial subtract(float v) {
		return this.minus(v) ;
	}

	public Polynomial subtractRev(float v) {
		return this.times(-1).plus(v) ;
	}

	public Polynomial subtract(double v) {
		return this.minus(v) ;
	}

	public Polynomial subtractRev(double v) {
		return this.times(-1).plus(v) ;
	}

	/**
	 * Operator overload support: a*b
	 */
	public Polynomial multiply(Polynomial v) {
		return this.times(v);
	}

	public Polynomial multiplyRev(Polynomial v) {
		return v.times(this);
	}

	public Polynomial multiply(int v) {
		return this.times(v);
	}

	public Polynomial multiplyRev(int v) {
		return this.times(v);
	}

	public Polynomial multiply(long v) {
		return this.times(v);
	}

	public Polynomial multiplyRev(long v) {
		return this.times(v);
	}

	public Polynomial multiply(float v) {
		return this.times(v);
	}

	public Polynomial multiplyRev(float v) {
		return this.times(v);
	}

	public Polynomial multiply(double v) {
		return this.times(v);
	}

	public Polynomial multiplyRev(double v) {
		return this.times(v);
	}

	/**
	 * Operator overload support: a/b
	 */
	public Rational divide(Polynomial v) {
		return new Rational(this, v);
	}

	public Rational divideRev(Polynomial v) {
		return v.divides(this);
	}

	public Polynomial divide(int v) {
		return this.divides(v) ;
	}

	public Rational divideRev(int v) {
		return Rational.valueOf(v)/this ;
	}

	public Polynomial divide(long v) {
		return this.divides(v) ;
	}

	public Rational divideRev(long v) {
		return Rational.valueOf(v)/this ;
	}

	public Polynomial divide(float v) {
		return this.divides(v) ;
	}

	public Rational divideRev(float v) {
		return Rational.valueOf(v)/this ;
	}

	public Polynomial divide(double v) {
		return this.divides(v) ;
	}

	public Rational divideRev(double v) {
		return Rational.valueOf(v)/this ;
	}

	/**
	 * Operator overload support: -a
	 */
	public Polynomial negate() {
		return this.times(-1) ;
	}


    // for test
    public static void main(String[] args) {
		Polynomial p =  X.pow(3) + X.pow(2) - X - 1 ;
		System.out.println(p);
		System.out.println(p.getFactors());

		Polynomial q = X.pow(5) - X.pow(4) - X + 1 ;
		System.out.println(q);
		System.out.println(q.getFactors());

		System.out.println("\n" + getCommonFactors(p, q));
		System.out.println(getCommonRoots(p, q));

		System.out.println((p/(X-1)).simplify());

		Polynomial p1 = (X-1).pow(5) *(X+2) ;
		System.out.println(p1);
		Polynomial q1 = (X-1).pow(3) ;
		System.out.println(q1);
		System.out.println(p1.getFactors());
		System.out.println(q1.getFactors());

	}

}


package mathLib.polynom;

import static mathLib.polynom.Polynomial.X;
import static mathLib.polynom.Polynomial.getCommanFactors;

import java.util.ArrayList;

public class Rational {

	Polynomial p, q;

	// Rational R(x) = p(x)/q(x)

	public Rational(Polynomial numerator, Polynomial denominator) {
		this.p = numerator;
		this.q = denominator;
	}

	// Rational R(x) = p(x)/1 

	public Rational(Polynomial numerator) {
		this.q = 0 * X + 1;
		this.p = numerator;
	}
	
	public double evaluate(double x) {
		return p.evaluate(x)/q.evaluate(x) ;
	}
	
	public Rational LHopital() {
		return new Rational(p.diff(), q.diff()) ;
	}
	
	public Rational plus(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q + a.q * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational plus(double a) {
		return new Rational(p+q*a, q) ;
	}
	
	public Rational minus(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q - a.q * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational minus(double a) {
		return new Rational(p-q*a, q) ;
	}
	
	public Rational times(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational times(double a) {
		return new Rational(p*a, q) ;
	}
	
	public Rational divides(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q ;
		Polynomial denom = a.q * r.p ;
		return new Rational(num, denom) ;
	}
	
	public Rational divides(double a) {
		return new Rational(p, q*a) ;
	}
	
	public Rational diff() {
		Polynomial num = p.diff() * q - p * q.diff() ;
		Polynomial denom = q * q ;
		return new Rational(num, denom) ;
	}
	
	public Rational diff(int n) {
		Rational a = this ;
		for(int i=0; i<n; i++)
			a = a.diff() ;
		return a ;
	}
	
//	public void simplify() {
//		// leading coefficients of p(x) and q(x)
//		double pcoef = p.coef[p.degree()] ;
//		double qcoef = q.coef[q.degree()] ;
//		// find common factors of of p(x) and q(x)
//		ArrayList<Polynomial> factorsOfP = p.getFactors() ;
//		ArrayList<Polynomial> factorsOfQ = q.getFactors() ;
//		ArrayList<Polynomial> commonFactors = getCommanFactors(p, q) ;
//		// remove the common factors
//		for(Polynomial common : commonFactors) {
//			factorsOfP.remove(common) ;
//			factorsOfQ.remove(common) ;
//		}
//		// reconstruct p(x) and q(x)
//		Polynomial pSimp = 0*X+1 ;
//		for(Polynomial w : factorsOfP)
//			pSimp = pSimp * w ;
//		Polynomial qSimp = 0*X+1 ;
//		for(Polynomial z : factorsOfQ)
//			qSimp = qSimp * z ;
//		
//		this.p = pSimp * pcoef ;
//		this.q = qSimp * qcoef ;
//	}
	
	public Rational simplify() {
		// leading coefficients of p(x) and q(x)
		double pcoef = p.coef[p.degree()] ;
		double qcoef = q.coef[q.degree()] ;
		// find common factors of of p(x) and q(x)
		ArrayList<Polynomial> factorsOfP = p.getFactors() ;
		ArrayList<Polynomial> factorsOfQ = q.getFactors() ;
		ArrayList<Polynomial> commonFactors = getCommanFactors(p, q) ;
		// remove the common factors
		for(Polynomial common : commonFactors) {
			factorsOfP.remove(common) ;
			factorsOfQ.remove(common) ;
		}
		// reconstruct p(x) and q(x)
		Polynomial pSimp = 0*X+1 ;
		for(Polynomial w : factorsOfP)
			pSimp = pSimp * w ;
		Polynomial qSimp = 0*X+1 ;
		for(Polynomial z : factorsOfQ)
			qSimp = qSimp * z ;
		
		return new Rational(pSimp * pcoef, qSimp * qcoef) ;
	}
	
	public static Rational toRational(Polynomial p) {
		return new Rational(p, 0*X+1) ;
	}
	
	
	@Override
	public String toString() {
		StringBuilder st = new StringBuilder() ;
		st.append(p) ;
		st.append("\n") ;
		int m = Math.max(p.toString().length(), q.toString().length()) ;
		for(int i=0; i<m; i++)
			st.append("-") ;
		st.append("\n") ;
		st.append(q) ;
		st.append("\n") ;
		return st ;
	}
	
	// ************ operator overloading **********************

	/**
	 * Operator overloading support:
	 *
	 * Object a = 5;
	 *
	 */
	public static Rational valueOf(int v) {
		return new Rational(0*X+v);
	}

	public static Rational valueOf(long v) {
		return new Rational(0*X+v);
	}

	public static Rational valueOf(float v) {
		return new Rational(0*X+v);
	}

	public static Rational valueOf(double v) {
		return new Rational(0*X+v);
	}

	public static Rational valueOf(Polynomial v) {
		return new Rational(v, 0*X+1) ;
	}
	
	/**
	 * Operator overload support: a+b
	 */
	public Rational add(Polynomial v) {
		return this.plus(new Rational(v)) ;
	}

	public Rational addRev(Polynomial v) {
		return this.plus(new Rational(v)) ;
	}
	
	public Rational add(Rational v) {
		return this.plus(v) ;
	}

	public Rational addRev(Rational v) {
		return this.plus(v) ;
	}

	public Rational add(int v) {
		return this.plus(v) ;
	}

	public Rational addRev(int v) {
		return this.plus(v) ;
	}

	public Rational add(long v) {
		return this.plus(v) ;
	}

	public Rational addRev(long v) {
		return this.plus(v) ;
	}

	public Rational add(float v) {
		return this.plus(v) ;
	}

	public Rational addRev(float v) {
		return this.plus(v) ;
	}

	public Rational add(double v) {
		return this.plus(v) ;
	}

	public Rational addRev(double v) {
		return this.plus(v) ;
	}
	
	/**
	 * Operator overload support: a-b
	 */
	public Rational subtract(Polynomial v) {
		return this.minus(new Rational(v)) ;
	}

	public Rational subtractRev(Polynomial v) {
		return this.times(-1).plus(new Rational(v)) ;
	}
	
	public Rational subtract(Rational v) {
		return this.minus(v) ;
	}

	public Rational subtractRev(Rational v) {
		return this.times(-1).plus(v) ;
	}

	public Rational subtract(int v) {
		return this.minus(v) ;
	}

	public Rational subtractRev(int v) {
		return this.times(-1).plus(v) ;
	}

	public Rational subtract(long v) {
		return this.minus(v) ;
	}

	public Rational subtractRev(long v) {
		return this.times(-1).plus(v) ;
	}

	public Rational subtract(float v) {
		return this.minus(v) ;
	}

	public Rational subtractRev(float v) {
		return this.times(-1).plus(v) ;
	}

	public Rational subtract(double v) {
		return this.minus(v) ;
	}

	public Rational subtractRev(double v) {
		return this.times(-1).plus(v) ;
	}
	
	/**
	 * Operator overload support: a*b
	 */
	public Rational multiply(Polynomial v) {
		return this.times(new Rational(v));
	}

	public Rational multiplyRev(Polynomial v) {
		return (new Rational(v)).times(this);
	}
	
	public Rational multiply(Rational v) {
		return this.times(v);
	}

	public Rational multiplyRev(Rational v) {
		return v.times(this);
	}

	public Rational multiply(int v) {
		return this.times(v);
	}

	public Rational multiplyRev(int v) {
		return this.times(v);
	}

	public Rational multiply(long v) {
		return this.times(v);
	}

	public Rational multiplyRev(long v) {
		return this.times(v);
	}

	public Rational multiply(float v) {
		return this.times(v);
	}

	public Rational multiplyRev(float v) {
		return this.times(v);
	}

	public Rational multiply(double v) {
		return this.times(v);
	}

	public Rational multiplyRev(double v) {
		return this.times(v);
	}
	
	/**
	 * Operator overload support: a/b
	 */
	public Rational divide(Polynomial v) {
		return this.divides(new Rational(v));
	}

	public Rational divideRev(Polynomial v) {
		return (new Rational(v)).divides(this);
	}
	
	public Rational divide(Rational v) {
		return this.divides(v);
	}

	public Rational divideRev(Rational v) {
		return v.divides(this);
	}

	public Rational divide(int v) {
		return this.divides(v) ;
	}

	public Rational divideRev(int v) {
		return null;
	}

	public Rational divide(long v) {
		return this.divides(v) ;
	}

	public Rational divideRev(long v) {
		return null;
	}

	public Rational divide(float v) {
		return this.divides(v) ;
	}

	public Rational divideRev(float v) {
		return null;
	}

	public Rational divide(double v) {
		return this.divides(v) ;
	}

	public Rational divideRev(double v) {
		return null;
	}
	
	/**
	 * Operator overload support: -a
	 */
	public Rational negate() {
		return this.times(-1) ;
	}

	// for test
	public static void main(String[] args) {
		Rational r = new Rational(X*X-1, X.pow(3)-1) ;
		Rational s = new Rational(X-1, X+1) ;
		System.out.println(r);
		System.out.println(s);
//		System.out.println(r.LHopital());
//		Rational q = r.divides(s) ;
//		System.out.println(q);
//		System.out.println(q.getSimplified());
//		System.out.println(q.p.getFactors());
//		System.out.println(q.q.getFactors());
//		System.out.println(getCommanFactors(q.p, q.q));

		System.out.println(r*s);
		System.out.println(r/s);
		
		System.out.println(r.simplify());
	}
	

}

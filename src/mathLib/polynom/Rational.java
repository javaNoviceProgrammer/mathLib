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

	// Rational R(x) = 1/q(x)

	public Rational(Polynomial denomenator) {
		this.p = 0 * X + 1;
		this.q = denomenator;
	}
	
	
	public double evaluate(double x) {
		return p.evaluate(x)/q.evaluate(x) ;
	}
	
	public Rational plus(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q + a.q * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational minus(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q - a.q * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational times(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.p ;
		Polynomial denom = a.q * r.q ;
		return new Rational(num, denom) ;
	}
	
	public Rational divides(Rational r) {
		Rational a = this ;
		Polynomial num = a.p * r.q ;
		Polynomial denom = a.q * r.p ;
		return new Rational(num, denom) ;
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
	
	public void simplify() {
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
		
		this.p = pSimp * pcoef ;
		this.q = qSimp * qcoef ;
	}
	
	public Rational getSimplified() {
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

	// for test
	public static void main(String[] args) {
		Rational r = new Rational(2*X*X-2, X.pow(5)-1) ;
		System.out.println(r);
		System.out.println(r.getSimplified());
	}
	

}

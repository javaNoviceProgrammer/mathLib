package mathLib.polynom.special;

import static mathLib.polynom.ComplexPolynomial.Xc;
import static mathLib.polynom.Polynomial.X;
import static mathLib.util.MathUtils.combination;

import mathLib.polynom.ComplexPolynomial;
import mathLib.polynom.Polynomial;

public class MorganVoycePoly {

	public static Polynomial Bn(int n) {
		if(n==0)
			return 0*X + 1 ;
		if(n==1)
			return X + 2 ;
		if(n > 1) {
			Polynomial p = 0*X ;
			for(int k=0; k <= n ; k++)
				p = p + combination(n+k+1, n-k) * X.pow(k) ;
			return p ;
		}	
		else 
			throw new IllegalArgumentException("order must be 0 or higher") ;
	}
	
	public static Polynomial bn(int n) {
		if(n==0)
			return 0*X + 1 ;
		if(n==1)
			return X + 1 ;
		if(n > 1) {
			Polynomial p = 0*X ;
			for(int k=0; k <= n ; k++)
				p = p + combination(n+k, n-k) * X.pow(k) ;
			return p ;
		}	
		else 
			throw new IllegalArgumentException("order must be 0 or higher") ;
	}
	
	public static ComplexPolynomial BnC(int n) {
		return Bn(n)+0*Xc ;
	}
	
	public static ComplexPolynomial bnC(int n) {
		return bn(n)+0*Xc ;
	}

	
	// for test
	public static void main(String[] args) {
		System.out.println(Bn(0));
		System.out.println(Bn(1));
		System.out.println(Bn(2));
		System.out.println(Bn(3));
		System.out.println(Bn(4));
		System.out.println(Bn(5));
		
		System.out.println();
		
		System.out.println(bn(0));
		System.out.println(bn(1));
		System.out.println(bn(2));
		System.out.println(bn(3));
		System.out.println(bn(4));
		System.out.println(bn(5));
		
		System.out.println("\n" + (bn(5)*bn(3)-bn(4)*bn(4)));
		System.out.println("\n" + (Bn(8)*Bn(6)-Bn(7)*Bn(7)));
	}
	
	
}

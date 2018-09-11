package tests;

import mathLib.polynom.Polynomial;
import mathLib.polynom.Rational;

import static mathLib.polynom.Polynomial.*;

public class TestRational {
	
	public static void main(String[] args) {
		Polynomial p1 = X*X*X-2*X+2 ;
		p1.plot(-10, 10);
		Rational q1 = p1 + 1/X ;
		System.out.println(p1);
		System.out.println("\n" + q1);
	}

}

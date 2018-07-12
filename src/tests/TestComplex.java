package tests;

import static utils.Complex.*;
import static utils.ComplexMath.*;

import utils.Complex;

public class TestComplex {
	public static void main(String[] args) {
		Complex u = 2+j ;
		Complex v = sin(u)*sin(u) + cos(u)*cos(u) ;
		System.out.println(v);
		System.out.println(sin(PI/2));
	}
}

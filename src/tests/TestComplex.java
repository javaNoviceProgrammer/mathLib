package tests;

import static mathLib.utils.Complex.*;
import static mathLib.utils.ComplexMath.*;

import mathLib.utils.Complex;

public class TestComplex {
	public static void main(String[] args) {
		Complex u = 2+j ;
		Complex v = sin(u)*sin(u) + cos(u)*cos(u) ;
		System.out.println(v);
		System.out.println(sin(PI/2));
	}
}

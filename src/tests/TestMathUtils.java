package tests;

import mathLib.util.MathUtils;

public class TestMathUtils {
	public static void main(String[] args) {
		System.out.println(MathUtils.areEqual(1e-5, 1e-5+1e-12, 1e-13));
	}
}

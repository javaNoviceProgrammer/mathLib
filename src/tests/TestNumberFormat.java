package tests;

import mathLib.util.StringUtils;

public class TestNumberFormat {
	public static void main(String[] args) {
		double a = 10.0/3.0 ;
		System.out.println(StringUtils.fixedWidthDoubletoString(a, 4, 4));
	}
}

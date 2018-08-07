package tests;

import mathLib.util.Conversions;
import mathLib.util.Units;

public class TestConversions {
	// for test
	public static void main(String[] args) {
		double u = Conversions.opticalPower(5.0, Units.dBm, Units.mW) ;
		System.out.println(u);
	}
}

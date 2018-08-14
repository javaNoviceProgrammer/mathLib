package tests;

import mathLib.util.Conversions;
import mathLib.util.Units;

public class TestConversions {

	public static void main(String[] args) {
		// convert optical power
		double u = Conversions.opticalPower(5.0, Units.dBm, Units.mW) ;
		System.out.println(u);
		// convert time
		double v = Conversions.time(3600, Units.minute, Units.day) ;
		System.out.println(v);
		// convert wg loss
		double w = Conversions.wgLoss(1.0, Units.perMeter, Units.dBperMeter) ;
		System.out.println(w);
	}
}

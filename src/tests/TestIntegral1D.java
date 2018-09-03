package tests;

import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.util.MathUtils;

public class TestIntegral1D {
	public static void main(String[] args) {

		IntegralFunction1D func = new IntegralFunction1D() {
			@Override
			public double function(double x) {
				Integral1D integral1d = new Integral1D(t -> MathUtils.Functions.sinc(t), 0, x) ;
				return integral1d.getIntegral();
			}
		};

		System.out.println(func.function(1));
	}
}

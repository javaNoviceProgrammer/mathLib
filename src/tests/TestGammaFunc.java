package tests;

import mathLib.func.GammaFunc;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class TestGammaFunc {
	public static void main(String[] args) {

		Timer timer1 = new Timer() ;
		timer1.start();
		System.out.println("using gamma function: "+ GammaFunc.gamma(50.1));
		timer1.stop();
		timer1.show();

		Timer timer2 = new Timer() ;
		timer2.start();
		System.out.println("using recursive factorial: " + MathUtils.factorial(50.1));
		timer2.stop();
		timer2.show();
	}
}

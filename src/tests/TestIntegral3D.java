package tests;

import mathLib.integral.Integral3D;
import mathLib.integral.intf.IntegralDomain3D;
import mathLib.integral.intf.IntegralFunction3D;
import mathLib.util.Timer;

public class TestIntegral3D {
	public static void main(String[] args) {
		IntegralFunction3D func = new IntegralFunction3D() {
			
			@Override
			public double function(double var1, double var2, double var3) {
				return var1*var1*Math.sin(var2) ;
			}
		};
		
		IntegralDomain3D domain = new IntegralDomain3D() {
			
			@Override
			public double getVar3Min(double var1, double var2) {
				return 0;
			}
			
			@Override
			public double getVar3Max(double var1, double var2) {
				return 2*Math.PI;
			}
			
			@Override
			public double getVar2Min(double var1) {
				return 0;
			}
			
			@Override
			public double getVar2Max(double var1) {
				return Math.PI;
			}
			
			@Override
			public double getVar1Min() {
				return 0;
			}
			
			@Override
			public double getVar1Max() {
				return 5.0;
			}
		};
		
		Timer timer = new Timer() ;
		timer.start(); 
		
		Integral3D integral = new Integral3D(func, domain) ;
		
		timer.stop();
		System.out.println(integral.getIntegral());
		System.out.println(timer);
		
		
	}
}

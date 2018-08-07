package mathLib.integral;

import mathLib.integral.intf.IntegralDomain2D;
import mathLib.integral.intf.IntegralFunction;
import mathLib.integral.intf.IntegralFunction2D;

public class Integral2D {
	
	IntegralFunction2D func2d ;
	IntegralDomain2D domain ;
	
	public Integral2D(IntegralFunction2D func, IntegralDomain2D domain) {
		this.func2d = func ;
		this.domain = domain ;
	}
	
	public void setFunction(IntegralFunction2D func) {
		this.func2d = func ;
	}
	
	public void setDomain(IntegralDomain2D domain) {
		this.domain = domain ;
	}
	
	public double getIntegral() {
		IntegralFunction func1d = new IntegralFunction() {
			@Override
			public double function(double var1) {
				// step1: define an integral function
				IntegralFunction ff = new IntegralFunction() {
					@Override
					public double function(double var2) {
						return func2d.function(var1, var2);
					}
				};
				// step 2: define the adaptive integral
				AdaptiveIntegral integral = new AdaptiveIntegral(ff, domain.getVar2Min(var1), domain.getVar2Max(var1)) ;
				return integral.getIntegral();
			}
		};
		
		AdaptiveIntegral integral = new AdaptiveIntegral(func1d, domain.getVar1Min(), domain.getVar1Max()) ;
		return integral.getIntegral() ;
	}
	
}

package mathLib.integral;

import mathLib.integral.intf.IntegralDomain2D;
import mathLib.integral.intf.IntegralFunction1D;
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
		IntegralFunction1D func1d = new IntegralFunction1D() {
			@Override
			public double function(double var1) {

				IntegralFunction1D func = new IntegralFunction1D() {
					@Override
					public double function(double var2) {
						return func2d.function(var1, var2);
					}
				};

				Integral1D integral = new Integral1D(func, domain.getVar2Min(var1), domain.getVar2Max(var1)) ;
				return integral.getIntegral();
			}
		};
		
		Integral1D integral = new Integral1D(func1d, domain.getVar1Min(), domain.getVar1Max()) ;
		return integral.getIntegral() ;
	}
	
}

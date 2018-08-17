package mathLib.integral;

import mathLib.integral.intf.IntegralDomain3D;
import mathLib.integral.intf.IntegralFunction;
import mathLib.integral.intf.IntegralFunction3D;

public class Integral3D {

	IntegralFunction3D func3d;
	IntegralDomain3D domain;

	public Integral3D(IntegralFunction3D func, IntegralDomain3D domain) {
		this.func3d = func;
		this.domain = domain;
	}

	public void setFunction(IntegralFunction3D func) {
		this.func3d = func;
	}

	public void setDomain(IntegralDomain3D domain) {
		this.domain = domain;
	}

	public double getIntegral() {
		IntegralFunction func1d = new IntegralFunction() {
			@Override
			public double function(double var1) {

				IntegralFunction func2d = new IntegralFunction() {
					@Override
					public double function(double var2) {
						IntegralFunction func = new IntegralFunction() {

							@Override
							public double function(double var3) {
								return func3d.function(var1, var2, var3);
							}
						};
						Integral1D integral3 = new Integral1D(func, domain.getVar3Min(var1, var2),
								domain.getVar3Max(var1, var2));
						return integral3.getIntegral();
					}
				};
				// step 2: define the adaptive integral
				Integral1D integral2 = new Integral1D(func2d, domain.getVar2Min(var1),
						domain.getVar2Max(var1));
				return integral2.getIntegral();
			}
		};

		Integral1D integral = new Integral1D(func1d, domain.getVar1Min(), domain.getVar1Max());
		return integral.getIntegral();
	}

}

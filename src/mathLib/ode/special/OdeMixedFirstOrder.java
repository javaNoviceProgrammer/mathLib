package mathLib.ode.special;

import static mathLib.func.symbolic.FMath.C;
import static mathLib.func.symbolic.FMath.sin;
import static mathLib.func.symbolic.FMath.x;
import static mathLib.func.symbolic.FMath.y;

import flanagan.integration.DerivFunction;
import flanagan.integration.RungeKutta;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

/**
 * Solves the differential equation of form a(x,y) y' + b(x,y) = f(x,y)
 *
 * a(x,y) cannot be null, but b(x,y) and f(x,y) can be null.
 *
 */

public class OdeMixedFirstOrder extends RungeKutta implements DerivFunction {

	MathFunc a, b, f ;

	public OdeMixedFirstOrder(MathFunc a, MathFunc b, MathFunc f) {
		if(a == null)
			throw new IllegalArgumentException("First argument cannot be null") ;
		if(b == null)
			b = C(0) ;
		if(f == null)
			f = C(0) ;

		this.a = a ;
		this.b = b ;
		this.f = f ;
	}

	@Override
	public double deriv(double x, double y) {
		// y' = g0(x,y)
		double g0 = f.apply(x,y) - b.apply(x,y) ;
		g0 = g0/a.apply(x,y) ;
		return g0;
	}

	// for test
	public static void main(String[] args) {
		// solving: y'(x) = sin(x*x)-y with y(0) = 0
		OdeMixedFirstOrder ode = new OdeMixedFirstOrder(C(1), null, sin(x*x)-y*y) ;
		ode.setInitialValueOfX(0);
		ode.setInitialValueOfY(0);
		ode.setStepSize(1e-1);
		double[] xVal = MathUtils.linspace(0, 20, 2000) ;
		double[] yVal = new double[xVal.length] ;
		for(int i=0; i<xVal.length; i++) {
			ode.setFinalValueOfX(xVal[i]);
			yVal[i] = ode.fourthOrder(ode) ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xVal, yVal, "b");
		fig.renderPlot();
//		fig.markerON();
		fig.xlabel("X");
		fig.ylabel("Solution Y");
		fig.run(true);
	}

}

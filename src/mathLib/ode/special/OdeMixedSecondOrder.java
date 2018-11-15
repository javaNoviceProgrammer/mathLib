package mathLib.ode.special;

import static mathLib.func.symbolic.FMath.*;

import flanagan.integration.RungeKutta;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.ode.intf.DerivnFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

/**
 * solving ordinary differential equation of the form  a(x,y) y'' + b(x,y) y' + c(x,y) y = f(x,y)
 *
 *	a(x,y) cannot be null, but b(x,y), c(x,y) and f(x,y) can be null.
 *
 *	solution gives y(x) and y'(x)
 */

public class OdeMixedSecondOrder extends RungeKutta implements DerivnFunction1D {

	MathFunc a, b, c, f ;

	public OdeMixedSecondOrder(MathFunc a, MathFunc b, MathFunc c, MathFunc f) {
		if(a == null)
			throw new IllegalArgumentException("First argument cannot be null") ;
		if(b == null)
			b = C(0) ;
		if(c == null)
			c = C(0) ;
		if(f == null)
			f = C(0) ;

		this.a = a ;
		this.b = b ;
		this.c = c ;
		this.f = f ;
	}

	@Override
	public double[] derivn(double x, double[] yy) {
		// z = y' => y'' = z'
		// y' = g0(x,y,z)
		// z' = g1(x,y,z)
		double y = yy[0] ;
		double z = yy[1] ;
		double g0 = z ;
		double g1 = f.apply(x,y) - c.apply(x,y)*y - b.apply(x,y) * z ;
		g1 = g1/a.apply(x,y) ;
		return new double[] {g0, g1} ;
	}

	// for test
	public static void main(String[] args) {
		Timer timer = new Timer() ;
		timer.start();
		// solving y'' = sin(x*x)-y  on [0, 10] with y(0) = 0, y'(0) = 0
		OdeMixedSecondOrder ode = new OdeMixedSecondOrder(C(1), null, null, sin(x*x)-y) ;
		ode.setInitialValueOfX(0);
		ode.setInitialValueOfY(new double[] {0,0});
		ode.setStepSize(1e-1);
		double[] xVal = MathUtils.linspace(0, 10, 1000) ;
		double[] yVal = new double[xVal.length] ;
		double[] dyVal = new double[xVal.length] ;
		for(int i=0; i<xVal.length; i++) {
			ode.setFinalValueOfX(xVal[i]);
			yVal[i] = ode.fourthOrder(ode)[0] ;
			dyVal[i] = ode.fourthOrder(ode)[1] ;
		}

		timer.stop();
		System.out.println(timer);

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xVal, yVal, "b");
		fig.renderPlot();
//		fig.markerON();
		fig.xlabel("X");
		fig.ylabel("Solution Y");
		fig.run(true);
	}


}

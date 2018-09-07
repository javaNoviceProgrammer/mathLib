package mathLib.ode.special;

import edu.uta.futureye.function.intf.MathFunc;
import flanagan.integration.RungeKutta;
import mathLib.ode.intf.DerivFunction1D;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

import static edu.uta.futureye.function.FMath.* ;

/**
 * solving ordinary differential equation of the form  a(x) y' + b(x) y = f(x)
 * 
 *	a(x) cannot be null, but b(x) and f(x) can be null.
 */

public class OdeFirstOrder extends RungeKutta implements DerivFunction1D {
	
	MathFunc a, b, f ;
	
	public OdeFirstOrder(MathFunc a, MathFunc b, MathFunc f) {
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
		// y' = g(x,y)
		double g = (f.apply(x)-b.apply(x)*y)/a.apply(x) ;
		return g;
	}
	
	// for test
	public static void main(String[] args) {
		// solving y' = 2x on [0,2] with y(0) = 0
		OdeFirstOrder ode = new OdeFirstOrder(C(1), null, 2*x) ;
		ode.setInitialValueOfX(0);
		ode.setInitialValueOfY(0);
		ode.setStepSize(1e-4);
		double[] xVal = MathUtils.linspace(0, 2, 100) ;
		double[] yVal = new double[xVal.length] ;

		for(int i=0; i<xVal.length; i++) {
			ode.setFinalValueOfX(xVal[i]);
			yVal[i] = ode.fourthOrder(ode) ;
		}
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(xVal, yVal);
		fig.RenderPlot();
		fig.markerON();
		fig.xlabel("X");
		fig.ylabel("Solution Y");
		fig.run(true);
	}

}

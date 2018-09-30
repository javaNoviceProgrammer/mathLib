package mathLib.ode.special;

import static edu.uta.futureye.function.FMath.C;
import static edu.uta.futureye.function.FMath.sin;
import static edu.uta.futureye.function.FMath.x;

import edu.uta.futureye.function.intf.MathFunc;
import flanagan.integration.RungeKutta;
import mathLib.ode.intf.DerivnFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

/**
 * solving ordinary differential equation of the form  a(x) y'''' + b(x) y''' + c(x) y'' + d(x) y' + e(x) y = f(x)
 * 
 *	a(x) cannot be null, but b(x), c(x), d(x), e(x), and f(x) can be null.
 *
 *	solution gives y(x), y'(x), y''(x), and y'''(x)
 */

public class OdeFourthOrder extends RungeKutta implements DerivnFunction1D {

	MathFunc a, b, c, d, e, f ;
	
	public OdeFourthOrder(MathFunc a, MathFunc b, MathFunc c, MathFunc d, MathFunc e, MathFunc f) {
		if(a == null)
			throw new IllegalArgumentException("First argument cannot be null") ;
		if(b == null)
			b = C(0) ;
		if(c == null)
			c = C(0) ;
		if(d == null)
			d = C(0) ;
		if(e == null)
			e = C(0) ;
		if(f == null)
			f = C(0) ;
		
		this.a = a ;
		this.b = b ;
		this.c = c ;
		this.d = d ;
		this.e = e ;
		this.f = f ;
	}
	
	@Override
	public double[] derivn(double x, double[] yy) {
		// z = y', w = y'', s = y''' ==> z' = y'' = w , y''' = w' = s, s' = y''''
		// y' = g0(x,y,z)
		// z' = g1(x,y,z)
		// w' = g2(x,y,z) 
		// s' = g3(x,y,z)
		double y = yy[0] ;
		double z = yy[1] ;
		double w = yy[2] ;
		double s = yy[3] ;
		double g0 = z ;
		double g1 = w ;
		double g2 = s ;
		double g3 = f.apply(x) - e.apply(x)*y - d.apply(x)*z - c.apply(x)*w - b.apply(x)*s  ;
		g3 = g3/a.apply(x) ;
		return new double[] {g0, g1, g2, g3} ;
	}
	
	// for test
	public static void main(String[] args) {
		// solving y'''' = 2x sin(x) on [0,20] with y(0) = 0, y'(0) = 0, y''(0) = 0, y'''(0) = 0
		OdeFourthOrder ode = new OdeFourthOrder(C(1), null, null, null, null, 2*x*sin(x)) ;
		ode.setInitialValueOfX(0);
		ode.setInitialValueOfY(new double[] {0, 0, 0, 0});
		ode.setStepSize(1e-1);
		double[] xVal = MathUtils.linspace(0, 20, 100) ;
		double[] yVal = new double[xVal.length] ;
		for(int i=0; i<xVal.length; i++) {
			ode.setFinalValueOfX(xVal[i]);
			yVal[i] = ode.fourthOrder(ode)[0] ;
		}
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(xVal, yVal, "b");
		fig.RenderPlot();
		fig.markerON();
		fig.xlabel("X");
		fig.ylabel("Solution Y");
		fig.run(true);
	}

}

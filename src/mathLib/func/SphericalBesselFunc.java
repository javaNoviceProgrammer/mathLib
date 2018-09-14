package mathLib.func;

import static java.lang.Math.* ;

import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class SphericalBesselFunc {

	public static double in(int n, double x) {
		if(n<0)
			return in(-n, x)*MathUtils.minusOnePower(n) ;
		if(n==0)
			return sinh(x)/x ;
		if(n==1)
			return (x*cosh(x)-sinh(x))/(x*x) ;

		return gn(n, x)*sinh(x) + gn(-(n+1), x)*cosh(x) ;
	}

	private static double gn(int n, double x) {
		if(n==0)
			return 1.0/x ;
		if(n==1)
			return -1.0/(x*x) ;
		if(n <= -1)
			return gn(n+2, x) + (2*n+3)/x * gn(n+1, x) ;

		return gn(n-2, x)-(2*n-1)/x * gn(n-1, x) ;
	}



	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0.0, 6.0, 1000) ;
		double[] y0 = ArrayFunc.apply(t -> in(0, t), x) ;
		double[] y1 = ArrayFunc.apply(t -> in(1, t), x) ;
		double[] y2 = ArrayFunc.apply(t -> in(2, t), x) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y0, "b");
		fig.plot(x, y1, "r");
		fig.plot(x, y2, "m");
		fig.RenderPlot();
		fig.run(true);

	}

}

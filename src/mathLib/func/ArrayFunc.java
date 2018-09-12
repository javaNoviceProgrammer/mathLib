package mathLib.func;

import mathLib.func.intf.RealFunction;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class ArrayFunc {

	public static double[] apply(RealFunction func, double[] var0) {
		int n = var0.length ;
		double[] var1 = new double[n] ;
		for(int i=0; i<n; i++)
			var1[i] = func.evaluate(var0[i]) ;
		return var1 ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0, 10, 1000) ;
		double[] y = apply(t -> BesselFunc.j0(t), x) ;
		double[] z = apply(t -> BesselFunc.j1(t), x) ;
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");
		fig.plot(x, z, "r");
		fig.RenderPlot();
		fig.setFigLineWidth(0, 2);
		fig.run(true);
	}

}

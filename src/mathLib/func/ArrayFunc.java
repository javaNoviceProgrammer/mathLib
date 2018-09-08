package mathLib.func;

import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class ArrayFunc {

	public static double[] apply(Function func, double[] var0) {
		int n = var0.length ;
		double[] var1 = new double[n] ;
		for(int i=0; i<n; i++)
			var1[i] = func.function(var0[i]) ;
		return var1 ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(-10, 10, 1000) ;
		double[] y = apply(t -> Math.sin(t), x) ;
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y);
		fig.RenderPlot();
		fig.setFigLineWidth(0, 2);
		fig.run(true);
	}

}

package mathLib.func;

import mathLib.func.intf.RealFunction;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class PeriodicFunction {
	
	RealFunction funcOnePeriod ;
	double periodStart, periodEnd, period ;
	
	public PeriodicFunction(RealFunction funcOnePeriod, double periodStart, double periodEnd) {
		this.funcOnePeriod = funcOnePeriod ;
		this.periodStart = periodStart ;
		this.periodEnd = periodEnd ;
		this.period = periodEnd - periodStart ;
	}
	
	public double evaluate(double var) {
		double a = (var-periodStart)/period ;
		int k = (int) Math.floor(a) ;
		if(Math.floor(a) == a)
			k = (int) Math.floor(a) ;
		if(Math.floor(a)+1 == a)
			k++ ;
		double var1 = var - k*period ;
		return funcOnePeriod.evaluate(var1) ;
	}
	
	
	// for test
	public static void main(String[] args) {
		PeriodicFunction pFunc = new PeriodicFunction(t -> t, -1, 4) ;
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(-10, 10, 100000) ;
		double[] y = ArrayFunc.apply(t -> pFunc.evaluate(t), x) ;
		fig.plot(x, y);
		fig.RenderPlot();
		fig.run(true);
	}

}

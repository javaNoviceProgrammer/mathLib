package mathLib.integral.recent;

import mathLib.func.ArrayFunc;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class LowPassFilter {

	public static void main(String[] args) {
		double tau = 1 ; // units are second
		IntegralFunction1D impulse = t -> t<0.0 ? 0.0 : 1.0/tau * Math.exp(-t/tau) ;
		IntegralFunction1D input = t -> t<0.0 ? 0.0 : 1.0 ;
//		IntegralFunction1D input = t -> t<0.0 ? 0.0 : Math.sin(t*1) ;

		// implement generalized version of convolution
		MemoizedIntegral convolution = new MemoizedIntegral(y -> impulse.function(y), -5.0, 50.0) ;
		convolution.setNumberOfPoints(10000);
		convolution.calculate();

		MatlabChart fig = new MatlabChart() ;
		double[] time = MathUtils.linspace(-5.0, 50.0, 10000) ;
		double[] y = ArrayFunc.apply(t -> impulse.function(t), time) ;
		double[] x = ArrayFunc.apply(t -> input.function(t), time) ;
		fig.plot(time, y, "b:");
		fig.plot(time, x, "r:");
		fig.plot(convolution.getPoints(), convolution.getResults(), "g");
		fig.renderPlot();
		fig.xlabel("Time (second)");
		fig.ylabel("Function");
//		fig.legend("southeast");
//		fig.legendON();
		fig.show(true);

	}

}

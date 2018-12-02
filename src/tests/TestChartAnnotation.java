package tests;

import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.func.ArrayFunc;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestChartAnnotation {
	public static void main(String[] args) {
		MatlabChart fig = new MatlabChart() ;
//		fig.plot(new double[] {}, new double[] {});
		double[] x = MathUtils.linspace(0, Math.PI, 100) ;
		double[] y = ArrayFunc.apply(t -> Math.sin(t), x) ;
		double[] z = ArrayFunc.apply(t -> Math.cos(t), x) ;
		fig.plot(x, y, "b");
		fig.plot(x, z, "r");
		fig.renderPlot();
		fig.run(true);
		
		XYLineAnnotation ann = new XYLineAnnotation(0, 0, 5, 5) ;
		fig.getChart().getXYPlot().addAnnotation(ann);
	}
}

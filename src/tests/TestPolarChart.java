package tests;

import mathLib.func.ArrayFunc;
import mathLib.plot.PolarChart;
import mathLib.util.MathUtils;
import static java.lang.Math.* ;

public class TestPolarChart {
	
	public static void main(String[] args) {
		PolarChart fig = new PolarChart() ;
		double[] theta = MathUtils.linspace(0, 360, 1000) ;
		double[] r1 = ArrayFunc.apply(t -> abs(cos(t*PI/180.0)), theta) ;
		double[] r2 = ArrayFunc.apply(t -> abs(sin(t*PI/180.0)), theta) ;
		
		fig.plot(theta, r1);
		fig.plot(theta, r2);
		fig.renderPlot();
		fig.run(true);

	}

}

package tests;

import mathLib.plot.MatlabChart;

public class TestMeshPlot {
	public static void main(String[] args) {
		MatlabChart fig = new MatlabChart() ;
		double[] x = {} ;
		double[] y = {} ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.append(0, 0, 0);
		fig.append(0, 5, 0);
		fig.append(0, 0, 5);
		fig.append(0, 5, 5);
		fig.run(true);
		fig.markerON();
	}
}

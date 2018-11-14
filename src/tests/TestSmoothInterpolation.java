package tests;

import mathLib.fitting.interpol.LinearInterpolation1D;
import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.plot.MatlabChart;

public class TestSmoothInterpolation {
	public static void main(String[] args) {
		double[] lambda = { 1600.0, 1594.405594, 1588.850174, 1583.333333, 1577.854671, 1572.413793, 1567.010309,
				1561.643836, 1556.313993, 1551.020408, 1545.762712, 1540.540541, 1535.353535, 1530.201342, 1525.083612,
				1520.0, 1514.950166, 1509.933775, 1504.950495, 1500.0 };
		double[] neff = { 1.916769, 1.917891, 1.919005, 1.920112, 1.921211, 1.922302, 1.923386, 1.924462, 1.925531,
				1.926592, 1.927646, 1.928693, 1.929733, 1.930766, 1.931791, 1.93281, 1.933822, 1.934827, 1.935825,
				1.936816 };
		
		LinearInterpolation1D neffInterpolate = new LinearInterpolation1D(lambda, neff) ;
		
		MatlabChart fig1 = new MatlabChart() ;
		fig1.plot(lambda, ArrayFunc.apply(t -> neffInterpolate.function().evaluate(t), lambda));
		fig1.renderPlot();
		fig1.run(true);
		fig1.markerON();
		
		RealFunction ngFunc = t -> neffInterpolate.function().evaluate(t) - t * neffInterpolate.derivFunc().evaluate(t) ;
		
		MatlabChart fig2 = new MatlabChart() ;
		fig2.plot(lambda, ArrayFunc.apply(t -> ngFunc.evaluate(t), lambda));
		fig2.renderPlot();
		fig2.run(true);
		fig2.markerON();
	}

}

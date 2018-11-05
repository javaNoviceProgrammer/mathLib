package mathLib.plot.tutorial;

import static java.lang.Math.PI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mathLib.numbers.Complex;
import mathLib.numbers.ComplexMath;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class Tutorial3 {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		double[] x = MathUtils.linspace(-3 * PI, 3 * PI, 1000);
		Complex[] y = new Complex[x.length] ;
		for(int i=0; i<x.length; i++)
			y[i] = ComplexMath.sin(new Complex(x[i], 0.0)) ;
		MatlabChart fig = new MatlabChart();
		fig.plot(x, y);
		fig.renderPlot();
		fig.xlabel("X variable");
		fig.ylabel("Y variable");
		fig.run(true);
		fig.legendON();
//		fig.grid("off", "off");
	}
}

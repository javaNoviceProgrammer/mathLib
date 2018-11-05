package mathLib.plot.tutorial;

import static java.lang.Math.PI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class Tutorial1 {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		// 0. setting the UI look and feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// 1. define the x and y variables
		double[] x = MathUtils.linspace(-3 * PI, 3 * PI, 1000);
		double[] y = MathUtils.Arrays.Functions.sin(x);
		// 2. create the matlab chart using jfreechart
		MatlabChart fig = new MatlabChart();
		// 3. add as many variables to the figure
		fig.plot(x, y, "b-", 2f, "real numbers");
		// 4. after adding the plots, render the chart
		fig.renderPlot();
		// 5. set the x and y lables
		fig.xlabel("X variable");
		fig.ylabel("Y variable");
		// 6. show the plot
		fig.run(true);
//		fig.legend("northwest");
		fig.legendON();
		fig.grid("off", "off");
		fig.markerON();
	}
}

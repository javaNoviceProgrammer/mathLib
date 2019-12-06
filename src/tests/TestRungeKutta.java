package tests;

import flanagan.integration.DerivnFunction;
import flanagan.integration.RungeKutta;
import mathLib.plot.MatlabChart;
import mathLib.util.Timer;

public class TestRungeKutta {

	public static void main(String[] args) {
		double w = Math.PI ;

		DerivnFunction dfuncs = new DerivnFunction() {

			@Override
			public double[] derivn(double x, double[] yy) {
				double y = yy[0] ;
				double z = yy[1] ;
				return new double[] {z, -w*w*y};
			}
		};

		RungeKutta integrator = new RungeKutta() ;
		integrator.setStepSize(1e-4);
		integrator.setInitialValueOfX(0);
		integrator.setFinalValueOfX(1);
		integrator.setInitialValuesOfY(new double[] {0, w});

		Timer timer = new Timer() ;
		timer.start();

		double[][] sol = integrator.fourthOrder(dfuncs, 100) ;

		timer.stop();
		timer.show();

//		integrator.plot() ;

//		MathUtils.Arrays.show(y);

		MatlabChart fig = new MatlabChart() ;
		fig.plot(sol[0], sol[1], "b");
		fig.plot(sol[0], sol[2], "r");
		fig.renderPlot();
		fig.run(true);
		fig.markerON();

	}

}
